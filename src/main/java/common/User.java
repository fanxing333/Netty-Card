package common;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class User {
    private String username;
    private int userId;
    private int grade;
    private ArrayList<String> cards = new ArrayList<>();
    private List<Integer> indexList;


    public User(String username, int userId){
        this.username = username;
        this.userId = userId;
        this.grade = 0;
    }

    public void setCards() {

        ArrayList<String> cardList = new CardBuilder().getCards();

        Collections.sort(indexList);
        for (int i:indexList) {
            cards.add(cardList.get(i));
        }
    }

    public ArrayList<String> getCards () {
        return cards;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }

    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public void setIndexList(List<Integer> indexList) {
        this.indexList = indexList;
    }

    public List<Integer> getIndexList() {
        return indexList;
    }

    public String toString() {
        String str1 = this.cards.toString();

        return str1;
    }

    public String cardsToStringGracefully() {
        if (cards.size() == 0) {
            return "";
        }
        String index = "[";
        for (int i=1; i<=cards.size(); i++) {
            if (cards.get(i-1).length() == 2) {
                if (i<10) {
                    index = index + " " + i + ", ";
                } else {
                    index = index + i + ", ";
                }

            } else if (cards.get(i-1).length() == 3){
                if (i<10) {
                    index = index + i + ", ";
                } else {
                    index = index + " "+ i + ", ";
                }
            } else {
                index = index + "  " + i + " , ";
            }
        }
        index = index.substring(0, index.length()-2) + "]";
        index = index + "\n" + cards.toString();

        return index;
    }

    public ArrayList<String> discardCard(String[] indexStringList) {

        ArrayList<String> result = new ArrayList<>();
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (String str:indexStringList) {
            integerArrayList.add(Integer.parseInt(str)-1);
        }

        // 从大到小排序
        Collections.sort(integerArrayList, Collections.reverseOrder());

        for (int i:integerArrayList) {
            //indexList.remove(i);
            result.add(0, cards.get(i));
            cards.remove(i);
        }

        return result;
    }


    public static void main(String[] args) {
        User user1 = new User("张三", 1);
        User user2 = new User("李四", 2);
        User user3 = new User("王五", 3);
        User user4 = new User("赵六", 4);

        CardBuilder cardBuilder = new CardBuilder();
        cardBuilder.cardsDeal();

        user1.setIndexList(cardBuilder.getUserIndexList(user1.getUserId()));
        user2.setIndexList(cardBuilder.getUserIndexList(user2.getUserId()));
        user3.setIndexList(cardBuilder.getUserIndexList(user3.getUserId()));
        user4.setIndexList(cardBuilder.getUserIndexList(user4.getUserId()));

        user1.setCards();
        user2.setCards();
        user3.setCards();
        user4.setCards();


        System.out.println(user1.getCards().toString());
        System.out.println(user2.getCards().toString());
        System.out.println(user3.getCards().toString());
        System.out.println(user4.getCards().toString());


    }
}
