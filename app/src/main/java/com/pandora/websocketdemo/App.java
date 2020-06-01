package com.pandora.websocketdemo;

import android.app.Application;
import android.content.Intent;

import com.pandora.websocket.service.WebSocketService;
import com.pandora.websocket.WebSocketSetting;
import com.pandora.websocketdemo.dispatcher.AppResponseDispatcher;
import com.pandora.websocketdemo.dispatcher.EchoResponseDispatcher;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //配置 WebSocket，必须在 WebSocket 服务启动前设置
        WebSocketSetting.setConnectUrl("wss://echo.websocket.org");//必选
        //WebSocketSetting.setResponseProcessDelivery(new AppResponseDispatcher());
        WebSocketSetting.setResponseProcessDelivery(new EchoResponseDispatcher());
        WebSocketSetting.setReconnectWithNetworkChanged(true);

        //启动 WebSocket 服务
        startService(new Intent(this, WebSocketService.class));
    }
}
