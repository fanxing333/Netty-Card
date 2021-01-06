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
            String response;

            if (new String(arr, "UTF-8").equals("pass")){
                UserSet.setSeq(user.getUserId()+1!=5?user.getUserId()+1:1);
                response = user.getUserId() + "号玩家放弃出牌。" + "\n"
                            + "现在轮到" + UserSet.getSeq() + "号玩家出牌";

                ctx.writeAndFlush(Unpooled.copiedBuffer("现在轮到" + UserSet.getSeq() + "号玩家出牌", CharsetUtil.UTF_8));

                for (User user1: UserSet.users) {
                    if (user1 != user) {
                        UserSet.getChannelByUserId(user1.getUserId())
                                .writeAndFlush(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));
                    }
                }

            } else {
                String[] indexList = new String(arr, "UTF-8").split(",");


                ArrayList<String> discardCards = user.discardCard(indexList);
                UserSet.setSeq(user.getUserId()+1!=5?user.getUserId()+1:1);

                response = "打出的牌：" + discardCards.toString() + "\n"
                        + "剩下的牌：" + "\n"
                        + user.cardsToStringGracefully() + "\n"
                        + "轮到" + UserSet.getSeq() + "号玩家出牌";
                ctx.writeAndFlush(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));

                response = user.getUserId() + "号玩家出牌：" + discardCards.toString() + "\n"
                        + "现在轮到" + UserSet.getSeq() + "号玩家出牌";
                for (User user1: UserSet.users) {
                    if (user1 != user) {
                        UserSet.getChannelByUserId(user1.getUserId())
                                .writeAndFlush(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));
                    }
                }
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
