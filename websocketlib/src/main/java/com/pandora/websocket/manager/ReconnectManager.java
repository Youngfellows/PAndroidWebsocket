package com.pandora.websocket.manager;

import android.os.Handler;
import android.util.Log;

import com.pandora.websocket.WebSocketThread;
import com.pandora.websocket.conf.MessageType;

import org.java_websocket.client.WebSocketClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 负责 WebSocket 重连
 */
public class ReconnectManager {

    private final String TAG = this.getClass().getSimpleName();

    /**
     * WS线程
     */
    private WebSocketThread mWebSocketThread;

    /**
     * 是否正在重连
     */
    private volatile boolean retrying;

    /**
     * 是否销毁资源
     */
    private volatile boolean destroyed;

    /**
     * 线程池
     */
    private final ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

    public ReconnectManager(WebSocketThread mWebSocketThread) {
        this.mWebSocketThread = mWebSocketThread;
        retrying = false;
        destroyed = false;
    }

    /**
     * 开始重新连接，连接方式为每个500ms连接一次，持续十五次。
     */
    public synchronized void performReconnect() {
        if (retrying) {
            Log.i(TAG, "正在重连，请勿重复调用。");
        } else {
            retry();
        }
    }

    /**
     * 开始重连
     */
    private synchronized void retry() {
        if (!retrying) {
            retrying = true;
            synchronized (singleThreadPool) {
                singleThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        retrying = true;
                        for (int i = 0; i < 20; i++) {
                            if (destroyed) {
                                retrying = false;
                                return;
                            }
                            Handler handler = mWebSocketThread.getHandler();
                            WebSocketClient websocket = mWebSocketThread.getSocket();
                            if (handler != null && websocket != null) {
                                if (mWebSocketThread.getConnectState() == 2) {
                                    break;
                                } else if (mWebSocketThread.getConnectState() == 1) {
                                    continue;
                                } else {
                                    handler.sendEmptyMessageDelayed(MessageType.CONNECT, 1000 * 3);
                                }
                            } else {
                                break;
                            }
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                Log.e(TAG, "retry()", e);
                                if (destroyed = true) {
                                    retrying = false;
                                    return;
                                } else {
                                    Thread.currentThread().interrupt();
                                }
                            }
                        }
                        retrying = false;
                    }
                });
            }
        }
    }

    /**
     * 销毁资源，并停止重连
     */
    public void destroy() {
        destroyed = true;
        if (singleThreadPool != null) {
            singleThreadPool.shutdownNow();
        }
        mWebSocketThread = null;
    }
}
