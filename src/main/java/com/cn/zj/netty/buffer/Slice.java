package com.cn.zj.netty.buffer;

import com.cn.zj.util.LogUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @Description:
 * @author: zjdking
 * @date: 2021/12/29 22:18
 */
public class Slice {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(16, 20);
        buffer.writeBytes(new byte[]{1,2,3,4,5,6,7,8,9,10});
        // 切片;这东西在py 和go中直接就有
        ByteBuf slice = buffer.slice(0, 5);
        ByteBuf slice1 = buffer.slice(5, 5);
        // 为了避免被buffer 错误释放，我们在每一个切片后边都加一个retain
        slice.retain();
        slice1.retain();

        LogUtil.log(slice);
        LogUtil.log(slice1);

        // 更改原始数组的值，在看切片，我们发现一起变了，用java来说他们就是一个引用，
        buffer.setByte(0,9);
        LogUtil.log(buffer);
        LogUtil.log(slice);
    }
}
