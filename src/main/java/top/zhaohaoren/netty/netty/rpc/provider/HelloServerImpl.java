package top.zhaohaoren.netty.netty.rpc.provider;

import top.zhaohaoren.netty.netty.rpc.api.HelloServer;


/**
 * 这就是服务端的一个业务的代码。客户端RPC就是想调用这个方法
 *
 * @author zhaohaoren
 */
public class HelloServerImpl implements HelloServer {

    @Override
    public String sayHello(String msg) {
        System.out.println("客户端的消息：" + msg);
        if (null != msg) {
            return "server ack:" + msg;
        } else {
            return "server ack";
        }
    }
}
