package com.pandora.websocketdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.pandora.websocketdemo.ws.PandoraWebSocket;

public class MainActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    private PandoraWebSocket mPandoraWebSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        mPandoraWebSocket = new PandoraWebSocket(App.getContext());
    }

    /**
     * 测试WS
     *
     * @param view
     */
    public void onTestWS(View view) {
        Log.d(TAG, "onTestWS: ");
        mPandoraWebSocket.sendText("{\"parameters\":{\"password\":\"123456\",\"username\":\"预估故居比\"},\"command\":{\"path\":\"Login\"}}");
    }

    /**
     * 测试WS登录
     *
     * @param view
     */
    public void onLoginWS(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
