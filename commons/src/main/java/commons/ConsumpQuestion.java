package commons;

public class ConsumpQuestion extends Question {

    private String question;
    private Activity activity;
    long first;
    long second;
    long third;

    public ConsumpQuestion(String question, Activity activity, long first, long second, long third) {
        super(question);
        this.activity = activity;
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public String getQuestion() {
        return question;
    }

    public Activity getFirstChoice() {
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
}
