package com.pandora.websocket.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.pandora.websocket.WebSocketSetting;
import com.pandora.websocket.deliver.ResponseDelivery;
import com.pandora.websocket.interf.IWebSocketPage;
import com.pandora.websocket.interf.Response;
import com.pandora.websocket.interf.SocketListener;
import com.pandora.websocket.response.ErrorResponse;
import com.pandora.websocket.service.WebSocketService;

/**
 * 负责页面的 WebSocketService 绑定等操作
 */
public class WebSocketServiceConnectManager {

    private static final String TAG = "WebSocketLib";

    private Context context;
    private IWebSocketPage webSocketPage;

    /**
     * 主线程Handler或者子线程Handler
     */
    private Handler mHandler;

    /**
     * WebSocket 服务是否绑定成功
     */
    private boolean webSocketServiceBindSuccess = false;
    protected WebSocketService mWebSocketService;

    private int bindTime = 0;
    /**
     * 是否正在绑定服务
     */
    private boolean binding = false;

    protected ServiceConnection mWebSocketServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            webSocketServiceBindSuccess = true;
            binding = false;
            bindTime = 0;
            mWebSocketService = ((WebSocketService.ServiceBinder) service).getService();
            mWebSocketService.addListener(mSocketListener);
            webSocketPage.onServiceBindSuccess();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binding = false;
            webSocketServiceBindSuccess = false;
            Log.e(TAG, "onServiceDisconnected:" + name);
            if (bindTime < 5 && !binding) {
                Log.d(TAG, String.format("WebSocketService 连接断开，开始第%s次重连", bindTime));
                bindService();
            }
        }
    };

    private SocketListener mSocketListener = new SocketListener() {
        @Override
        public void onConnected() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    webSocketPage.onConnected();
                }
            });
        }

        @Override
        public void onConnectError(final Throwable cause) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    webSocketPage.onConnectError(cause);
                }
            });
        }

        @Override
        public void onDisconnected() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    webSocketPage.onDisconnected();
                }
            });
        }

        @Override
        public void onMessageResponse(final Response message) {
            Log.d(TAG, "onMessageResponse: " + message.getResponseText());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    webSocketPage.onMessageResponse(message);
                }
            });
        }

        @Override
        public void onSendMessageError(final ErrorResponse error) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    webSocketPage.onSendMessageError(error);
                }
            });
        }
    };

    public WebSocketServiceConnectManager(Context context, IWebSocketPage webSocketPage) {
        this.mHandler = new Handler(Looper.getMainLooper());
        this.context = context;
        this.webSocketPage = webSocketPage;
        webSocketServiceBindSuccess = false;
    }

    public WebSocketServiceConnectManager(Context context, boolean isMain, IWebSocketPage webSocketPage) {
        this.context = context;
        this.webSocketPage = webSocketPage;
        webSocketServiceBindSuccess = false;
        if (isMain) {
            this.mHandler = new Handler(Looper.getMainLooper());
        } else {
            HandlerThread handlerThread = new HandlerThread("ws_sub_thread");
            handlerThread.start();
            mHandler = new Handler(handlerThread.getLooper());
        }
    }

    public void onCreate() {
        for (int i = 0; i < 10; i++) {
            bindService();
        }
    }

    /**
     * 绑定WS服务
     */
    private void bindService() {
        binding = true;
        webSocketServiceBindSuccess = false;
        Intent intent = new Intent(context, WebSocketService.class);
        context.bindService(intent, mWebSocketServiceConnection, Context.BIND_AUTO_CREATE);
        bindTime++;
    }

    /**
     * 发送
     *
     * @param text
     */
    public void sendText(String text) {
        if (webSocketServiceBindSuccess && mWebSocketService != null) {
            mWebSocketService.sendText(text);
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(2);
            errorResponse.setCause(new Throwable("WebSocketService dose not bind!"));
            errorResponse.setRequestText(text);
            ResponseDelivery delivery = new ResponseDelivery();
            delivery.addListener(mSocketListener);
            WebSocketSetting.getResponseProcessDelivery().onSendMessageError(errorResponse, delivery);
            if (!binding) {
                bindTime = 0;
                Log.d(TAG, String.format("WebSocketService 连接断开，开始第%s次重连", bindTime));
                bindService();
            }
        }
    }

    /**
     * 连接WS
     */
    public void reconnect() {
        if (webSocketServiceBindSuccess && mWebSocketService != null) {
            mWebSocketService.reconnect();
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(2);
            errorResponse.setCause(new Throwable("WebSocketService dose not bind!"));
            ResponseDelivery delivery = new ResponseDelivery();
            delivery.addListener(mSocketListener);
            WebSocketSetting.getResponseProcessDelivery().onSendMessageError(errorResponse, delivery);
            if (!binding) {
                bindTime = 0;
                Log.d(TAG, String.format("WebSocketService 连接断开，开始第%s次重连", bindTime));
                bindService();
            }
        }
    }

    public void onDestroy() {
        binding = false;
        bindTime = 0;
        context.unbindService(mWebSocketServiceConnection);
        Log.d(TAG, context.toString() + "已解除 WebSocketService 绑定");
        webSocketServiceBindSuccess = false;
        mWebSocketService.removeListener(mSocketListener);
    }
}
