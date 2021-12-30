package com.cn.zj.netty.buffer;

import com.cn.zj.util.LogUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.StandardCharsets;

/**
 * @Description:
 * @author: zjdking
 * @date: 2021/12/29 21:31
 */
public class CreatBuf {
    public static void main(String[] args) {
        // 在直接内存上创建 默认大小265
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        LogUtil.log(buffer);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <300; i++) {
            sb.append('a');
        }
        buffer.writeBytes(sb.toString().getBytes(StandardCharsets.UTF_8));
        LogUtil.log(buffer);
        // 创建对内存
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.heapBuffer();
    }
}
