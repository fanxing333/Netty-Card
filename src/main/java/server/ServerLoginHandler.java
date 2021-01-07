package server;

import common.Cards;
import common.User;
import common.UserSet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;

@Slf4j
public class ServerLoginHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {

        /*
         * 只接受 登录请求
         * */
        ByteBuf in = (ByteBuf) msg;
        int len = in.readableBytes();
        byte[] arr = new byte[len];
        in.getBytes(0, arr);


        User user = new User(new String(arr, "UTF-8"), UserSet.getAllUsers()+1);

        UserSet.online(ctx.channel(), user);
        if (UserSet.getAllUsers()==4) {
            String response = "已满四人，开始游戏！";
            log.info(response);
            ctx.writeAndFlush(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));
            ctx.pipeline().remove(this);

            // 发牌
            Cards cards = new Cards();
            for (User singleUser:UserSet.users) {
                singleUser.setIndexList(cards.getUserIndex(singleUser.getUserId()));
                singleUser.setCards(cards);
                String singleResponse = "\n" + singleUser.cardsToStringGracefully();
                //log.info(singleResponse);
                if (singleUser.getCards().size()>25) {
                    UserSet.setBanker(singleUser);
                    UserSet.setSeq(singleUser.getUserId());
                    singleResponse += "\n" + "您是庄家，请先选择八张牌置底！";

                    UserSet.getChannelByUserId(singleUser.getUserId()).pipeline().addLast(new ServerDiscardHandler());
                } else {
                    //singleResponse += "\n" + UserSet.getBanker() + "    请先等待庄家弃牌！";
                }
                UserSet.getChannelByUserId(singleUser.getUserId())
                        .writeAndFlush(Unpooled.copiedBuffer(singleResponse, CharsetUtil.UTF_8));
            }
        } else {
            String response = "用户登陆成功！此时房间里已有" + UserSet.getAllUsers() + "名玩家。";
            log.info(user.getUserId() + "号" + response);
            ctx.writeAndFlush(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));
            ctx.pipeline().remove(this);
        }



    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
