package com.cn.zj.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import org.junit.Test;

/**
 * @Description:
 * @author: zjdking
 * @date: 2021/11/15 22:33
 */
public class ServerDemo {
    public static void main(String[] args) {
        // 服务器端启动器，负责组装netty组件
        new ServerBootstrap()
                // 注册线程选择器
                .group(new NioEventLoopGroup())
                // 选择channel的种类，比如kq,epoll,nio
                .channel(NioServerSocketChannel.class)
                // 决定执行那些操作。ChannelInitializer链接建立后被调用
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        // 字符串处理handler 解码
                        channel.pipeline().addLast(new StringDecoder());
                        // 自定义handler
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                })
                .bind(8080);
    }

}
