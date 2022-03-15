package commons;

import java.time.LocalDate;
import java.util.Objects;

public class QuizzQuestionServerParsed {
    private QuizzQuestion question;
    private long startTime;
    private int questionNum;

    public QuizzQuestionServerParsed() {}

    public QuizzQuestionServerParsed(QuizzQuestion question, long startTime, int questionNum) {
        this.question = question;
        this.startTime = startTime;
        this.questionNum = questionNum;
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

    public void setQuestion(QuizzQuestion question) {
        this.question = question;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    @Override
    public String toString() {
        return "QuizzQuestionServerParsed{" +
                "question=" + question +
                ", startTime=" + startTime +
                ", questionNum=" + questionNum +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuizzQuestionServerParsed that = (QuizzQuestionServerParsed) o;
        return questionNum == that.questionNum && Objects.equals(question, that.question) && Objects.equals(startTime, that.startTime);
    }

}
