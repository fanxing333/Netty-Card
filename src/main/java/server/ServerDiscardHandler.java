package server;

import common.User;
import common.UserSet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

@Slf4j
public class ServerDiscardHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {

        /*
         * 弃牌处理
         * */
        ByteBuf in = (ByteBuf) msg;
        int len = in.readableBytes();
        byte[] arr = new byte[len];
        in.getBytes(0, arr);
        log.info("server received: " + new String(arr, "UTF-8"));
        User user = UserSet.getUserByUserId((int)ctx.channel().attr(AttributeKey.valueOf("user")).get());
        if (user.getUserId() == UserSet.getSeq()) {
            String response;
            String indexList[] = new String(arr, "UTF-8").split(",");
            if (indexList.length != 8) {
                response = "请将八张牌置底！";
            } else {

                ArrayList<String> discardCards = new ArrayList<>();
                for (String index:indexList) {
                    discardCards.add(user.discardCard(Integer.parseInt(index)-1));

                }
                response = "弃掉的牌：" + discardCards.toString() + "\n" + "剩下的牌：" + "\n" + user.cardsToStringGracefully() + "\n" + "请出牌";
                ctx.pipeline().remove(this);
            }


            ctx.writeAndFlush(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));

        } else {
            String response = "不要急。";
            ctx.writeAndFlush(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
