package commons;

import java.util.List;
import java.util.Objects;

public class QuizzQuestionServerParsed {
    private QuizzQuestion question;
    private long startTime;
    private int questionNum;
    private List<Joker> jokerList;

    public QuizzQuestionServerParsed() {}

    public QuizzQuestionServerParsed(QuizzQuestion question, long startTime, int questionNum, List<Joker> jokerList) {
        this.question    = question;
        this.startTime   = startTime;
        this.questionNum = questionNum;
        this.jokerList   = jokerList;
    }

    public QuizzQuestion getQuestion() {
        return question;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public List<Joker> getJokerList() {
        return jokerList;
    }

    public void setQuestion(QuizzQuestion question) {
        this.question = question;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public void setJokerList(List<Joker> jokerList) {
        this.jokerList = jokerList;
    }


    @Override
    public String toString() {
        return "QuizzQuestionServerParsed{" +
                "question=" + question +
                ", startTime=" + startTime +
                ", questionNum=" + questionNum +
                ", jokerList=" + jokerList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuizzQuestionServerParsed that = (QuizzQuestionServerParsed) o;
        return startTime == that.startTime && questionNum == that.questionNum && Objects.equals(question, that.question) && Objects.equals(jokerList, that.jokerList);
    }
}
