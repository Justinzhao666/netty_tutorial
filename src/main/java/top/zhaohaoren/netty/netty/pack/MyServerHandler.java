package top.zhaohaoren.netty.netty.pack;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        // 将bytes转为string
        String content = new String(bytes, CharsetUtil.UTF_8);
        System.out.println("服务器端接受到数据：" + content);
        System.out.println("服务器第" + (++count) + "次接受客户端的消息！");
        // 回送消息给客户端，这里回送一个随机数
        ctx.writeAndFlush(Unpooled.copiedBuffer(UUID.randomUUID().toString(), CharsetUtil.UTF_8));
    }
}
