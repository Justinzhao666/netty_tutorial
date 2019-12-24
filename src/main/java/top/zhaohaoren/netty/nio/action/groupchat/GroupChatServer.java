package top.zhaohaoren.netty.nio.action.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * 群聊系统服务端
 *
 * @author zhaohaoren
 */
public class GroupChatServer {


    private Selector selector;
    private ServerSocketChannel ssc;
    private static final int PORT = 8888;


    /**
     * 初始化操作
     */
    public GroupChatServer() {
        try {
            //创建选择器
            selector = Selector.open();
            //创建channel
            ssc = ServerSocketChannel.open();
            //绑定端口
            ssc.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞
            ssc.configureBlocking(false);
            //注册channel到selector
            ssc.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 监听客户端的连接
     */
    public void listen() {
        try {
            while (true) {
                int cnt = selector.select(1000);
                if (cnt > 0) {
                    // 获取到事件发生，迭代时间并处理
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()) {
                            // 客户端连接，为其创建socketChannel并注册到selector上。
                            SocketChannel sc = ssc.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                            System.out.println(sc.getRemoteAddress() + " is online!");
                        }
                        if (key.isReadable()) {
                            // 读事件 要转发给其他的所有的客户端，也就是客户端对应的channel
                            SocketChannel channel = (SocketChannel) key.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            int count = 0;
                            try {
                                count = channel.read(byteBuffer);
                            } catch (IOException ioe) {
                                //如果读取异常，说明客户端关闭了，需要关闭相关资源。
                                System.out.println(channel.getRemoteAddress() + " is offline!");
                                //取消注册
                                key.cancel();
                                //关闭channel
                                channel.close();
                            }
                            if (count > 0) {
                                //获取到数据了
                                String msg = new String(byteBuffer.array());
                                System.out.println("from client msg:" + msg);
                                //转发给其他的客户端 keys获取所有注册到该selector的channel
                                for (SelectionKey selectionKey : selector.keys()) {
                                    //这里一定要使用Channel接受，是基类，因为keys里面不一定都是SocketChannel
                                    Channel targetChannel = selectionKey.channel();
                                    System.out.println("start to broadcast to targets");
                                    //不要转发给自己
                                    if (targetChannel instanceof SocketChannel && targetChannel != channel) {
                                        ((SocketChannel) targetChannel).write(ByteBuffer.wrap(msg.getBytes()));
                                    }
                                }
                            }
                        }
                        iterator.remove();
                    }
                } else {
                    System.out.println("keep waiting...");
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        GroupChatServer server = new GroupChatServer();
        server.listen();
    }
}
