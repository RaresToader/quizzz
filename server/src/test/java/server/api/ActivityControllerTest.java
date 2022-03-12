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

    @Test
    public void cannotDeleteNullActivity() {
        var actual = systemUnderTest.deleteActivityByObject(null);
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteByObject1() {
        var actual = systemUnderTest.deleteActivityByObject(new Activity("test", 0, "test"));
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteByObject2() {
        Activity test = new Activity("test", 0, "test");
        systemUnderTest.deleteActivityByObject(test);
        assertTrue(repo.calledMethods.contains("delete"));
    }

    @Test
    public void deleteById1() {
        var actual = systemUnderTest.deleteActivityById(0);
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteById2() {
        Activity test = new Activity("test", 0, "test");
        systemUnderTest.addActivity(test);
        var actual = systemUnderTest.deleteActivityById(test.getId());
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteById3() {
        Activity test = new Activity("test", 0, "test");
        systemUnderTest.addActivity(test);
        systemUnderTest.deleteActivityById(test.getId());
        assertTrue(repo.calledMethods.contains("deleteById"));
    }

    @Test
    public void modifyTest1() {
        var actual = systemUnderTest.modifyActivity(null);
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void modifyTest2() {
        var actual = systemUnderTest.modifyActivity(new Activity(null, 0, "test"));
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void modifyTest3() {
        var actual = systemUnderTest.modifyActivity(new Activity("test", 0, null));
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void modifyTest4() {
        var actual = systemUnderTest.modifyActivity(new Activity("test", 0, "test"));
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void modifyTest5() {
        Activity test = new Activity("test", 0, "test");
        systemUnderTest.addActivity(test);
        test.setConsumption_in_wh(10);
        var actual = systemUnderTest.modifyActivity(test);
        assertEquals(actual.getStatusCode(),HttpStatus.OK);
    }

    @Test
    public void modifyTest6() {
        Activity test = new Activity("test", 0, "test");
        systemUnderTest.addActivity(test);
        test.setConsumption_in_wh(10);
        systemUnderTest.modifyActivity(test);
        assertTrue(repo.calledMethods.contains("save"));
    }
}
