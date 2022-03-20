import commons.Activity;
import commons.Answer;
import commons.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Session;
import server.SessionController;
import server.api.TestActivityRepository;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {

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
    }

    @Test
    public void singleplayerInitTest() {
        Session x = new Session(false,sess.get60RandomActivities());
        assertFalse(x.isStarted());
        assertNotNull(x.getPlayerList());
        assertEquals(0, x.getPlayerNum());
        assertNull(x.getGameAdmin());
        assertFalse(x.isGameType());
        assertEquals(20, x.getQuestions().size());
        assertEquals(0, x.getAnswers().size());
    }

    @Test
    public void haveEveryoneAnsweredSingleplayerTest() {
        Session x = new Session(false,sess.get60RandomActivities());
        x.addPlayer("test");
        Answer toAdd = new Answer("test", 0, -1);
        x.addAnswer(toAdd);
        assertTrue(x.haveEveryoneAnswered());
    }

    @Test
    public void haveEveryoneAnsweredSingleplayer2Test() {
        Session x = new Session(false,sess.get60RandomActivities());
        x.addPlayer("test");
        assertFalse(x.haveEveryoneAnswered());
    }

    @Test
    public void getQuestionNotStartedTest() {
        Session x = new Session(false,sess.get60RandomActivities());
        assertEquals(Session.emptyQ,x.getCurrentQuestion());
    }

    @Test
    public void getQuestionStartedTest() {
        Session x = new Session(false,sess.get60RandomActivities());
        Date date = new Date();
        Question testQ = x.getQuestions().get(0);
        x.startGame();
        assertEquals(testQ, x.getCurrentQuestion().getQuestion());
    }

    @Test
    public void isSingleplayerAvailable() {
        Session x = new Session(false,sess.get60RandomActivities());
        assertTrue(x.isAvailable("test"));
    }

    @Test
    public void isSingleplayerAvailableAfterAddition() {
        Session x = new Session(false,sess.get60RandomActivities());
        x.addPlayer("test");
        assertFalse(x.isAvailable("test2"));
    }

    @Test
    public void removeFromSession() {
        Session x = new Session(false,sess.get60RandomActivities());
        x.addPlayer("test");
        assertTrue(x.removePlayer("test"));
    }

    @Test
    public void removeFromEmptySession() {
        Session x = new Session(false,sess.get60RandomActivities());
        assertFalse(x.removePlayer("test"));
    }

    @Test
    public void isPlayerInSession() {
        Session x = new Session(false,sess.get60RandomActivities());
        x.addPlayer("test");
        assertTrue(x.isPlayerInSession("test"));
        assertFalse(x.isPlayerInSession("test2"));
    }


}
