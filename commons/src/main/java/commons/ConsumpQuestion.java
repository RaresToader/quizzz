package commons;

import java.util.Objects;

public class ConsumpQuestion extends Question {

    private String question;
    private Activity activity;
    private long first;
    private long second;
    private long third;

    public ConsumpQuestion(String question, Activity activity, long first, long second, long third) {
        super();
        this.question = question;
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
        if (!(o instanceof ConsumpQuestion)) return false;
        ConsumpQuestion that = (ConsumpQuestion) o;
        return getFirst() == that.getFirst() && getSecond() == that.getSecond() && getThird() == that.getThird() && Objects.equals(getQuestion(), that.getQuestion()) && Objects.equals(getActivity(), that.getActivity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuestion(), getActivity(), getFirst(), getSecond(), getThird());
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
