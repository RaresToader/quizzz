package commons;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class PointsTest {
    @Test
    public void emptyConstructorTest() {
        Points p = new Points();
        assertNotNull(p);
    }

    @Test
    public void getPointsTestNotAnswered() {
        Points p = new Points();
        assertEquals(0,p.getPoints(false));
    }

    @Test
    public void getPointsTestAnswered() {
        Points p = new Points();
        assertEquals(333,p.getPoints(true));
    }
}
