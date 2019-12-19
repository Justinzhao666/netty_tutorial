package top.zhaohaoren.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO服务案例
 * 1. 服务器启动ServerSocket，然后accept阻塞等待连接
 * 2. 创建一个线程池，当有客户端连接的时候就将该socket交给一个线程去处理
 * 3. 客户端关闭后，服务器线程read管道失败，会返回-1，关闭连接
 *
 * @author zhaohaoren
 */
public class BioServer {
    public static void main(String[] args) throws IOException {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("server has started...");

        while (true) {
            //阻塞：如果没有连接，就一致阻塞在这里等待连接
            Socket socket = serverSocket.accept();
            System.out.println("client connect...");
            cachedThreadPool.execute(() -> {
                try {
                    System.out.println("thread id: " + Thread.currentThread().getId());
                    System.out.println("thread name: " + Thread.currentThread().getName());
                    InputStream inputStream = socket.getInputStream();
                    byte[] bytes = new byte[1024];
                    while (true) {
                        //阻塞：如果没有数据该线程就一直阻塞在这里等待数据
                        int read = inputStream.read(bytes);
                        if (read != -1) {
                            // 客户端关闭
                            System.out.println(Thread.currentThread().getId() + " ==> " + new String(bytes, 0, read));
                        } else {
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("close connection...");
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
