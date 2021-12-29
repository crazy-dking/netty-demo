package com.cn.zj.netty.eventloop;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:  之后netty中有很多方法都是异步非阻塞的。
 * future promise 都是和异步方法配套使用的。
 * @Author: wangdakai
 * @Date: 2021/12/29
 */
@Slf4j
public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        ChannelFuture future = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringEncoder());
                    }
                })
                // connect是main调用的一个一部非阻塞的线程执行的；
                .connect("127.0.0.1", 8080);
//        方法1通过sync进行同步等待。这样对于connect的异步非阻塞相当于没有使用到。白白等待。
//         可想而知，如果我们没有通过sync同步，我们的channel直接→主线程执行，链接还没有建立，这样就会出现问。
//        ChannelFuture sync = future.sync();
//        Channel channel = future.channel();
//        channel.writeAndFlush("hello world");

//        2.回调函数形式发送
        future.addListener((ChannelFutureListener) channelFuture -> {
            Channel channel = channelFuture.channel();
            log.info("回调函数方式发送");
            channel.writeAndFlush("hello world");
        });
    }
}
