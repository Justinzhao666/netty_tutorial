package top.zhaohaoren.netty.netty.rpc.api;

/**
 * 服务端和客户端的规约接口
 *
 * @author zhaohaoren
 */
public interface HelloServer {

    String sayHello(String msg);
}
