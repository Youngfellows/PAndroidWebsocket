package com.pandora.websocketdemo.response;

import com.pandora.websocket.interf.Response;
import com.pandora.websocketdemo.entity.LoginResponseEntity;

public class LoginResponse implements Response<LoginResponseEntity> {

    private String responseText;

    private LoginResponseEntity responseEntity;

    public LoginResponse(String responseText, LoginResponseEntity responseEntity) {
        this.responseText = responseText;
        this.responseEntity = responseEntity;
    }

    @Override
    public String getResponseText() {
        return responseText;
    }

    @Override
    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    @Override
    public LoginResponseEntity getResponseEntity() {
        return this.responseEntity;
    }

    @Override
    public void setResponseEntity(LoginResponseEntity responseEntity) {
        this.responseEntity = responseEntity;
    }
}
