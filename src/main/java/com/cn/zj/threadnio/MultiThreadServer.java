package com.cn.zj.threadnio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Description: 主要负责吹处理accept请求
 * @Author: wangdakai
 * @Date: 2021/11/18
 */
public class MultiThreadServer {
    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");
        // 获取channel
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        Selector selector = Selector.open();

        channel.register(selector, SelectionKey.OP_ACCEPT,null);
        channel.bind(new InetSocketAddress(8080));
        while(selector.select()>0){
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey sk = iterator.next();
                if(sk.isAcceptable()){
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel)sk.channel();
                    SocketChannel accept = serverSocketChannel.accept();
                    serverSocketChannel.configureBlocking(false);
                }


                // 最后要进行remove
                iterator.remove();
            }


        }
    }
}
