package top.zhaohaoren.netty.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Netty心跳检测机制
 * 心跳和Handler中触发的断链接是不一样的。
 * 有时候客户端断开了链接服务器是感知不到的。这时候只有不断的心跳检测才能发现。（断链接触发应该是因为客户端发起了一个断开请求，服务器收到了）
 * --
 * 本例心跳其实是检测客户端在设置时间内是否有读时间和写事件发生，主要目的偏向于看一个客户端是否长时间没操作应该关闭了。
 *
 * @author zhaohaoren
 */
public class HeartBeatServer {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //netty内置的一个日志Handler
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 使用Netty提供的IdleStateHandler（空闲状态处理器）
                            //参数
                            //readerIdleTime 多长时间没有读，就发送一个心跳
                            //writerIdleTime 多长时间没有写，就发送一个心跳
                            //allIdleTime, 多次长时间没有读和写，就发送一个心跳
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                            // IdleStateHandler在触发后，会传递给pipeline的下一个handler触发userTrigger调用
                            //心跳后进一步操作
                            pipeline.addLast(new HeartBeatHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.bind(8888).sync();
            channelFuture.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
