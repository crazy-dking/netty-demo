package com.cn.zj.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Description:
 * selector的 select 方法可以被唤醒。
 * @Author: wangdakai
 * @Date: 2021/12/28
 */
@Slf4j
public class MutiThreadServer {
    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");
        // 或得到服务端 网络请求 管道
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 默认我的阻塞队列是阻塞的，我们要设置成非阻塞
        ssc.configureBlocking(false);
        // 服务端绑定的地址
        ssc.bind(new InetSocketAddress(8080));

        // 获取到selector
        Selector boss = Selector.open();
        // 将管道注册到selector上，并且设置监听的类型。
        ssc.register(boss, SelectionKey.OP_ACCEPT);
        Worker worker = new Worker("worker-0");

        while(boss.select()>0){
            // 如果没有任何消息，boss.selector将会被阻塞，否则将会返还会一个大于0的数字
            // 获取SelectionKey中的
            Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                // 无论什么时候要将这个remove掉，否则对于已经处理过的时间再次轮回会出现空指针。
                iterator.remove();
                // 如果我们的是就绪状态，处理accept事件。
                if(key.isAcceptable()){
                    SocketChannel sc = ssc.accept();
                    log.info("客户端已连接{}", sc.getRemoteAddress());
                    sc.configureBlocking(false);
                    //　在worker中的selector注册监听
                    worker.register(sc);
                }
            }
        }

    }

    static class Worker implements Runnable{
        // 执行任务的线程
        private Thread thread;
        // 因为我们的管道都是注册到selector上的，所以我们操作需要selector
        private Selector selector;
        private String threadName;
        private volatile boolean state = false;
        // 通过线程安全的队列进行解耦
        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();

        public Worker(String threadName) {
            this.threadName = threadName;
        }
        public void register(SocketChannel sc) throws IOException {
            if(!state){
                thread = new Thread(this);
                // 开启一个新的selector ,先open ,否则会出现空指针
                selector = Selector.open();
                thread.start();
                this.state=true;
            }
            // 任务添加到队列中
            queue.add(()->{
                // 这个位置的注册要在我们的selector阻塞之前执行，否则将会一直阻塞。即使侥幸过去了，第二个客户端还是无法进行注册。
                try {
                    log.info("读事件注册前");
                    sc.register(selector, SelectionKey.OP_READ, null);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            // 这个东西可以唤醒selector的阻塞事件。类似LockSupport
            selector.wakeup();
        }

        @Override
        public void run() {
            while(true){
                try {
                    // 在没有事件注册进来会一直阻塞在这个位置
                    log.info("worker管道阻塞前");
                    selector.select();
                    Runnable task = queue.poll();
                    if (task!=null) {
                        task.run();
                    }
                    log.info("开始工作");
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()) {
                            SocketChannel sc = (SocketChannel) key.channel();
                            ByteBuffer bf = ByteBuffer.allocate(16);
                            //将管道中的数据写入到buffer;如果不写入，管道中的会一直有数据，就会导致死循环

                            sc.read(bf);
                            bf.flip();
                            String s = Charset.defaultCharset().decode(bf).toString();
                            log.info("服务器读取到客户端的值为：{}",s);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
