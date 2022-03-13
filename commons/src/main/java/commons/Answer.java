package commons;

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
}
