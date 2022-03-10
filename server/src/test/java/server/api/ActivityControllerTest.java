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
    public void getAllTest() {
        var actual = systemUnderTest.getAll();
        assertNotNull(actual);
    }

    @Test
    public void cannotAddNullActivity() {
        var actual = systemUnderTest.addActivity(null);
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void cannotAddNullSource() {
        var actual = systemUnderTest.addActivity(new Activity("test", 0, null));
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void cannotAddNullTitle() {
        var actual = systemUnderTest.addActivity(new Activity(null, 0, "test"));
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addActivityTest1() {
        var actual = systemUnderTest.addActivity(new Activity("test", 0, "test"));
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void addActivityTest2() {
        var actual = systemUnderTest.addActivity(new Activity("test", 0, "test"));
        assertTrue(repo.calledMethods.contains("save"));
    }

    @Test
    public void deleteAllTest1() {
        var actual = systemUnderTest.deleteAllActivities();
        assertTrue(repo.calledMethods.contains("deleteAll"));
    }

    @Test
    public void deleteAllTest2() {
        var actual = systemUnderTest.deleteAllActivities();
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

}
