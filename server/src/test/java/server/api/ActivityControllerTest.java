package server.api;


import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

public class ActivityControllerTest {

    private TestActivityRepository repo;
    private ActivityController systemUnderTest;

    @BeforeEach
    public void setup() {
        repo = new TestActivityRepository();
        systemUnderTest = new ActivityController(repo);
    }

    @Test
    public void constructorTest() {
        assertNotNull(systemUnderTest);
    }

    @Test
    public void addActivity() {
        assertNotEquals(systemUnderTest.addActivity(new Activity("test", "test","10", 10L       ,"test")).getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void cannotAddNullActivity() {
        var actual = systemUnderTest.addActivity(null);
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void cannotAddNullSource() {
        var actual = systemUnderTest.addActivity(new Activity("test", "test", "test",10l,null));
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteByIdTest() {
        assertEquals(systemUnderTest.deleteActivityById(0L).getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addActivityTest1() {
        var actual = systemUnderTest.addActivity(new Activity("test", "test", "test",10l,"test"));
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }
    @Test
    public void getRandom60Test(){
       var actual = systemUnderTest.get60RandomActivities();
       assertEquals(actual.getStatusCode(),HttpStatus.BAD_REQUEST);
    }
}
