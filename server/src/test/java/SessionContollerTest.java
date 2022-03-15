import commons.Activity;
import commons.QuizzQuestion;
import commons.QuizzQuestionServerParsed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Session;
import server.SessionContainer;
import server.SessionController;

import java.time.LocalDate;

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
        QuizzQuestionServerParsed expected = new QuizzQuestionServerParsed(new QuizzQuestion("This is test question", new Activity("abc","abc","abc",10l,"abc"), new Activity("bac","bac","bac",10l,"bac"),new Activity("cab","cab","cab",10l,"cab")), LocalDate.now(),0);
        assertEquals(sess.getCurrentQuestion("test"),expected);
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
