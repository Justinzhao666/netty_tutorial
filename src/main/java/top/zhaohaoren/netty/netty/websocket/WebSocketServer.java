package top.zhaohaoren.netty.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author zhaohaoren
 */
public class WebSocketServer {
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
                            //因为是基于Http协议所以加上Http编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            //因为是基于http所以是基于块方式写的。todo 没明白啥意思
                            pipeline.addLast(new ChunkedWriteHandler());
                            /*
                            http数据传输过程中是分段的，HttpObjectAggregator可将多个段聚合在一起
                            当浏览器发送大量数据时候，就会发出多次http请求，就是因为http是分段的
                            todo 还是没懂
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /*
                            websocket的数据是以帧形式传递的
                            WebSocketFrame有6个子类，分别对应不同类型的数据的websocket帧数据处理。
                            浏览器请求方式："ws://localhost:8888/xxxx"
                            * WebSocketServerProtocolHandler核心功能是将http协议升级为ws协议（websocket协议）保持长连接
                            是通过状态码101来切换的，只有浏览器请求匹配到这里配置的uri的时候会返回一个101状态码，来切换协议
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            /*
                            自定义handler来处理业务逻辑
                             */
                            pipeline.addLast(new TextWebSocketFrameHandler());
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
