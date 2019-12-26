package top.zhaohaoren.netty.netty.pack.solution;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 从字节中解析出 自定义的协议包内容
 *
 * @author zhaohaoren
 */
public class MyMessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("解码");
        // 读取的字节转为 协议数据包
        int len = in.readInt();
        byte[] bytes = new byte[len];
        in.readBytes(bytes);

        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(len);
        messageProtocol.setContent(bytes);

        // 向下传递
        out.add(messageProtocol);
    }
}
