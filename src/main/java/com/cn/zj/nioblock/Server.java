package com.cn.zj.niozuse;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: wangdakai
 * @Date: 2021/11/18
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        ServerSocketChannel channel = ServerSocketChannel.open();
        // 非常重要，编程非阻塞模式。
        channel.configureBlocking(false);

        channel.bind(new InetSocketAddress(8080));
        List<SocketChannel> channels = new ArrayList<>();
        while(true){
            System.out.println("connecting ... ");
            // 阻塞 在非阻塞的时候返回值null
            SocketChannel accept = channel.accept();
            System.out.println("connecting success...");

            channels.add(accept);
            for(SocketChannel sc : channels){
                System.out.println("before read...");
                // 阻塞  在非阻塞的时候返回值为0
                int len = sc.read(byteBuffer);
                byteBuffer.flip();
                System.out.println(new String(byteBuffer.array() ,0,len));
                byteBuffer.clear();
            }
            try {
                // 防止cpu一直跑。跑死。；之后我们就会通过selector防止这种一直循环的现象。
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
