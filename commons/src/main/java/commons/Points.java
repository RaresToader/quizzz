package commons;

public class Points {

    int currPoints = 0;
    Countdown countdown = new Countdown();

    public int getPoints(boolean answered) {
        if (answered) {
            int getTime = countdown.getCurrTime();
            currPoints = 500 + (500 / 30 * getTime);
        }
        return currPoints;
    }
}
