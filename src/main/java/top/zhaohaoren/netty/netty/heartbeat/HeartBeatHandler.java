package top.zhaohaoren.netty.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 心跳空闲处理Handler
 *
 * @author zhaohaoren
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    /**
     * 空闲状态Handler触发调用
     *
     * @param ctx 上下文
     * @param evt 事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            //event 向下转型
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventStr = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventStr = "read idle";
                    break;
                case WRITER_IDLE:
                    eventStr = "write idle";
                    break;
                case ALL_IDLE:
                    eventStr = "read&write idle";
                    break;
                default:

            }
            System.out.println(ctx.channel().remoteAddress() + "空闲类型：" + eventStr);
            System.out.println("处理空闲");
            // 关闭管道，这时候只会触发一次这个方法，一般就是时间最近的那个。
            //ctx.channel().close();
        }
    }
}
