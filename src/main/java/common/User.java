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

    public void setCards(Cards card) {

        ArrayList<String> cardList = card.getCards();

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
                    index = index + "  " + i + ", ";
                } else {
                    index = index + " "+ i + ", ";
                }
            } else {
                if (i<10) {
                    index = index + "  " + i + "  , ";
                } else {
                    index = index + "  " + i + " , ";
                }

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

    }
}
