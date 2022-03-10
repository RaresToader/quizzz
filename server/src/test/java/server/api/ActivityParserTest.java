package server.api;

import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import server.ActivityParser;


import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ActivityParserTest {
    @Test
    public void constructorTest() {
        ActivityParser test = new ActivityParser(new JSONParser());
        assertNotNull(test);
    }


}
