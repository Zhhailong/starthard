package com.butter.starthard.learnNetty.privateNetty.dataStruct;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: zhanghailong
 * @Date: 2020/11/19 14:50
 * @Description: netty私有协议定义消息头
 */
public final class Header {

    private int crcCode = 0xabef0101; //消息校验码

    private int length; //消息长度

    public long getSessionID() {
        return sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    private long sessionID; //会话ID

    private byte type; //会话类型

    private byte priority; //消息优先级

    private  Map<String,Object> attachment = new HashMap<String,Object>(); //附件

    public final int getCrcCode() {
        return crcCode;
    }

    public final void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }




    public final byte getType() {
        return type;
    }

    public final void setType(byte type) {
        this.type = type;
    }

    public final byte getPriority() {
        return priority;
    }

    public final void setPriority(byte priority) {
        this.priority = priority;
    }


    public final Map<String, Object> getAttachment() {
        return attachment;
    }

    public final void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "Header{" +
                "crcCode=" + crcCode +
                ", leagth=" + length +
                ", sessionId=" + sessionID +
                ", type=" + type +
                ", priority=" + priority +
                ", attachment=" + attachment +
                '}';
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
