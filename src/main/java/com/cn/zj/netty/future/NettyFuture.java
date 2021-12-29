package com.cn.zj.netty.future;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @Author: wangdakai
 * @Date: 2021/12/29
 */
@Slf4j
public class NettyFuture {
    public static void main(String[] args) {
        // 和jdk类型，netty future也是需要线程池的，只不过netty中通过eventLoop管理线程
        EventLoop loop = new NioEventLoopGroup().next();
        Future<Integer> future = loop.submit(() -> {
            log.info("线程执行任务");
            Thread.sleep(1000);
            return 529;
        });
        // 我们会发现最终执行打印结果的线程是，执行计算的线程。并非主线程。并且这个方法时异步非阻塞的。如果想使用同步 sync
        future.addListener(future1 -> log.info(""+ future1.getNow()));
        log.info("执行任务");
    }
}

