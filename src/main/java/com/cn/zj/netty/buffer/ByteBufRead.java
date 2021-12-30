package com.cn.zj.netty.buffer;

import com.cn.zj.util.LogUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @Description:
 * @author: zjdking
 * @date: 2021/12/29 21:57
 */
public class ByteBufRead {
    public static void main(String[] args) {
        // 创建ByteBuf
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(16, 20);
        // 向buffer中写入数据
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        buffer.writeInt(5);

        // 读取4个字节
//        System.out.println(buffer.readByte());
//        System.out.println(buffer.readByte());
//        System.out.println(buffer.readByte());
//        System.out.println(buffer.readByte());
        LogUtil.log(buffer);

        // 通过mark与reset实现重复读取
        buffer.markReaderIndex();
        //　这个位置他一次会读取四个字节
        System.out.println(buffer.readInt());
        LogUtil.log(buffer);
        buffer.resetReaderIndex();
        LogUtil.log(buffer);
    }
}
