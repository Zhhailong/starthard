package com.butter.starthard.learnNetty.privateNetty;

import com.butter.starthard.learnNetty.privateNetty.common.NettyConstants;
import com.butter.starthard.learnNetty.privateNetty.handlersafe.LoginAuthReqHandler;
import com.butter.starthard.learnNetty.privateNetty.heartBeat.HeartBeatReqHandler;
import com.butter.starthard.learnNetty.privateNetty.marshalling.NettyMessageDecoder;
import com.butter.starthard.learnNetty.privateNetty.marshalling.NettyMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: zhanghailong
 * @Date: 2020/11/24 16:48
 * @Description: 客户端....
 */
public class NettyClient {

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(int port, String host) {

        //client NIO thread
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4, -8, 0));
                            ch.pipeline().addLast(new NettyMessageEncoder());
                            ch.pipeline().addLast(new ReadTimeoutHandler(50));
                            ch.pipeline().addLast(new LoginAuthReqHandler());
                            ch.pipeline().addLast(new HeartBeatReqHandler());

                        }
                    });

            //异步连接
            ChannelFuture future = b.connect(
                    new InetSocketAddress(host, port)
                    //指定本地的端口
                    //new InetSocketAddress(NettyConstants.LOCAL_IP, NettyConstants.LOCAL_PORT)
            ).sync();
            System.out.println("client: connect to server host:" + host + ", port:" + port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //释放资源，重连
            executorService.execute(new Runnable() {
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                        //重连
                        connect(NettyConstants.REMOTE_PORT, NettyConstants.REMOTE_IP);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    public static void main(String[] args) {
        new NettyClient().connect(NettyConstants.REMOTE_PORT, NettyConstants.REMOTE_IP);
    }

}
