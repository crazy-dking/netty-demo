package com.cn.zj.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @Description:
 * @author: zjdking
 * @date: 2021/12/29 21:47
 */
public class ByteBufPool {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        System.out.println(buffer.getClass());
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.heapBuffer();
        System.out.println(byteBuf.getClass());
        ByteBuf buffer1 = ByteBufAllocator.DEFAULT.directBuffer();
        System.out.println(buffer1.getClass());
/*      结果如下
        class io.netty.buffer.PooledUnsafeDirectByteBuf
        class io.netty.buffer.PooledUnsafeHeapByteBuf
        class io.netty.buffer.PooledUnsafeDirectByteBuf*/
    }
}
