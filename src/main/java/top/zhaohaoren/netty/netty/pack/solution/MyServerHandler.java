package top.zhaohaoren.netty.netty.pack.solution;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        System.out.println("服务器端接受到数据：" + new String(msg.getContent(), CharsetUtil.UTF_8));
        System.out.println("服务器第" + (++count) + "次接受客户端的消息！");

        // 给客户端回复消息
        String resp = UUID.randomUUID().toString();
        byte[] bytes = resp.getBytes(CharsetUtil.UTF_8);
        MessageProtocol message = new MessageProtocol();
        message.setLen(bytes.length);
        message.setContent(bytes);

        ctx.writeAndFlush(message);
    }
}
