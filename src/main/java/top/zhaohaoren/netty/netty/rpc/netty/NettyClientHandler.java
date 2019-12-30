package top.zhaohaoren.netty.netty.rpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.Callable;

/**
 * 客户端的handler *重点
 *
 * @author zhaohaoren
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<String> implements Callable {

    private ChannelHandlerContext context;
    /**
     * 返回结果
     */
    private String result;
    /**
     * 请求参数
     */
    private String params;


    public void setParams(String params) {
        this.params = params;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    @Override
    protected synchronized void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //接受到服务器的数据后，调用该方法
        result = msg;
        //唤起等待的线程。 以为call的请求会等待
        notify();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public synchronized Object call() throws Exception {
        //代理对象调用它，发送数据给服务端，然后进入wait，等待被唤醒（结果返回的时候唤醒 notify）。
        context.writeAndFlush(params);
        // 进行wait
        wait();
        // wait醒了说明数据来了，就返回res-服务器的结果。
        return result;
    }
}
