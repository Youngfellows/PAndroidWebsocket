package com.pandora.websocket.conf;

/**
 * 消息类型
 */
public interface MessageType {

    /**
     * 连接WebSocket
     */
    int CONNECT = 0;

    /**
     * 重新连接新WebSocket
     */
    int RESET_CONNECT = 1;

    /**
     * 断开连接，主动关闭或被动关闭
     */
    int DISCONNECT = 2;

    /**
     * 结束线程
     */
    int QUIT = 3;

    /**
     * 通过WebSocket连接发送数据
     */
    int SEND_MESSAGE = 4;

    /**
     * 通过WebSocket连接发送心跳数据
     */
    int SEND_PING = 5;

    /**
     * 接收到WebSocket返回数据
     */
    int RECEIVE_MESSAGE = 6;
}
