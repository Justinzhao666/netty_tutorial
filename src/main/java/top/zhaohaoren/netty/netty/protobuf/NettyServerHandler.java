package top.zhaohaoren.netty.netty.protobuf;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author zhaohaoren
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<MyMessageProto.MyMessage> {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client~", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyMessageProto.MyMessage msg) throws Exception {

        MyMessageProto.MyMessage.DataType dataType = msg.getDataType();
        switch (dataType) {
            case AppleType:
                MyMessageProto.Apple apple = msg.getApple();
                System.out.println("获取苹果:" + apple.toString());
                break;
            case PearType:
                MyMessageProto.Pear pear = msg.getPear();
                System.out.println("获取梨:" + pear.toString());
                break;
            case UNRECOGNIZED:
            default:
                System.out.println("输入类型不对");
        }

    }
}
