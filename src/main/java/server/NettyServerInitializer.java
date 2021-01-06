package server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new ServerLoginHandler()); // in
        //ch.pipeline().addLast(new ServerDiscardHandler());
        //ch.pipeline().addLast(new ServerPlayCardsHandler());
        //ch.pipeline().addLast(new ServerHandler()); // in
    }
}

