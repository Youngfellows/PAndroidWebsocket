package com.pandora.websocketdemo.ws;


import android.content.Context;
import android.util.Log;

import com.pandora.websocket.WebSocketSetting;
import com.pandora.websocket.base.AbsWebSocket;
import com.pandora.websocket.interf.Response;
import com.pandora.websocket.response.ErrorResponse;
import com.pandora.websocketdemo.conf.Token;

public class PandoraWebSocket extends AbsWebSocket {

    public PandoraWebSocket(Context context) {
        super(context);
    }

    @Override
    public void onMessageResponse(Response message) {
        super.onMessageResponse(message);
        Log.d(TAG, "onMessageResponse: " + Thread.currentThread().getName());
        Log.d(TAG, "onMessageResponse: " + message.getResponseText());
    }

    @Override
    public void onSendMessageError(ErrorResponse error) {
        super.onSendMessageError(error);
        Log.d(TAG, "onSendMessageError: ");
    }

    @Override
    public void resetConnect() {
        //配置 WebSocket，必须在 WebSocket 服务启动前设置
        Token token = new Token();
        String wsHost = token.wsHost();
        WebSocketSetting.setConnectUrl(wsHost);//必选
        Log.d(TAG, "resetConnect: wsHost: " + wsHost);
        super.resetConnect();
    }
}