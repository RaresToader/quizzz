package server.api;

import commons.Activity;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import server.ActivityParser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ActivityParserTest {
    @Test
    public void constructorTest() {
        ActivityParser test = new ActivityParser(new JSONParser());
        assertNotNull(test);
    }


}
