package server;

import common.Cards;
import common.User;
import common.UserSet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.EventExecutorGroup;
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
        //log.info("server received: " + new String(arr, "UTF-8"));

        String str[] = new String(arr, "UTF-8").split("@");



        User user = new User(str[1], Integer.parseInt(str[0]), str[0]);

        UserSet.online(ctx.channel(), user);
        if (UserSet.getAllUsers()==4) {
            String response = "已满四人，开始游戏！";
            log.info(response);
            ctx.writeAndFlush(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));
            ctx.pipeline().remove(this);
            ctx.pipeline().addLast(new ServerDiscardHandler());
            //ctx.channel().pipeline().addLast(new ServerHandler());

            // 发牌
            Cards cards = new Cards();
            for (User singleUser:UserSet.users) {
                singleUser.setIndexList(cards.getUserIndex(singleUser.getUserId()));
                singleUser.setCards();
                String singleResponse = "\n" + singleUser.cardsToStringGracefully();
                //log.info(singleResponse);
                if (singleUser.getCards().size()>25) {
                    UserSet.setBanker(singleUser);
                    UserSet.setSeq(singleUser.getUserId());
                    singleResponse += "\n" + "您是庄家，请先选择八张牌置底！";
                } else {
                    //singleResponse += "\n" + UserSet.getBanker() + "    请先等待庄家弃牌！";
                }
                UserSet.getChannelByUserId(singleUser.getUserId())
                        .writeAndFlush(Unpooled.copiedBuffer(singleResponse, CharsetUtil.UTF_8));
            }
        } else {
            String response = "用户登陆成功！此时房间里已有" + UserSet.getAllUsers() + "名玩家。";
            log.info("用户登陆成功！此时房间里已有" + UserSet.getAllUsers() + "名玩家。" + ctx.channel().remoteAddress().toString());
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
