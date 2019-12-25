package top.zhaohaoren.netty.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;

/**
 * 使用Unpooled创建ByteBuf 案例2
 *
 * @author zhaohaoren
 */
public class NettyByteBuf02 {
    public static void main(String[] args) {
        //创建buffer: 该方式创建（需要创建buffer的内容，该内容使用什么编码）按照内容来创建buffer
        ByteBuf byteBuf = Unpooled.copiedBuffer("content", CharsetUtil.UTF_8);

        if (byteBuf.hasArray()) {
            //可以直接获取到里面的数组
            byte[] array = byteBuf.array();
            //转为字符串
            System.out.println(new String(array, CharsetUtil.UTF_8));
            //查看该buffer实际类型：UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeHeapByteBuf
            System.out.println("byteBuf=" + byteBuf);
            //数组的偏移位置
            System.out.println(byteBuf.arrayOffset());
            //三个位置属性
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex());
            System.out.println(byteBuf.capacity());

            //获取可读的字节数
            System.out.println(byteBuf.readableBytes());
            //读取部分，指定范围
            System.out.println(byteBuf.getCharSequence(0, 4, CharsetUtil.UTF_8));
        }
    }
}
