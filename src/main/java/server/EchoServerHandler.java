package server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @auther shilikun
 * @Date 2019/11/04 11:27 下午
 * @Description
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf)msg;
        System.out.println(
                "Server received: "+ in.toString(CharsetUtil.UTF_8)
        );
        ctx.write(in);
        //将收到的消息发送给发送者，而不是冲刷出站消息
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
        //未决消息： 目前暂时存储于 ChannelOutboundBuffer的消息，将在调用 .flush() 和 .writeAndFlush()方法时，会尝试写出到套接字
        //将未决消息冲刷到远程节点，并且关闭该Channel 通道
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
