package common;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CardBuilder {
    private static ArrayList<String> cards = new ArrayList<>();
    private static String[] cardHeads = {"♣", "♦", "♥", "♠"};
    private static String[] numbers = {"3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A", "2"};

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
    }

    // 获得初始牌序
    public ArrayList<String> getCards() {
        return cards;
    }

    public static void main(String[] args) {

    }
}
