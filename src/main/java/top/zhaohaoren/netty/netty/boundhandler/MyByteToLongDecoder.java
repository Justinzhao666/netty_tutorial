package top.zhaohaoren.netty.netty.boundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 自定义解码器。读取数据解码为Long。
 *
 * @author zhaohaoren
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 每大于8个字节，就作为一个long读取到list中去。最后out的list不为空就将数据传入下一个handler
        System.out.println("解码 byte -> long");
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
