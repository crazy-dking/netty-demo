package com.cn.zj.netty.application;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @Description:
 * @Author: wangdakai
 * @Date: 2021/12/30
 */
@Slf4j
public class HttpServerCodecServer {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG))
                                .addLast(new HttpServerCodec())
                                .addLast(new SimpleChannelInboundHandler<HttpRequest>(){

                                    @Override
                                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) throws Exception {
                                        log.info("uri:{}", httpRequest.uri());
                                        // 创建一个完整的响应
                                        DefaultFullHttpResponse response = new DefaultFullHttpResponse(httpRequest.protocolVersion(), HttpResponseStatus.OK);
                                        byte[] bytes = "<h1>zjdking</h1>".getBytes(StandardCharsets.UTF_8);
                                        // 因为浏览器不知道是否响应完成，我们需要告诉他浏览器，我就发这么长。你可以结束了。
                                        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, bytes.length);
                                        response.content().writeBytes(bytes);
                                        channelHandlerContext.channel().writeAndFlush(response);
                                    }
                                });

                    }
                })
                .bind(8080);
    }
}
