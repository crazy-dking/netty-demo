package com.cn.zj.netty.eventloop;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * eventLoop 他继承自线程池，所以可以调用线程池中的方法
 * @Author: wangdakai
 * @Date: 2021/12/24
 */
@Slf4j
public class EventLoopTest {
    public static void main(String[] args) {
        EventLoopGroup eventLoop = new NioEventLoopGroup(2);
//        System.out.println(Runtime.getRuntime().availableProcessors());
//        for (int i = 0; i < 4; i++) {
//            System.out.println(eventLoop.next());
//        }
//
//        eventLoop.next().submit(()->{
//            log.info("hello eventLoop");
//        });
        // 定时任务可以做keepalive的链接保护。
        eventLoop.next().scheduleAtFixedRate(() -> {
            log.info("hello");
        },0,1, TimeUnit.SECONDS);

    }
}
