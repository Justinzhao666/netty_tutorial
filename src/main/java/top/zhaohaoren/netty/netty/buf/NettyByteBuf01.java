package top.zhaohaoren.netty.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 使用Unpooled创建ByteBuf 案例1
 *
 * @author zhaohaoren
 */
public class NettyByteBuf01 {
    public static void main(String[] args) {

        /*
        ByteBuf 是Netty提供的Buffer。
        内部也是一个数组byte[]
        该buffer在读写的时候，不需要使用flip进行反转。因为底层维护了两个index：
        readerIndex：下一次读的位置
        writerIndex：下一次写的位置

        还可见：Netty的中组件，都是可以单独拎出来使用的。没有说那种强关联性，比如Buffer只能给对应的NettyChannel进行使用啥的。
         */


        ByteBuf byteBuf = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            //向buffer中写数据：不断变化writerIndex，可写的范围是 writerIndex~capacity范围
            byteBuf.writeByte(i);
        }

        System.out.println("cap=" + byteBuf.capacity());

        for (int i = 0; i < byteBuf.capacity(); i++) {
            //从buffer读数据：不断变化readerIndex，可读范围是 readerIndex~writerIndex范围
            System.out.println(byteBuf.readByte());
        }


    }
}
