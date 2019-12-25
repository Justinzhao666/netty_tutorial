package top.zhaohaoren.netty.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 自定义handler
 * 服务器的handler是处理 客户端传来的数据。
 *
 * @author zhaohaoren
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 因为要群发，所以要知道其他所有客户端连接的channel
     * GlobalEventExecutor.INSTANCE是一个全局的事件执行器，是个单例
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 当连接建立的时候，第一个被执行的方法。
     * 这里就是将新连接加入到group中
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //group会循环发送给内部所有的channel
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "加入群聊\n");
        channelGroup.add(channel);
    }

    /**
     * 表示channel处于一个活动的状态
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线");
    }

    /**
     * channel处于非活动状态触发
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线");
    }

    /**
     * 断开连接
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 通知所有人，该客户端离线。离线group也会自动删除。
        channelGroup.writeAndFlush("[客户端]" + ctx.channel().remoteAddress() + "离开了\n");
        System.out.println("剩余在线人数" + channelGroup.size());
    }

    /**
     * 读取数据
     * 处理转发数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if (ch != channel) {
                //发给别人，直接转发
                ch.writeAndFlush("[客户端]" + channel.remoteAddress() + " 发送：" + msg + "\n");
            } else {
                //自己的
                ch.writeAndFlush("[我] 发送：" + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
