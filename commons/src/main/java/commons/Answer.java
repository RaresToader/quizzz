package commons;

import java.util.Objects;

public class Answer {
    private String nickname;
    private int answer;
    private int questionNum;

    public Answer() {
    }

    public Answer(String nickname, int answer, int questionNum) {
        this.nickname = nickname;
        this.answer = answer;
        this.questionNum = questionNum;
    }


    public String getNickname() {
        return nickname;
    }

    public int getAnswer() {
        return answer;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "nickname='" + nickname + '\'' +
                ", answer=" + answer +
                ", questionNum=" + questionNum +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer1 = (Answer) o;
        return answer == answer1.answer && questionNum == answer1.questionNum && Objects.equals(nickname, answer1.nickname);
    }
}
