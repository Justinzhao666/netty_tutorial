package top.zhaohaoren.netty.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * 自定义Handler
 * 继承SimpleChannelInboundHandler，其父类其实就是ChannelInboundHandlerAdapter，下面获取的内容就是HttpObject不是Object了
 *
 * @author zhaohaoren
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 当有读取事件发生会调用
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {

        // 判断是不是httpRequest请求
        if (httpObject instanceof HttpRequest) {
            System.out.println("msg type:" + httpObject.getClass());
            System.out.println("client addr:"+channelHandlerContext.channel().remoteAddress());

            //给浏览器回复信息
            ByteBuf content = Unpooled.copiedBuffer("i am server", CharsetUtil.UTF_8);
            //构造httpResponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
            //写给客户端
            channelHandlerContext.writeAndFlush(response);

        }

    }
}
