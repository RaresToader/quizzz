package commons;

public abstract class Question {

    String question;

    public Question(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }
}
