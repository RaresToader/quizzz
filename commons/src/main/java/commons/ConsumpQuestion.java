package commons;

import java.util.Objects;

public class ConsumpQuestion extends Question {

    private Activity activity;
    long first;
    long second;
    long third;

    // Constructor for the "How much does it take" question type
    public ConsumpQuestion(String question, Activity activity, long first, long second, long third) {
        super(question);
        this.activity = activity;
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public Activity getActivity() {
        return activity;
    }

    public long getFirst() {
        return first;
    }

    public long getSecond() {
        return second;
    }

    public long getThird() {
        return third;
    }

    public String getConsump() {
        return Long.toString(activity.getConsumption_in_wh());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsumpQuestion that = (ConsumpQuestion) o;
        return first == that.first && second == that.second && third == that.third && Objects.equals(question, that.question) && Objects.equals(activity, that.activity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, activity, first, second, third);
    }

    @Override
    public String toString() {
        return "ConsumpQuestion{" +
                "question='" + question + '\'' +
                ", activity=" + activity +
                ", first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }
}
