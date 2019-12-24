package top.zhaohaoren.netty.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 自定的childChannel类
 *
 * @author zhaohaoren
 */
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 向对应的channel的管道中加入handler
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        //这里示例：加入一个netty自己内置的handler：用于处理http的编-解码器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        //这里示例：加入一个自己定义的handler。HttpServerHandler是给该handler起的名字
        pipeline.addLast("HttpServerHandler", new HttpServerHandler());
    }
}
