package top.zhaohaoren.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zhaohaoren
 */
public class NioServer {
    public static void main(String[] args) throws IOException {
        /*创建服务器的管道*/

        // 首先要创建一个socketChannel和BIO的SocketServer一样
        ServerSocketChannel ssc = ServerSocketChannel.open();

        // 绑定端口
        ssc.socket().bind(new InetSocketAddress(6666));
        // 设置该管道为非阻塞
        ssc.configureBlocking(false);

        /*创建Selector：其实selector就是一个对象*/

        Selector selector = Selector.open();

        /*注册到selector：所有注册完成后会返回得到一个selectKey*/

        // 注册的时候指定Key的类别，下面这个是ACCEPT表示这个管道是用来接收连接的
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        /*等待客户端连接*/

        while (true) {
            // 服务器阻塞1s判断是否有连接
            if (selector.select(1000) == 0) {
                System.out.println("server wait 1s, no connection...");
            } else {
                // 有连接发生(也有可能是其他的事件)，就会将selectionKey加入到Selector的内部集合中去。
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();

                    // 处理那些连接的channel
                    if (selectionKey.isAcceptable()) {
                        // 通过ServerSocketChannel获取到一个SocketChannel用于客户端的数据交互
                        SocketChannel sc = ssc.accept();
                        // 必须也要设置为非阻塞
                        sc.configureBlocking(false);
                        System.out.println("client connected:" + sc.hashCode());
                        // 也要将该channel注册到该selector上去，方式为READ，并分配一个buffer
                        sc.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    }
                    // 处理那些读的操作
                    if (selectionKey.isReadable()) {
                        // 通过selectKey还可以反向获取到是哪一个channel
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        // 获取大概channel关联的那个buffer
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                        // 将channel的数据写入到buffer中去
                        channel.read(buffer);
                        System.out.println("from client: " + new String(buffer.array()));
                    }
                    // 删除遍历完后的selectionKey，防止重复操作。
                    iterator.remove();
                }

            }
        }


    }
}
