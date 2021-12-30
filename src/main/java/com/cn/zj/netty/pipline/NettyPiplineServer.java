package com.cn.zj.netty.pipline;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @Description:
 * @Author: wangdakai
 * @Date: 2021/12/29
 */
@Slf4j
public class NettyPiplineServer {
    public static void main(String[] args) {

        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 在实际输出 head  h1 h2 h4 h3 tail;对于写入是从后向前
                        socketChannel.pipeline().addLast("handler1",new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("handler1");
                                // 如果没有这个东西将无法将东西传递给下一边
                                // 或者通过ctx.fireChannelRead(msg);进行传递
                                super.channelRead(ctx, msg);
                            }
                        }).addLast("handler2",new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("handler2");
                                // 我们需要写入
                                super.channelRead(ctx, msg);
                                socketChannel.writeAndFlush(ctx.alloc().buffer().writeBytes("Server...".getBytes(Charset.defaultCharset())));
                            }
                        }).addLast("handler3",new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.info("handler3");
                                super.write(ctx, msg, promise);
                            }
                        }).addLast("handler4",new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.info("handler4");
                                super.write(ctx, msg, promise);
                            }
                        });
                    }
                })
                .bind(8080);
    }
}
