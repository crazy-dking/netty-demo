package com.cn.zj.netty.eventloop;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @Description:
 * 越到后边的知识越重要，越不可操之过急，不可大意。
 * 通过scanner进行控制关闭连接。
 * 执行了关闭任务之后，程序仍然存活，因为NioEventLoopGroup里边还有一些线程没有关闭。我们需要手动关闭。
 *
 * @Author: wangdakai
 * @Date: 2021/12/29
 */
@Slf4j
public class CloseClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new StringEncoder());
                    }
                })
                // 异步非阻塞
                .connect("localhost", 8080);
        Channel channel = channelFuture.sync().channel();
        new Thread(()->{
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String next = scanner.next();
                log.info("执行任务");
                if ("q".equals(next)) {
                    // 这个方法也是也不非阻塞的。如果在这个位置执行关闭后续处理还是会出现在没有关闭的情况，关闭处理执行了。
                    channel.close();
                    break;
                }
                channel.writeAndFlush(next);
            }
        },"scanner").start();

        log.info("主线程中执行其他的任务");
        ChannelFuture close = channel.closeFuture();
        close.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                log.info("关闭后续处理操作");
                group.shutdownGracefully();
            }
        });
    }
}
