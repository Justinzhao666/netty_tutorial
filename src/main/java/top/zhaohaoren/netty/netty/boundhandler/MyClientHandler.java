package top.zhaohaoren.netty.netty.boundhandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("服务器回显数据:" + msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client开始发送数据");
        //调用编码器
        ctx.writeAndFlush(666666L);
        //这个不会调用编码器 因为编码器会去判断是不是自己处理的类型，只有是才会执行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("aassddwwCCssddww", CharsetUtil.UTF_8));
    }
}
