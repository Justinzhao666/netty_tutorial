package top.zhaohaoren.netty.netty.boundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 客户端将Long类型数据转为Byte类型
 *
 * @author zhaohaoren
 */
public class MyLongToByteEncoder extends MessageToByteEncoder<Long> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("编码 long -> byte");
        System.out.println("msg=" + msg);
        out.writeLong(msg);
    }
}
