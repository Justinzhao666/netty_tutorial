package top.zhaohaoren.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * NIO 的客户端
 * @author zhaohaoren
 */
public class NioClient {
    public static void main(String[] args) throws IOException {
        // 简历一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        // 将通道设置为非阻塞
        socketChannel.configureBlocking(false);
        // 设置连接的服务器和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);

        // 连接服务器
        if (!socketChannel.connect(inetSocketAddress)){
            // 如果客户端没有连接上，循环判断是否连接
            while (!socketChannel.finishConnect()){
                // 这里不会被阻塞，客户端可以自己做自己的逻辑
                System.out.println("client is connecting...");
            }
        }

        // 客户端已经连接上了
        String str = "hello beijing!";
        // 准备buffer
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        // 将buffer数据写入channel
        socketChannel.write(buffer);

        // 让客户端停在这里为了测试不退出。
        System.in.read();
    }
}
