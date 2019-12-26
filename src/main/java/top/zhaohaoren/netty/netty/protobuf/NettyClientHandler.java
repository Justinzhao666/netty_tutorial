package top.zhaohaoren.netty.netty.protobuf;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Random;

/**
 * @author zhaohaoren
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 随机发送Apple或者Pear对象
        int i = new Random().nextInt(3);
        MyMessageProto.MyMessage myMessage;
        if (i==0){
            myMessage = MyMessageProto.MyMessage.newBuilder().setDataType(MyMessageProto.MyMessage.DataType.AppleType)
                    .setApple(MyMessageProto.Apple.newBuilder().setId(1).setName("红苹果").build())
                    .build();
        }else {
            myMessage = MyMessageProto.MyMessage.newBuilder().setDataType(MyMessageProto.MyMessage.DataType.PearType)
                    .setPear(MyMessageProto.Pear.newBuilder().setId(1).setName("黄梨").build())
                    .build();
        }
        ctx.writeAndFlush(myMessage);
    }

    /**
     * 通道有读取事件的时候就触发
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("server reply:" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("server address:" + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
