package com.pandora.websocket.base;

import android.content.Context;
import android.util.Log;

import com.pandora.websocket.interf.IWebSocketPage;
import com.pandora.websocket.interf.Response;
import com.pandora.websocket.manager.WebSocketServiceConnectManager;
import com.pandora.websocket.response.ErrorResponse;

public class AbsWebSocket implements IWebSocketPage {

    public String TAG = this.getClass().getSimpleName();

    private WebSocketServiceConnectManager mConnectManager;

    public AbsWebSocket(Context context) {
        mConnectManager = new WebSocketServiceConnectManager(context, this);
        mConnectManager.onCreate();
    }

    @Override
    public void onServiceBindSuccess() {

    }

    @Override
    public void sendText(String text) {
        mConnectManager.sendText(text);
    }

    @Override
    public void reconnect() {
        mConnectManager.reconnect();
    }

    @Override
    public void onConnected() {
        Log.d(TAG, "onConnected: ");
    }

    @Override
    public void onConnectError(Throwable cause) {
        Log.d(TAG, "onConnectError: ");
    }

    @Override
    public void onDisconnected() {
        Log.d(TAG, "onDisconnected: ");
    }

    @Override
    public void onMessageResponse(Response message) {
        Log.d(TAG, "onMessageResponse: ");
    }

    @Override
    public void onSendMessageError(ErrorResponse error) {
        Log.d(TAG, "onSendMessageError: ");
    }
}
