import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.SessionContainer;
import server.SessionController;
import server.api.TestActivityRepository;

import static org.junit.jupiter.api.Assertions.*;

public class SessionContainerTest {

    private TestActivityRepository repo;
    private SessionController sess;

    @BeforeEach
    public void setup() {
        repo = new TestActivityRepository();
        for(int i=0; i<60; i++) {
            Activity toBeAdded = new Activity("test"+i, "test","10", 10L       ,"test");
            repo.save(toBeAdded);
        }
        sess = new SessionController(repo);
        SessionContainer.clear();
    }

    @Test
    public void createSessionTest() {
        SessionContainer.createSession(false, "test", sess.get60RandomActivities());
        assertEquals(1, SessionContainer.getSessionList().size());
        assertTrue(SessionContainer.getSessionList().get(0).isPlayerInSession("test"));
    }

    @Test
    public void findUserSessionTest() {
        SessionContainer.createSession(false, "test",sess.get60RandomActivities());
        assertEquals(0, SessionContainer.findUserSession("test"));
        assertEquals(-1, SessionContainer.findUserSession("test2"));
        assertEquals(-1, SessionContainer.findUserSession(null));
    }

    @Test
    public void findAvailableSessionTest() {
        SessionContainer.createSession(false, "test",sess.get60RandomActivities());
        System.out.println(SessionContainer.getSessionList().size());
        assertEquals(-1, SessionContainer.findAvailableSession("test"));
        assertEquals(-1, SessionContainer.findAvailableSession("test2"));
        SessionContainer.createSession(true, "test2",sess.get60RandomActivities());
        System.out.println(SessionContainer.getSessionList().size());
        assertEquals(1, SessionContainer.findAvailableSession("test3"));
    }
}
