package top.zhaohaoren.netty.netty.simple;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 自定义pipeline中的handler
 * 需要继承Netty定好的某个HandlerAdapter，然后覆盖其中的方法，里面每个方法其实都对应的一种操作。
 *
 * @author zhaohaoren
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 这就是读取数据的操作（这里就是读取客户端的数据）
     *
     * @param ctx 上下文对象
     * @param msg 客户端的数据，封装为了Object对象了
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //如果这里针对读有一个很耗时的业务，那么最好不要阻塞他，而是提交给该channel对应的eventLoop的taskQueue中执行。
        //解决方案1：自定义普通任务（可以添加多个）
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 10);
                    //写回给客户端
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello-o client1", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        //解决方案2：自定义定时任务，该任务将被提交到scheduleTaskQueue
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 5);
                    //写回给客户端
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello-o client2", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }, 5, TimeUnit.SECONDS);


        //验证这里是否会被阻塞
        System.out.println("go on");

        /*

        System.out.println("server read thread:" + Thread.currentThread().getName());
        //DEBUG HERE...
        System.out.println("server ctx=" + ctx);
        //Netty提供的ByteBuf，不是NIO的，Netty有优化，更好的Buffer
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("client msg:" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("client address:" + ctx.channel().remoteAddress());

        */
    }

    /**
     * 数据读取完成后的操作
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // write是写入缓冲区 flush是刷写到管道上去。必须要flush
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client~", CharsetUtil.UTF_8));
    }

    /**
     * 处理异常情况，一般就是关闭管道。
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
