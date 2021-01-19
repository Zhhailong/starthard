package com.butter.starthard.learnNetty.privateNetty.common;

/**
 * @Auther: zhanghailong
 * @Date: 2020/11/23 20:04
 * @Description: xi
 */
public enum MessageType {

    //心跳请求，应答

    HEARTBEAT_REQ((byte) 5),

    HEARTBEAT_RESP((byte) 6),

    //握手请求，应答

    LOGIN_REQ((byte) 3),

    LOGIN_RESP((byte) 4);

    public byte value;

    MessageType(byte value) {

        this.value = value;

    }

}
