package commons;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class CountdownTest {
    @Test
    public void emptyConstructorTest() {
        Countdown x = new Countdown();
        assertNotNull(x);
    }

    @Test
    public void getCurrTimeTest() {
        Countdown x = new Countdown();
        assertEquals(20,x.getCurrTime());
    }

    @Test
    public void startTimerTest() throws InterruptedException {
        Countdown x = new Countdown();
        x.startTimer();
        Thread.sleep(1000);
        assertEquals(-1,x.getCurrTime());
    }

}
