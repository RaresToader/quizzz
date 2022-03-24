package commons;

import java.util.List;
import java.util.Objects;

public class QuizzQuestionServerParsed {
    private Question question;
    private long startTime;
    private long questionNum;
    private List<Joker> jokerList;


    public QuizzQuestionServerParsed(Question question, long startTime, long questionNum,List<Joker> jokerList) {
        this.question = question;
        this.startTime = startTime;
        this.questionNum = questionNum;
        this.jokerList   = jokerList;
    }
    public QuizzQuestionServerParsed() {}

   

    public Question getQuestion() {
        return question;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getQuestionNum() {
        return questionNum;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
    public List<Joker> getJokerList() {
        return jokerList;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setQuestionNum(long questionNum) {
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
