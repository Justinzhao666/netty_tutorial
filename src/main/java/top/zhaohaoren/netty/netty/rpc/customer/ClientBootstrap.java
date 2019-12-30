package top.zhaohaoren.netty.netty.rpc.customer;

import top.zhaohaoren.netty.netty.rpc.api.HelloServer;
import top.zhaohaoren.netty.netty.rpc.netty.NettyClient;

public class ClientBootstrap {

    private static final String providerName = "helloServer#hello#";

    public static void main(String[] args) {
        NettyClient client = new NettyClient();
        HelloServer proxy = (HelloServer) client.getBean(HelloServer.class, providerName);
        String response = proxy.sayHello("hello beijing!");
        System.out.println(response);
    }
}
