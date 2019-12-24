package top.zhaohaoren.netty.nio.action.zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 传统IO的客户端
 *
 * @author zhaohaoren
 */
public class IOClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 7000);
        String file = "test.txt";
        FileInputStream fileInputStream = new FileInputStream(file);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] buffer = new byte[4096];
        long read;
        long total = 0;
        long startTime = System.currentTimeMillis();

        while ((read = fileInputStream.read(buffer)) > 0) {
            total += read;
            dataOutputStream.write(buffer);
        }
        System.out.println("发送字节数" + total + "，耗时：" + (System.currentTimeMillis() - startTime));
        dataOutputStream.close();
        socket.close();
        fileInputStream.close();
    }
}
