package top.zhaohaoren.netty.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Netty 服务端
 *
 * @author zhaohaoren
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        /*
        1.创建两个线程组，两个都是无限循环的。
        默认这两个group含有的子线程个数为cpu核数量*2
        client的连接给boss，boss会分配一个EventLoop线程给该client。分配方式为轮询方式。
        可以在这里debug进去：group下面的child是一个个EventLoop，每个EventLoop就是一个线程里面有一个selector，也在不停的循环。
        循环等待事件发生，然后会触发对应的handler进行处理。
        */
        //boss只负责处理连接请求，这里设置boss只有一个线程group
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //work负责和客户端业务交互处理
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            //2.创建服务器启动对象
            ServerBootstrap bootstrap = new ServerBootstrap();
            //设置
            bootstrap
                    //设置2个线程组
                    .group(bossGroup, workGroup)
                    //使用NioServerSocketChannel做服务器的通道
                    .channel(NioServerSocketChannel.class)
                    //设置线程队列等待连接的个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //给我们WorkGroup的eventLoop对应的pipeline设置处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //创建一个通道测试对象（匿名对象）
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            System.out.println("server is already ready");
            //绑定一个端口，并同步。（同时也是启动服务器）
            ChannelFuture channelFuture = bootstrap.bind(8886).sync();
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            //优雅的关闭
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
}
