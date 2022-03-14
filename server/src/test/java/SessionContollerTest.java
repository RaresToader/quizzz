import commons.Activity;
import commons.QuizzQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Session;
import server.SessionContainer;
import server.SessionController;

import static org.junit.jupiter.api.Assertions.*;

public class SessionContollerTest {

    @BeforeEach
    public void setup() {
        SessionContainer.clear();
    }

    @Test
    public void getActivePlayersEmptyTest() {
        SessionController sess = new SessionController();
        assertEquals(0, sess.getActivePlayers());
    }

    @Test
    public void getActivePlayersTest() {
        SessionController sess = new SessionController();
        int singlePlayerSessCount = 55;
        for(int i=0; i<singlePlayerSessCount; i++) {
            String username = "test"+i;
            SessionContainer.createSession(false,username);
        }
        assertEquals(singlePlayerSessCount, sess.getActivePlayers());
    }

    @Test
    public void getCurrentQuestionTest() {
        SessionController sess = new SessionController();
        QuizzQuestion expected = new QuizzQuestion("This is test question", new Activity("abc",55,"abc"), new Activity("bac",66,"bac"), new Activity("cab", 566, "cab"));
        assertEquals(expected,sess.getCurrentQuestion("test"));
    }

    @Test
    public void submitAnswerOutOfGameTest() {
        SessionController sess = new SessionController();
        assertFalse(sess.submitAnswer("test",0,0));
    }

    @Test
    public void submitAnswerInvalidAnswerFormatTest() {
        SessionController sess = new SessionController();
        SessionContainer.createSession(false,"test");
        Session x = (Session)SessionContainer.getSession(SessionContainer.findUserSession("test"));
        x.startGame();
        x.getCurrentQuestion();
        assertFalse(sess.submitAnswer("test",4,0));
    }

    @Test
    public void submitAnswerNotStartedTest() {
        SessionController sess = new SessionController();
        SessionContainer.createSession(false,"test");
        assertFalse(sess.submitAnswer("test",2,0));
    }

    @Test
    public void submitAnswerNotCurrentQuestionTest() {
        SessionController sess = new SessionController();
        SessionContainer.createSession(false,"test");
        Session x = (Session)SessionContainer.getSession(SessionContainer.findUserSession("test"));
        x.startGame();
        x.getCurrentQuestion();
        assertFalse(sess.submitAnswer("test",2,1));
    }

    @Test
    public void submitAnswerTest() {
        SessionController sess = new SessionController();
        SessionContainer.createSession(false,"test");
        Session x = (Session)SessionContainer.getSession(SessionContainer.findUserSession("test"));
        x.startGame();
        x.getCurrentQuestion();
        assertTrue(sess.submitAnswer("test",2,0));
    }
}
