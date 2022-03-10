package commons;

public class Points {

    private int addPoints = 0;
    private Countdown countdown = new Countdown();

    /**
     * The player receives points based on how much time is left,
     * scaling equally between 1000 for 30 seconds left and 500 for 0 seconds left.
     */
    public int getPoints(boolean answered) {
        if (answered) {
            int getTime = countdown.getCurrTime();
            addPoints = 500 + (500 / 30 * getTime);
        }
        return addPoints;
    }
}
