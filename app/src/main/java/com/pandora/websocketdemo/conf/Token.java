package com.pandora.websocketdemo.conf;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Token {

    private String TAG = this.getClass().getSimpleName();

    private final List<String> WS_TOKEN = new ArrayList<String>() {
        {
            add("xxxxx");
            add("xxxxx");
            add("xxxxx");
            add("xxxxx");
        }
    };

    /**
     * WS服务Host
     */
    private final String WS_HOST = "wss://echo.websocket.org";

    /**
     * 获取Token
     *
     * @return
     */
    private String getToken() {
        Random random = new Random();
        int i = random.nextInt(WS_TOKEN.size());
        String token = WS_TOKEN.get(i);
        token = token.replace(" ", "%20");
        return token;
    }

    /**
     * 获取WS Host
     */
    public String wsHost() {
        String token = getToken();
        String wsUrl = WS_HOST + "token=" + token;
        Log.d(TAG, "wsHost: " + wsUrl);
        return WS_HOST;
    }
}
