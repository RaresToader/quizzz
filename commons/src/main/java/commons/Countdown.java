package commons;

import java.util.*;

public class Countdown {

    int currTime = 30;

    Timer timer = new Timer();

    public void startTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                while (currTime >= 0) {
                    currTime = currTime - 1;
                }
                timer.cancel();
            }
        }, 0, 1000);
    }

    public int getCurrTime() {
        return currTime;
    }

}
