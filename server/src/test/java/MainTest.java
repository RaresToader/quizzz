import org.junit.jupiter.api.Test;
import server.Main;

import static org.junit.jupiter.api.Assertions.*;
public class MainTest {
    @Test
    public void constructorTest() {
        Main x = new Main();
        assertNotNull(x);
    }

    @Test
    public void mainMethodTest() {
        Main.main(new String[] {});
    }
}
