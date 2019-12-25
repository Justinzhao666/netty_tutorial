package top.zhaohaoren.netty.netty.groupchat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

// TODO: 2019-12-25 这些流程到底是 一步一步怎么转的，数据是怎么流的？客户端和服务端是怎么对上的？ 有点晕乎了。。。。客户端为啥和服务端都有handler

/**
 * 客户端的Handler
 * 客户端的handler是处理 服务器传来的数据。
 *
 * @author zhaohaoren
 */
public class GroupChatClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //客户端就直接打印服务器的消息就行了。
        System.out.println(msg.trim());
    }
}
