package top.zhaohaoren.netty.netty.pack.solution;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        System.out.println("服务器传来的数据：" + new String(msg.getContent(), CharsetUtil.UTF_8));
        System.out.println("客户端第" + (++count) + "次接受服务器的消息！");
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 使用客户端 循环发送10条数据，模拟粘包和拆包
        // 这里模拟粘包： 这10条数据，相当于是10条数据包，但是服务器只接受了一次，这10个数据包是粘在一起被接受的（也有可能是几个粘一起，果然数据很大，也可能拆开）。
        for (int i = 0; i < 10; i++) {
            String msg = "落霞与孤鹜齐飞，秋水共长天一色";
            byte[] content = msg.getBytes(CharsetUtil.UTF_8);
            //内容包装协议包
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(content.length);
            messageProtocol.setContent(content);
            ctx.writeAndFlush(messageProtocol);
        }
    }


}
