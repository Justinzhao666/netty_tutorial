package top.zhaohaoren.netty.netty.rpc.provider;

import top.zhaohaoren.netty.netty.rpc.netty.NettyServer;

/**
 * 模拟一个server。相当于容器，用来启动服务端的服务。
 *
 * @author zhaohaoren
 */
public class ServerStarter {

    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1", 8888);
    }
}
