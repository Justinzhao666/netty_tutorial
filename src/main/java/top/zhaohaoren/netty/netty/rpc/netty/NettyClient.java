package top.zhaohaoren.netty.netty.rpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 真实的客户端模拟
 *
 * @author zhaohaoren
 */
public class NettyClient {

    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static NettyClientHandler clientHandler;

    /**
     * 初始化客户端
     */
    private static void initClient() {
        clientHandler = new NettyClientHandler();

        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(clientHandler);
                    }
                });
        try {
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8888).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用代理模式获取一个代理对象
     */
    public Object getBean(final Class<?> serverClass, final String provideName) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serverClass},
                (proxy, method, args) -> {
                    if (clientHandler == null) {
                        initClient();
                    }
                    // 设置发给服务器的信息
                    clientHandler.setParams(provideName + args[0]);
                    return executorService.submit(clientHandler).get();
                });
    }
}
