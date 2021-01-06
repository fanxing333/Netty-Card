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
public class ServerPlayCardsHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {

        /*
         * 只接受 登录请求
         * */
        ByteBuf in = (ByteBuf) msg;
        int len = in.readableBytes();
        byte[] arr = new byte[len];
        in.getBytes(0, arr);
        log.info(ctx.channel().remoteAddress().toString());

        User user = UserSet.getUserByUserId((int)ctx.channel().attr(AttributeKey.valueOf("user")).get());
        if (user.getUserId() == UserSet.getSeq()) {
            // 判断出牌是否符合规则

            // 出牌
            ArrayList<String> discardCards = new ArrayList<>();
            String indexList[] = new String(arr, "UTF-8").split(",");
            for (String index:indexList) {
                discardCards.add(user.discardCard(Integer.parseInt(index)-1));

            }
            String response = user.getUserId() + "号玩家出牌：" + discardCards;
            for (User singleUser:UserSet.users) {
                UserSet.getChannelByUserId(singleUser.getUserId())
                        .writeAndFlush(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));
            }
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
