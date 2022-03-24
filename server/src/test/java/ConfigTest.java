import org.junit.jupiter.api.Test;
import server.Config;


import static org.junit.jupiter.api.Assertions.*;
public class ConfigTest {
    @Test
    public void constructorTest() {
        Config x = new Config();
        assertNotNull(x);
    }

    @Test
    public void rndMethodTest() {
        Config cfg = new Config();
        assertNotNull(cfg.getRandom());
    }
}
