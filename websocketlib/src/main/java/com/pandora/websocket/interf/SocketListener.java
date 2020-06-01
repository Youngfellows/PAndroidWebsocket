package com.pandora.websocket.interf;

import com.pandora.websocket.response.ErrorResponse;

/**
 * WebSocket监听器
 */
public interface SocketListener {

    /**
     * 连接成功
     */
    void onConnected();

    /**
     * 连接失败
     *
     * @param cause 失败原因
     */
    void onConnectError(Throwable cause);

    /**
     * 连接断开
     */
    void onDisconnected();

    /**
     * 接收到消息
     */
    void onMessageResponse(Response message);

    /**
     * 消息发送失败或接受到错误消息等等
     */
    void onSendMessageError(ErrorResponse error);
}
