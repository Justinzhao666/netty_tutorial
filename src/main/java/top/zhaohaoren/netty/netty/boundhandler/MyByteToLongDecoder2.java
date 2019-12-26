package top.zhaohaoren.netty.netty.boundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 自定义解码器。读取数据解码为Long。
 * 使用的是 扩展了的ReplayingDecoder。该ReplayingDecoder不需要再判断和循环读取readableBytes，内部封装了判断。
 *
 * @author zhaohaoren
 */
public class MyByteToLongDecoder2 extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 每大于8个字节，就作为一个long读取到list中去。最后out的list不为空就将数据传入下一个handler
        System.out.println("解码 byte -> long");
        out.add(in.readLong());
    }
}
