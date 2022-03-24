import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Session;
import server.SessionContainer;
import server.SessionController;
import server.api.TestActivityRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SessionContainerTest {

    private TestActivityRepository repo;
    private SessionController sess;

    @BeforeEach
    public void setup() {
        repo = new TestActivityRepository();
        for (int i = 0; i < 60; i++) {
            Activity toBeAdded = new Activity("test" + i, "test", "10", 10L, "test");
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
        SessionContainer.createSession(false, "test", sess.get60RandomActivities());
        assertEquals(0, SessionContainer.findUserSession("test"));
        assertEquals(-1, SessionContainer.findUserSession("test2"));
        assertEquals(-1, SessionContainer.findUserSession(null));
    }

    @Test
    public void findAvailableSessionTest() {
        SessionContainer.createSession(false, "test", sess.get60RandomActivities());
        System.out.println(SessionContainer.getSessionList().size());
        assertEquals(-1, SessionContainer.findAvailableSession("test"));
        assertEquals(-1, SessionContainer.findAvailableSession("test2"));
        SessionContainer.createSession(true, "test2", sess.get60RandomActivities());
        System.out.println(SessionContainer.getSessionList().size());
        assertEquals(1, SessionContainer.findAvailableSession("test3"));
    }

    @Test
    public void emptyConstructorTest() {
        SessionContainer x = new SessionContainer();
        assertNotNull(x);
    }

    @Test
    public void setSessionListTest() {
        List<Session> sessionList = new ArrayList<Session>();
        sessionList.add(null);
        SessionContainer.setSessionList(sessionList);
        assertEquals(sessionList, SessionContainer.getSessionList());
    }

    @Test
    public void getNumOfActivePlayerNullTest() {
        List<Session> sessionList = new ArrayList<Session>();
        sessionList.add(null);
        SessionContainer.setSessionList(sessionList);
        assertEquals(0, SessionContainer.getNumOfActivePlayers());
    }

    @Test
    public void findUserSessionNullTest() {
        List<Session> sessionList = new ArrayList<Session>();
        sessionList.add(null);
        SessionContainer.setSessionList(sessionList);
        assertEquals(-1, SessionContainer.findUserSession("test"));
    }

    @Test
    public void findUserSessionHasEndedTest() {
        List<Session> sessionList = new ArrayList<Session>();
        Session x = new Session(false, sess.get60RandomActivities());
        x.endGame();
        sessionList.add(x);
        SessionContainer.setSessionList(sessionList);
        assertEquals(-1, SessionContainer.findUserSession("test"));
    }

    @Test
    public void findAvailableSessionNullTest() {
        List<Session> sessionList = new ArrayList<Session>();
        sessionList.add(null);
        SessionContainer.setSessionList(sessionList);
        assertEquals(-1, SessionContainer.findAvailableSession("test"));
    }

    @Test
    public void greaterIdTest() {
        assertNull(SessionContainer.getSession(5));
    }

    @Test
    public void nullSessionListCleanupTest() {
        SessionContainer.setSessionList(null);
        SessionContainer.cleanup();
        assertNull(SessionContainer.getSessionList());
    }

    @Test
    public void emptySessionCleanupTest() {
        List<Session> sessionList = new ArrayList<Session>();
        Session x = new Session(false, sess.get60RandomActivities());
        sessionList.add(x);
        SessionContainer.setSessionList(sessionList);
        SessionContainer.cleanup();
        List<Session> toBeObtained = new ArrayList<Session>();
        toBeObtained.add(null);
        assertEquals(toBeObtained,SessionContainer.getSessionList());
    }

    @Test
    public void maxSessionNumberExceededTest() {
        List<Session> sessionList = new ArrayList<>();
        for(int i=0; i<100; i++) {
            sessionList.add(new Session(false, sess.get60RandomActivities()));
        }

        SessionContainer.setSessionList(sessionList);
        assertFalse(SessionContainer.createSession(false,"test",sess.get60RandomActivities()));
    }

    @Test
    public void createInPlaceOfNullTest() {
        List<Session> sessionList = new ArrayList<>();
        sessionList.add(null);
        List<Activity> activities = sess.get60RandomActivities();
        SessionContainer.setSessionList(sessionList);
        assertTrue(SessionContainer.createSession(false,"test",activities));
        Session x = new Session(false,activities);
        x.addPlayer("test");
        sessionList.set(0,x);
        assertEquals(sessionList,SessionContainer.getSessionList());
    }
}
