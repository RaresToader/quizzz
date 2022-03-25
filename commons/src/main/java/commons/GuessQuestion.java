package commons;

import java.util.Objects;

public class GuessQuestion extends Question {
    private String question;
    private Activity activity;

    public GuessQuestion(String question, Activity activity) {
        super();
        this.question = question;
        this.activity = activity;
    }

    public String getQuestion() {
        return question;
    }

    public Activity getActivity() {
        return activity;
    }

    public String getCorrectGuess() {
        return Long.toString(activity.getConsumption_in_wh());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GuessQuestion that = (GuessQuestion) o;
        return Objects.equals(activity, that.activity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), activity);
    }

    @Override
    public String toString() {
        return "GuessQuestion{" +
                "question='" + question + '\'' +
                "activity=" + activity +
                '}';
    }
}
