package server.api;


import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.json.simple.parser.ParseException;

import java.io.IOException;

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
        assertNotEquals(systemUnderTest.addActivity(new Activity("test", "test", "10", 10L, "test")).getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void cannotAddNullActivity() {
        var actual = systemUnderTest.addActivity(null);
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void cannotAddNullSource() {
        var actual = systemUnderTest.addActivity(new Activity("test", "test", "test", 10L, null));
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void cannotAddNullId() {
        var actual = systemUnderTest.addActivity(new Activity(null, "test", "test", 10L, "test"));
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void cannotAddNullTitle() {
        var actual = systemUnderTest.addActivity(new Activity("test", "test", null, 10L, "test"));
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteByIdTest() {
        assertEquals(systemUnderTest.deleteActivityById("test").getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addActivityTest1() {
        var actual = systemUnderTest.addActivity(new Activity("test", "test", "test", 10L, "test"));
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void addActivityTest2() {
        Activity test = new Activity("test", "test", "test", 10L, "test");
        systemUnderTest.addActivity(test);
        assertTrue(repo.activities.contains(test));
    }

    @Test
    public void getAllTest() {
        systemUnderTest.getAll();
        assertTrue(repo.calledMethods.contains("findAll"));
    }

    @Test
    public void getRandom60Test1() {
        var actual = systemUnderTest.get60RandomActivities();
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getRandom60Test2() {
        for (int i = 0; i < 60; i++) {
            systemUnderTest.addActivity(new Activity("id" + i, "test", "test", 0L, "test"));
        }
        var actual = systemUnderTest.get60RandomActivities();
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getRandom60Test3() {
        for (int i = 0; i < 60; i++) {
            systemUnderTest.addActivity(new Activity("id" + i, "test", "test", 0L, "test"));
        }
        assertEquals(systemUnderTest.get60RandomActivities().getBody().size(), 60);
    }

    @Test
    public void getActivityById1() {
        var actual = systemUnderTest.getActivityById("notFound");
        assertEquals(actual.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void getActivityById2() {
        systemUnderTest.getActivityById("notFound");
        assertTrue(repo.calledMethods.contains("findById"));
    }

    @Test
    public void getActivityById3() {
        Activity test = new Activity("test", "test", "test", 10L, "test");
        systemUnderTest.addActivity(test);
        assertEquals(test, systemUnderTest.getActivityById("test").getBody());
    }

    @Test
    public void getActivityById4() {
        Activity test = new Activity("test", "test", "test", 10L, "test");
        systemUnderTest.addActivity(test);
        assertEquals(HttpStatus.OK, systemUnderTest.getActivityById("test").getStatusCode());
    }

    @Test
    public void deleteAllTest1() {
        var actual = systemUnderTest.deleteAllActivities();
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteAllTest2() {
        systemUnderTest.deleteAllActivities();
        assertTrue(repo.calledMethods.contains("deleteAll"));
    }

    @Test
    public void deleteByObjectTest1() {
        var actual = systemUnderTest.deleteActivityByObject(null);
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteByObjectTest2() {
        Activity test = new Activity("test", "test", "test", 10L, "test");
        var actual = systemUnderTest.deleteActivityByObject(test);
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteByObjectTest3() {
        Activity test = new Activity("test", "test", "test", 10L, "test");
        systemUnderTest.deleteActivityByObject(test);
        assertTrue(repo.calledMethods.contains("delete"));
    }

    @Test
    public void deleteByIdTest1() {
        var actual = systemUnderTest.deleteActivityById(null);
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteByIdTest2() {
        var actual = systemUnderTest.deleteActivityById("test");
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteByIdTest3() {
        systemUnderTest.deleteActivityById("test");
        assertTrue(repo.calledMethods.contains("findById"));
    }

    @Test
    public void deleteByIdTest4() {
        Activity test = new Activity("test", "test", "test", 10L, "test");
        systemUnderTest.addActivity(test);
        var actual = systemUnderTest.deleteActivityById("test");
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteByIdTest5() {
        Activity test = new Activity("test", "test", "test", 10L, "test");
        systemUnderTest.addActivity(test);
        systemUnderTest.deleteActivityById("test");
        assertTrue(repo.calledMethods.contains("deleteById"));
    }

    @Test
    public void modifyTest1() {
        var actual = systemUnderTest.modifyActivity(null);
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void modifyTest2() {
        var actual = systemUnderTest.modifyActivity(new Activity(null, "test", "test", 0L, "test"));
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void modifyTest3() {
        var actual = systemUnderTest.modifyActivity(new Activity("test", "test", null, 0L, "test"));
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void modifyTest4() {
        var actual = systemUnderTest.modifyActivity(new Activity("test", "test", "test", 0L, null));
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void modifyTest5() {
        var actual = systemUnderTest.modifyActivity(new Activity("test", null, "test", 0L, "test"));
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
    @Test
    public void modifyTestNonExistentActivity() {
        var actual = systemUnderTest.modifyActivity(new Activity("test", "test", "test", 0L, "test"));
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
    @Test
    public void modifyTest6() {
        Activity test = new Activity("test", "test", "test", 100L, "test");
        systemUnderTest.addActivity(test);
        test = new Activity("test", "new", "new", 0L, "new");
        var actual = systemUnderTest.modifyActivity(test);
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void modifyTest7() {
        Activity test = new Activity("test", "test", "test", 100L, "test");
        systemUnderTest.addActivity(test);
        test = new Activity("test", "new", "new", 0L, "new");
        systemUnderTest.modifyActivity(test);
        assertEquals(test, systemUnderTest.getActivityById("test").getBody());
    }

    @Test
    public void modifyTest8() {
        Activity test = new Activity("test", "test", "test", 100L, "test");
        systemUnderTest.addActivity(test);
        test = new Activity("test", "new", "new", 0L, "new");
        systemUnderTest.modifyActivity(test);
        assertTrue(repo.calledMethods.contains("save"));
    }

    @Test
    public void modifyTest9() {
        Activity test = new Activity("test", "test", "test", 100L, "test");
        systemUnderTest.addActivity(test);
        test = new Activity("test", "new", "new", 0L, "new");
        systemUnderTest.modifyActivity(test);
        assertTrue(repo.calledMethods.contains("deleteById"));
    }

    @Test
    public void modifyTest10() {
        Activity test = new Activity("test", "test", "test", 100L, "test");
        systemUnderTest.addActivity(test);
        test = new Activity("test", "new", "new", 0L, "new");
        systemUnderTest.modifyActivity(test);
        assertTrue(repo.calledMethods.contains("getById"));

    }

    @Test
    public void modifyNonexistentActivity() {
        var actual = systemUnderTest.modifyActivity(new Activity("testNONEXISTENT", "test", "test", 0L, "test"));
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void existsTest1() {
        var actual = systemUnderTest.exists(null);
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void existsTest2() {
        var actual = systemUnderTest.exists(null);
        assertEquals(actual.getBody(), false);
    }

    @Test
    public void existsTest3() {
        var actual = systemUnderTest.exists("test");
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void existsTest4() {
        var actual = systemUnderTest.exists("test");
        assertEquals(actual.getBody(), false);
    }

    @Test
    public void existsTest5() {
        systemUnderTest.addActivity(new Activity("test", "test", "test", 10L, "test"));
        var actual = systemUnderTest.exists("test");
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void existsTest6() {
        systemUnderTest.addActivity(new Activity("test", "test", "test", 10L, "test"));
        var actual = systemUnderTest.exists("test");
        assertEquals(actual.getBody(), true);
    }

    @Test
    public void loadActivitiesTest1() throws IOException, ParseException {
        var actual = systemUnderTest.loadActivities();
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void loadActivitiesTest2() throws IOException, ParseException {
        var actual = systemUnderTest.loadActivities();
        assertNotNull(actual.getBody());
    }

    @Test
    public void loadActivitiesTest3() throws IOException, ParseException {
        var actual = systemUnderTest.loadActivities();
        assertTrue(actual.getBody().size() > 0);
    }

    @Test
    public void loadActivitiesTest4() throws IOException, ParseException {
        systemUnderTest.loadActivities();
        assertTrue(repo.calledMethods.contains("save"));
    }

    @Test
    public void loadActivitiesTest5() {
        assertDoesNotThrow(() -> {
            systemUnderTest.loadActivities();
        });
    }
}
