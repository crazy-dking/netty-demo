package com.cn.zj.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Description:
 * selector的 select 方法可以被唤醒。
 * @Author: wangdakai
 * @Date: 2021/12/28
 */
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
                    //
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                }
            }
        }

    }
    class Worker implements Runnable{
        // 执行任务的线程
        private Thread thread;
        // 因为我们的管道都是注册到selector上的，所以我们操作需要selector
        private Selector selector;
        private String threadName;
        private volatile boolean state = false;

        public Worker(String threadName) {
            this.threadName = threadName;
        }
        public void register() throws IOException {
            if(!state){
                thread = new Thread(this);
                thread.start();
                selector = Selector.open();
                this.state=true;
            }
        }

        @Override
        public void run() {

        }
    }
}
