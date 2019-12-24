package top.zhaohaoren.netty.nio.action.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 群聊系统客户端
 *
 * @author zhaohaoren
 */
public class GroupChatClient {

    private final String HOST = "127.0.0.1";
    private final int PORT = 8888;
    private Selector selector;
    private SocketChannel sc;
    private String username;

    public GroupChatClient() throws IOException {
        selector = Selector.open();
        //连接服务器
        sc = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        //设置非阻塞
        sc.configureBlocking(false);
        //将channel注册到selector
        sc.register(selector, SelectionKey.OP_READ);
        username = sc.getLocalAddress().toString().substring(1);
    }

    /**
     * 发送消息
     */
    private void sendMsg(String msg) {
        msg = username + " : " + msg;
        try {
            sc.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务器读取数据
     */
    private void receiveMsg() {
        try {
            int readChannels = selector.select();
            if (readChannels > 0) {
                //有channel事件
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        // 获取到可读事件
                        SocketChannel fromSc = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        fromSc.read(buffer);
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                        //删除当前的selectionKey 防止重复操作
                        iterator.remove();
                    }
                }
            } else {
                System.out.println("no channel found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        //启动客户端
        GroupChatClient client = new GroupChatClient();
        //启动一个线程不断的去读取数据 3s读一下
        new Thread(() -> {
            while (true) {
                client.receiveMsg();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //发送数据给服务器
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            client.sendMsg(line);
        }
    }
}
