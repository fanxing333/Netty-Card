package common;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class UserSet {
    public static ArrayList<User> users = new ArrayList<>();

    public static int getAllUsers() {
        return channelMap.size();
    }

    public static ConcurrentHashMap<User, Channel> channelMap = new ConcurrentHashMap<>();

    public static void online(Channel channel, User user) {

        users.add(user);
        channelMap.put(user, channel);
        AttributeKey<Integer> key = AttributeKey.valueOf("user");
        channel.attr(key).set(user.getUserId());
    }

    public static Channel getChannelByUserId(int userId) {
        if (getUserByUserId(userId)!=null) {
            return channelMap.get(getUserByUserId(userId));
        }
        return null;
    }

    public static User getUserByUserId(int userId) {
        for (User user:users) {
            if (user.getUserId()==userId) {
                return user;
            }
        }

        return null;
    }

    private static User banker;

    public static void setBanker(User user) {
        banker = user;
    }
    public static String getBanker() {
        return "本轮游戏庄家为" + banker.getUserId() + "号玩家：" + banker.getUsername();
    }

    public static int Seq=0;

    public static int getSeq() {
        return Seq;
    }

    public static void setSeq(int seq) {
        Seq = seq;
    }

}
