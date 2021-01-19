package com.butter.starthard.learnNetty.privateNetty.heartBeat;

import com.butter.starthard.learnNetty.privateNetty.common.MessageType;
import com.butter.starthard.learnNetty.privateNetty.dataStruct.Header;
import com.butter.starthard.learnNetty.privateNetty.dataStruct.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Auther: zhanghailong
 * @Date: 2020/11/24 16:06
 * @Description: 心跳检测 server
 */
public class HeartBeatRespHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value) {
            System.out.println("server:receive client HeartBeat ---> " + message);
            NettyMessage heartBeat = buildHeartBeat();
            ctx.writeAndFlush(heartBeat);
            System.out.println("server:send  HeartBeat to client ---> " + heartBeat);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildHeartBeat() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value);
        message.setHeader(header);
        message.setBody((byte) 0);
        return message;
    }

}
