package com.cn.zj.netty.application;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.StandardCharsets;

/**
 * @Description:
 * @Author: wangdakai
 * @Date: 2021/12/30
 */
public class LengthFIeldBaseFrameDecoderClient {
    public static void main(String[] args) {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                new LoggingHandler(LogLevel.DEBUG),
                new LengthFieldBasedFrameDecoder(1024,1,4,1,0)
        );
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        send(buffer, "hello");
        embeddedChannel.writeInbound(buffer);
        send(buffer, "world");
        embeddedChannel.writeInbound(buffer);

    }

    private static void send(ByteBuf buffer, String hello) {
        int length = hello.length();
        byte[] bytes = hello.getBytes(StandardCharsets.UTF_8);
        buffer.writeByte(0xCA);
        buffer.writeInt(length);
        buffer.writeByte(0xCA);
        buffer.writeBytes(bytes);
    }
}
