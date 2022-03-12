package server.api;


import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import server.database.ActivityRepository;

import static org.junit.jupiter.api.Assertions.*;

public class ActivityControllerTest {
    /**
     * Might not be sufficient, we may need to create a mock
     * activity repository class as their example did(with test quote
     * repository)
     */
    private ActivityRepository testRepo = Mockito.mock(ActivityRepository.class);
    private ActivityController systemUnderTest;

    @BeforeEach
    public void setup() {
        systemUnderTest = new ActivityController(testRepo);
    }

    @Test
    public void allActivitiesTest() {
        assertNotNull(systemUnderTest.getAll());
    }

    @Test
    public void addActivity() {
        assertNotEquals(systemUnderTest.addActivity(new Activity("test", "test",10, "test","test")).getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addNullActivity() {
        assertEquals(systemUnderTest.addActivity(null).getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteAllTest() {
        assertNotEquals(HttpStatus.BAD_REQUEST, systemUnderTest.deleteAllActivities().getStatusCode());
    }

    @Test
    public void deleteByIdTest() {
        assertEquals(systemUnderTest.deleteActivityById("nothing").getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void modify() {
        assertEquals(systemUnderTest.modifyActivity(Mockito.mock(Activity.class)).getStatusCode(), HttpStatus.BAD_REQUEST);
    }

}
