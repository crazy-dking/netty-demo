package com.cn.zj.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @Description:
 * @Author: wangdakai
 * @Date: 2021/12/28
 */
public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("127.0.0.1",8080));
        sc.write(Charset.defaultCharset().encode("zjdking"));
        System.out.println();
    }
}
