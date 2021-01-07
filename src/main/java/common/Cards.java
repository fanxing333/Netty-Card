package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Cards {
    private static ArrayList<String> cards = new ArrayList<>();
    private static List<Integer> indexList;
    private static String[] cardHeads = {"♣", "♦", "♥", "♠"};
    private static String[] numbers = {"3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A", "2"};

    public Cards() {
        // 生成已排序扑克牌
        cardsInit();
        // 生成0-107的随机序列
        indexInit();

    }

    public ArrayList<String> getCard() {

        return cards;
    }

    /**
     * 用户获得手牌
     * @param userId
     * @return indexList
     * */
    public List<Integer> getUserIndex(int userId) {
        List<Integer> userIndex = indexList.subList((int) 25*(userId-1), (int) 25*userId);
        if (userIndex.contains(20)) {
            List<Integer> allSites = new ArrayList<>(userIndex);
            allSites.addAll(getHandCard());
            return allSites;
        }
        return userIndex;

    }

    // 获得底牌
    /**
     * 获得底牌
     * @param
     * @return indexList
     * */
    public List<Integer> getHandCard() {
        return indexList.subList(100, 108);
    }

    private void cardsInit() {
        for (String num:numbers) {
            for (String cardHead:cardHeads) {
                cards.add(cardHead + num);
                cards.add(cardHead + num);
            }
        }
        cards.add("joker");
        cards.add("joker");
        cards.add("Joker");
        cards.add("Joker");
    }

    private void indexInit() {
        Integer[] numList = new Integer[108];
        for (int i=0; i<numList.length; i++) {
            numList[i] = i;
        }
        indexList = Arrays.asList(numList);
        Collections.shuffle(indexList);
        indexList.toArray(numList);
    }

    public ArrayList<String> getCards() {
        return cards;
    }

    public static void main(String[] args) {

        Cards cards = new Cards();

        // 用户注册
        User user1 = new User("张三", 1);
        User user2 = new User("李四", 2);
        User user3 = new User("王五", 3);
        User user4 = new User("赵六", 4);

        //用户获得手牌
        user1.setIndexList(cards.getUserIndex(user1.getUserId()));
        user2.setIndexList(cards.getUserIndex(user2.getUserId()));
        user3.setIndexList(cards.getUserIndex(user3.getUserId()));
        user4.setIndexList(cards.getUserIndex(user4.getUserId()));

        user1.setCards(cards);
        user2.setCards(cards);
        user3.setCards(cards);
        user4.setCards(cards);

        System.out.println(user1.cardsToStringGracefully()+"牌数："+user1.getCards().size());
        System.out.println(user2.cardsToStringGracefully()+"牌数："+user2.getCards().size());
        System.out.println(user3.cardsToStringGracefully()+"牌数："+user3.getCards().size());
        System.out.println(user4.cardsToStringGracefully()+"牌数："+user4.getCards().size());

        System.out.println(cards.getHandCard());

    }

}
