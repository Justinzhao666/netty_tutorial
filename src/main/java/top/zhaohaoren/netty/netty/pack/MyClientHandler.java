package top.zhaohaoren.netty.netty.pack;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] buffer = new byte[msg.readableBytes()];
        msg.readBytes(buffer);

        String string = new String(buffer, CharsetUtil.UTF_8);
        System.out.println("客户端收到：" + string);
        System.out.println("客户端第" + (++count) + "次接受服务器的消息！");
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 使用客户端 循环发送10条数据，模拟粘包和拆包
        // 这里模拟粘包： 这10条数据，相当于是10条数据包，但是服务器只接受了一次，这10个数据包是粘在一起被接受的（也有可能是几个粘一起，果然数据很大，也可能拆开）。
        for (int i = 0; i < 10; i++) {
            ByteBuf byteBuf = Unpooled.copiedBuffer("* hi server" + i, CharsetUtil.UTF_8);
            ctx.writeAndFlush(byteBuf);
        }
    }
}
