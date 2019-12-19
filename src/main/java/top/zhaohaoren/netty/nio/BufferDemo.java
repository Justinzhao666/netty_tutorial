package top.zhaohaoren.netty.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO中的Buffer
 * 是一个可以读，也可以写的缓冲区
 *
 * @author zhaohaoren
 */
public class BufferDemo {
    public static void main(String[] args) throws IOException {

        /*
        初始化一个buffer，有IntBuffer、ByteBuffer等等等
        此时：
            mark = -1;
            position = 0;
            limit = 5;
            capacity = 5;
       */
        IntBuffer buffer = IntBuffer.allocate(5);

        /*
        buffer中存入数据
        此时：
            mark = -1;
            position = i; //i必须小于5
            limit = 5;
            capacity = 5;

         */
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put(i);
        }

        /*
        将buffer写切换成读
        其实操作就是改变下一些标志位：
            limit = position; // 读的范围不能超过上次写的最大位置。所以limit需要调整
            position = 0; // 从头开始读
            mark = -1;
         */
        buffer.flip();

        //hasRemaining 表示position是否小于limit，即还有可读数据
        while (buffer.hasRemaining()) {
            // get() 获取当前position数据
            // get(index) 获取index位置数据
            System.out.println(buffer.get());
        }


        //只读buffer
        IntBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        mappedByteBuffer();
    }

    /**
     * MappedByteBuffer使用 extends ByteBuffer
     * 好处：可以让文件直接在内存（堆外内存）中修改，不需要操作系统拷贝文件一次。
     */
    private static void mappedByteBuffer() throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("test.txt", "rw");
        //获取通道
        FileChannel channel = randomAccessFile.getChannel();
        //(使用的读写模式，可以直接修改的开始位置，该文件映射到内存的大小范围)
        //下面修改的范围就是0-5字节
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        //改文件
        mappedByteBuffer.put(0, (byte) 'H');
        System.out.println("changed!");

        randomAccessFile.close();
    }
}
