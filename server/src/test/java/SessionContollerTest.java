import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Session;
import server.SessionContainer;
import server.SessionController;
import server.api.TestActivityRepository;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class SessionContollerTest {

    private TestActivityRepository repo;

    @BeforeEach
    public void setup() {
        repo = new TestActivityRepository();
        for(int i=0; i<60; i++) {
            Activity toBeAdded = new Activity("test"+i, "test","10", 10L       ,"test");
            repo.save(toBeAdded);
        }
        SessionContainer.clear();
    }

    @Test
    public void getActivePlayersEmptyTest() {
        SessionController sess = new SessionController(repo);
        assertEquals(0, sess.getActivePlayers());
    }

    @Test
    public void getActivePlayersTest() {
        SessionController sess = new SessionController(repo);
        int singlePlayerSessCount = 55;
        for(int i=0; i<singlePlayerSessCount; i++) {
            String username = "test"+i;
            SessionContainer.createSession(false,username,sess.get60RandomActivities());
        }
        assertEquals(singlePlayerSessCount, sess.getActivePlayers());
    }

    @Test
    public void getCurrentQuestionTest() {
        SessionController sess = new SessionController(repo);
        Date date = new Date();
        assertNotEquals(Session.emptyQ,sess.getCurrentQuestion("test"));
    }

    @Test
    public void submitAnswerOutOfGameTest() {
        SessionController sess = new SessionController(repo);
        assertFalse(sess.submitAnswer("test",0,0));
    }

    @Test
    public void submitAnswerInvalidAnswerFormatTest() {
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(false,"test",sess.get60RandomActivities());
        Session x = (Session)SessionContainer.getSession(SessionContainer.findUserSession("test"));
        x.startGame();
        x.getCurrentQuestion();
        assertFalse(sess.submitAnswer("test",4,0));
    }

    @Test
    public void submitAnswerNotStartedTest() {
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(false,"test",sess.get60RandomActivities());
        assertFalse(sess.submitAnswer("test",2,0));
    }

    @Test
    public void submitAnswerNotCurrentQuestionTest() {
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(false,"test",sess.get60RandomActivities());
        Session x = (Session)SessionContainer.getSession(SessionContainer.findUserSession("test"));
        x.startGame();
        x.getCurrentQuestion();
        assertFalse(sess.submitAnswer("test",2,1));
    }

    @Test
    public void submitAnswerTest() {
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(false,"test",sess.get60RandomActivities());
        Session x = (Session)SessionContainer.getSession(SessionContainer.findUserSession("test"));
        x.startGame();
        x.getCurrentQuestion();
        assertTrue(sess.submitAnswer("test",2,0));
    }
}
