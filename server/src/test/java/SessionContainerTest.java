import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.SessionContainer;

import static org.junit.jupiter.api.Assertions.*;

public class SessionContainerTest {

    @BeforeEach
    public void setup() {
        SessionContainer.clear();
    }

    @Test
    public void createSessionTest() {
        SessionContainer.createSession(false, "test");
        assertEquals(1, SessionContainer.getSessionList().size());
        assertTrue(SessionContainer.getSessionList().get(0).isPlayerInSession("test"));
    }

    @Test
    public void findUserSessionTest() {
        SessionContainer.createSession(false, "test");
        assertEquals(0, SessionContainer.findUserSession("test"));
        assertEquals(-1, SessionContainer.findUserSession("test2"));
        assertEquals(-1, SessionContainer.findUserSession(null));
    }

    @Test
    public void findAvailableSessionTest() {
        SessionContainer.createSession(false, "test");
        System.out.println(SessionContainer.getSessionList().size());
        assertEquals(-1, SessionContainer.findAvailableSession("test"));
        assertEquals(-1, SessionContainer.findAvailableSession("test2"));
        SessionContainer.createSession(true, "test2");
        System.out.println(SessionContainer.getSessionList().size());
        assertEquals(1, SessionContainer.findAvailableSession("test3"));
    }
}
