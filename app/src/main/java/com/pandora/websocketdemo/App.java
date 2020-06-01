package com.pandora.websocketdemo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pandora.websocket.service.WebSocketService;
import com.pandora.websocket.WebSocketSetting;
import com.pandora.websocketdemo.dispatcher.AppResponseDispatcher;
import com.pandora.websocketdemo.dispatcher.EchoResponseDispatcher;


public class App extends Application {

    private String TAG = this.getClass().getSimpleName();

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        mContext = this;
        init();
    }

    public static Context getContext() {
        return mContext;
    }

    public void init() {
        //配置 WebSocket，必须在 WebSocket 服务启动前设置
        WebSocketSetting.setConnectUrl("wss://echo.websocket.org");//必选
        //WebSocketSetting.setResponseProcessDelivery(new AppResponseDispatcher());
        WebSocketSetting.setResponseProcessDelivery(new EchoResponseDispatcher());
        WebSocketSetting.setReconnectWithNetworkChanged(true);

        //启动 WebSocket 服务
        startService(new Intent(this, WebSocketService.class));
    }
}
