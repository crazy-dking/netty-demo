package com.cn.zj.netty.eventloop;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @Description:
 * @Author: wangdakai
 * @Date: 2021/12/29
 */
@Slf4j
public class EventLoopServer {
    public static void main(String[] args) {
        EventLoopGroup defaultEventLoop = new DefaultEventLoopGroup();
        new ServerBootstrap()
                // 指定使用的eventLoop组，这个相当于是多线程版本的 boss,worker; eventLoop相当于线程版本的selector
                .group(new NioEventLoopGroup(),new NioEventLoopGroup())
                // 指定管道
                .channel(NioServerSocketChannel.class)
                // 指定处理器; ChannelInitializer一个特殊的channel处理器，他会程生成一个channel，注册到eventLoop上
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        // ChannelInboundHandlerAdapter接受和发送消息的处理器。
                        nioSocketChannel.pipeline().addLast("handler",new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                log.info("服务端读取到的数据:{}",buf.toString(Charset.defaultCharset()));
                                // handler1执行完成要交给下边  这个很重要
                                ctx.fireChannelRead(msg);
                            }
                        }).addLast(defaultEventLoop,"default",new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                log.info("服务端读取到的数据:{}",buf.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                })
                .bind(8080);
    }
}
