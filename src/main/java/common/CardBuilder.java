package common;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CardBuilder {
    private static ArrayList<String> cards = new ArrayList<>();
    private static List<Integer> indexList;
    private static String[] cardHeads = {"♣", "♦", "♥", "♠"};
    private static String[] numbers = {"3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A", "2"};
    //private static String[] jokers = {"Joker", "joker"};

    private static List<Integer> index1;
    private static List<Integer> index2;
    private static List<Integer> index3;
    private static List<Integer> index4;
    private static List<Integer> indexHand;


    // 初始化
    public CardBuilder() {
        // cards是固定的
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

        // 生成0-107的随机序列
        indexInit();

    }

    // 获得初始牌序
    public ArrayList<String> getCards() {
        return cards;
    }

    // 获得indexList列表
    public List<Integer> getIndexList() {
        return indexList;
    }

    public List<Integer> getUserIndexList(int userId) {
        if (userId == 1){
            return this.index1;
        } else if (userId == 2) {
            return this.index2;
        } else if (userId == 3) {
            return this.index3;
        } else if (userId == 4) {
            return this.index4;
        } else {
            return this.indexHand;
        }
    }

    // 发牌
    public void cardsDeal() {
        index1 = indexList.subList(0, 25);
        index2 = indexList.subList(25, 50);
        index3 = indexList.subList(50, 75);
        index4 = indexList.subList(75, 100);
        indexHand = indexList.subList(100, 108);
    }

    // 获得与index对应的排序
    public ArrayList<String> cardsBuild(List<Integer> indexList) {
        ArrayList<String> finalCards = new ArrayList<>();
        for (int i:indexList) {
            finalCards.add(cards.get(i));
        }

        return finalCards;
    }

    private void indexInit(){
        Integer[] numList = new Integer[108];
        for (int i=0; i<numList.length; i++) {
            numList[i] = i;
        }
        indexList = Arrays.asList(numList);
        Collections.shuffle(indexList);
        indexList.toArray(numList);
    }

    public String toString() {
        String str1 = "牌数：" + cards.size();
        String str2 = "初始扑克牌：    " + cards.toString();
        String str3 = "打乱后的扑克牌：" + cardsBuild(indexList).toString();

        if (indexHand != null) {
            String str4 = "1号玩家手牌：" + cardsBuild(index1).toString();
            String str5 = "2号玩家手牌：" + cardsBuild(index2).toString();
            String str6 = "3号玩家手牌：" + cardsBuild(index3).toString();
            String str7 = "4号玩家手牌：" + cardsBuild(index4).toString();
            String str8 = "剩余8张底牌：" + cardsBuild(indexHand).toString();

            return str1+ "\n" + str2 + "\n" + str3 + "\n" + str4 + "\n" + str5 + "\n" + str6 + "\n" + str7 + "\n" + str8;

        }

        return str1+ "\n" + str2 + "\n" + str3;
    }


    public static void main(String[] args) {
        System.out.println((1+1)%4);
        System.out.println(3%4);
        System.out.println(2%4);
        System.out.println(1%4);

    }
}
