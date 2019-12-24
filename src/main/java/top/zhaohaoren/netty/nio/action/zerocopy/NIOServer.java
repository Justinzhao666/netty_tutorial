package top.zhaohaoren.netty.nio.action.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * NIO服务端
 *
 * @author zhaohaoren
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7001);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(inetSocketAddress);

        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            int read = 0;
            while (read != -1) {
                try {
                    read = socketChannel.read(byteBuffer);
                    // buffer的position=0 mark=-1。 倒带，就是重新设置这个buffer
                    byteBuffer.rewind();
                } catch (Exception e) {
                    break;
                }

            }
        }

    }
}
