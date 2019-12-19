package top.zhaohaoren.netty.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO中的Channel
 * 以FileChannel为例
 *
 * @author zhaohaoren
 */
public class ChannelDemo {
    public static void main(String[] args) throws IOException {
        writeFile();
        readFile();
    }

    /**
     * 使用channel写入文件
     * 逻辑：字符串->写入buffer->写入channel->文件
     * 1. 创建一个关联的文件的文件流
     * 2. 通过流创建一个channel（流中内置了一个channel），那么这个channel就和该文件对应起来了
     * 3. 创建一个buffer，将数据写入buffer中
     * 4. 翻转buffer来读，将数据读入到channel中
     */
    private static void writeFile() throws IOException {
        //创建一个文件流
        FileOutputStream fos = new FileOutputStream("test.txt");

        //创建一个channel: Fos里面就包装了一个channel，不过是懒加载的只有get的时候会创建。
        //FileChannel实际类对象是FileChannelImpl
        FileChannel channel = fos.getChannel();

        //创建一个buffer，然后将数据先写入到buffer中
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("hello beijing!".getBytes());

        //然后切换到读，准备读buffer数据
        buffer.flip();

        //将buffer数据写入到FileChannel
        channel.write(buffer);
        channel.close();
    }

    /**
     * 使用channel读取文件
     * 逻辑：文件->写入channel->写入buffer->读取buffer转为string显示
     * 1. 创建一个关联的文件的文件流
     * 2. 通过流创建一个channel（流中内置了一个channel），那么这个channel就和该文件对应起来了
     * 3. 创建一个buffer，将数据写入buffer中
     * 4. 翻转buffer来读，将数据读入到channel中
     */
    private static void readFile() throws IOException {
        File file = new File("test.txt");
        FileInputStream fis = new FileInputStream(file);
        //文件输入流创建channel
        FileChannel channel = fis.getChannel();

        //创建buffer将channel数据写入到buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        channel.read(byteBuffer);

        System.out.println("read: " + new String(byteBuffer.array()));
        //不要忘记关闭流
        fis.close();

    }

    /**
     * 使用channel复制文件
     * 逻辑1：文件->写入读流的channel->写入buffer->读取buffer->写入写流的channel
     * 逻辑2：文件->写入读流的channel->复制到写入写流的channel（transferFrom）
     */
    private static void copyFile() throws IOException {
        writeFile();
        FileInputStream fis = new FileInputStream("test.txt");
        FileChannel channel = fis.getChannel();
        FileOutputStream fos = new FileOutputStream("test2.txt");
        FileChannel channelOut = fos.getChannel();

        /* 使用文件复制拷贝方式 */

        //文件不知道有多大，就一块一块的读
        ByteBuffer buffer = ByteBuffer.allocate(512);
        while (true) {
            //先清空buffer，将标记恢复到初始状态。这一步很必要！
            //注意：buffer的标记在写入的时候不是环形写的，也就是写满了之后就要手动做一次复位
            buffer.clear();
            //从buffer中读数据
            int read = channel.read(buffer);
            if (read == -1) {
                break;
            }
            //读一段就存入一段
            buffer.flip();
            channelOut.write(buffer);
        }

        /* 使用transferForm进行拷贝，一步完成：将一个channel复制到一个新的channel
           channelOut.transferFrom(channel, 0, channel.size());
        */

        //关闭流
        fos.close();
        fis.close();
    }
}
