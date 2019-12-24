package top.zhaohaoren.netty.nio.action.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * NIO 客户端
 *
 * @author zhaohaoren
 */
public class NIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 7001));
        String fileName = "test.txt";

        // 获取一个文件Channel
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
        // 准备发送
        long start = System.currentTimeMillis();
        // transferTo 底层就是使用的 零拷贝
        // 在linux下直接一次transfer就可以完成传输，windows一次只能发送8M需要分段传输。
        long count = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("发送字节数" + count + "，耗时：" + (System.currentTimeMillis() - start));
        fileChannel.close();
    }
}
