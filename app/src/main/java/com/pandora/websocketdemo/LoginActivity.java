package com.pandora.websocketdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.pandora.websocket.WebSocketSetting;
import com.pandora.websocket.base.AbsWebSocketActivity;
import com.pandora.websocket.response.ErrorResponse;
import com.pandora.websocket.interf.Response;
import com.pandora.websocketdemo.conf.Token;
import com.pandora.websocketdemo.response.LoginResponse;

import java.util.List;
import java.util.Random;

public class LoginActivity extends AbsWebSocketActivity {

    /**
     * 假设这是登陆的接口Path
     */
    private static final String LOGIN_PATH = "Login";

    private EditText etAccount;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        etAccount = (EditText) findViewById(R.id.et_account);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString();
                String password = etPassword.getText().toString();
                if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                    UiUtil.showToast(LoginActivity.this, "输入不能为空");
                    return;
                }
                login(account, password);
            }
        });
    }

    private void login(String account, String password) {
        JSONObject params = new JSONObject();
        JSONObject command = new JSONObject();
        JSONObject parameters = new JSONObject();

        command.put("path", LOGIN_PATH);

        parameters.put("username", account);
        parameters.put("password", password);

        params.put("command", command);
        params.put("parameters", parameters);
        sendText(params.toJSONString());
    }

    @Override
    public void resetConnect() {
        //配置 WebSocket，必须在 WebSocket 服务启动前设置
        Token token = new Token();
        String wsHost = token.wsHost();
        WebSocketSetting.setConnectUrl(wsHost);//必选
        super.resetConnect();
    }

    @Override
    public void onMessageResponse(Response message) {
        UiUtil.showToast(LoginActivity.this, "登陆成功: " + ((LoginResponse) message).getResponseEntity().getParameters().getUsername());
    }

    @Override
    public void onSendMessageError(ErrorResponse error) {
        UiUtil.showToast(LoginActivity.this, error.getDescription());
    }
}
