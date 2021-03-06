package com.pandora.websocket.receiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.RequiresPermission;
import android.util.Log;

import com.pandora.websocket.service.WebSocketService;

/**
 * 监听网络变化广播，网络变化时自动重连
 */
public class NetworkChangedReceiver extends BroadcastReceiver {

    private final String TAG = this.getClass().getSimpleName();

    private WebSocketService socketService;

    public NetworkChangedReceiver() {
    }

    public NetworkChangedReceiver(WebSocketService socketService) {
        this.socketService = socketService;
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager == null)
                return;
            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            if (activeNetwork != null) {
                if (activeNetwork.isConnected()) {
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        Log.i(TAG, "网络连接发生变化，当前WiFi连接可用，正在尝试重连。");
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        Log.i(TAG, "网络连接发生变化，当前移动连接可用，正在尝试重连。");
                    }
                    if (socketService != null) {
                        socketService.reconnect();
                    }
                } else {
                    Log.i(TAG, "当前没有可用网络");
                }
            }
        }
    }
}
