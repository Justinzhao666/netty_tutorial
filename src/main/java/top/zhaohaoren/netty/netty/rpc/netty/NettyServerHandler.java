package top.zhaohaoren.netty.netty.rpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.zhaohaoren.netty.netty.rpc.provider.HelloServerImpl;

/**
 * 服务器的handler
 *
 * @author zhaohaoren
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 获取客户端发送的消息，并调用服务端的服务。
        System.out.println("msg=" + msg);
        // 客户端调用服务端的api的时候，必须要遵从一个协议
        if (msg.startsWith("helloServer#hello#")) {
            // 请求的序列化是这个方法开头的，就创建这个服务，然后将参数传给这个服务。完成调用。返回写回结果。
            String response = new HelloServerImpl().sayHello(msg.substring(msg.lastIndexOf("#") + 1));
            ctx.writeAndFlush(response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
