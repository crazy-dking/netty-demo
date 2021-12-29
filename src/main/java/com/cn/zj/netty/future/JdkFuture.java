package com.cn.zj.netty.future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @Description:
 * @Author: wangdakai
 * @Date: 2021/12/29
 */
@Slf4j
public class JdkFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // jdk future一般是和线程池连用
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1, 1000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5)
                , Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        Future<Integer> task = pool.submit(() -> {
            log.info("线程池执行计算");
            Thread.sleep(1000);
            return 10;
        });
        // jdk future的get会阻塞后续结果的执行
        log.info("得到执行结果{}",task.get());
        log.info("主线程其他操作");
    }
}