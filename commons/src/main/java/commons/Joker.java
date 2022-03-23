package commons;

import java.util.Objects;

public class Joker {
    private String usedBy;
    private int jokerType;
    private int questionNum;

    public Joker() {
        //Object mapping purposes
    }

    public Joker(String usedBy, int jokerType, int questionNum) {
        this.usedBy = usedBy;
        this.jokerType = jokerType;
        this.questionNum = questionNum;
    }

    public String getUsedBy() {
        return usedBy;
    }

    public void setUsedBy(String usedBy) {
        this.usedBy = usedBy;
    }

    public int getJokerType() {
        return jokerType;
    }

    public void setJokerType(int jokerType) {
        this.jokerType = jokerType;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Joker joker = (Joker) o;
        return jokerType == joker.jokerType && questionNum == joker.questionNum && Objects.equals(usedBy, joker.usedBy);
    }

}
