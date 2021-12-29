package com.cn.zj.netty.future;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: wangdakai
 * @Date: 2021/12/29
 */
@Slf4j
public class NettyPromise {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        EventLoop loop = new NioEventLoopGroup().next();
        // promise是依托于线程，我们可以手动将结果放到promise中
        DefaultPromise<Object> promise = new DefaultPromise<>(loop);
        new Thread(()->{
            try {
                log.info("执行计算任务");
                TimeUnit.SECONDS.sleep(1);
                promise.setSuccess(529);
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        // 我们需要注意观察执行的时间。我们发现他并不是等待任务执行结束才会返回结果，
        // 他会在promise.setSuccess的时候就会执行。
        log.info("执行结果是：{}",promise.get());
        log.info("执行主线程其他任务");
    }
}
