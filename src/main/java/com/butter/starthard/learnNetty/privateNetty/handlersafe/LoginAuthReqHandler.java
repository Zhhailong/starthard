package com.butter.starthard.learnNetty.privateNetty.handlersafe;

import com.butter.starthard.learnNetty.privateNetty.common.MessageType;
import com.butter.starthard.learnNetty.privateNetty.dataStruct.Header;
import com.butter.starthard.learnNetty.privateNetty.dataStruct.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Auther: zhanghailong
 * @Date: 2020/11/24 14:53
 * @Description: 握手认证(client)
 */
public class LoginAuthReqHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //握手请求
        ctx.writeAndFlush(buildLoginReq());
        System.out.println("client: send LoginAuthReq ---> " + buildLoginReq());
    }

    private NettyMessage buildLoginReq() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType((byte) 3);
        message.setHeader(header);
        message.setBody((byte) 0);
        return message;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //握手应答消息
        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value) {
            byte loginResult = (Byte) message.getBody();
            if (loginResult != (byte) 0) {
                //如果应答消息体不为0则认证失败
                ctx.close();
            } else {
                System.out.println("client:Login is OK --->" + message);
                ctx.fireChannelRead(message);
            }

        } else {
            //通知下一个Handler
            ctx.fireChannelRead(message);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
