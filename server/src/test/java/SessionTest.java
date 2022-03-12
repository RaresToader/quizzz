import commons.Activity;
import commons.Answer;
import commons.QuizzQuestion;
import org.junit.jupiter.api.Test;
import server.Session;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {
    @Test
    public void singleplayerInitTest() {
        Session x = new Session(false);
        assertFalse(x.isStarted());
        assertNotNull(x.getPlayerList());
        assertEquals(0,x.getPlayerNum());
        assertNull(x.getGameAdmin());
        assertFalse(x.isGameType());
        assertEquals(1,x.getQuestions().size());
        assertEquals(0,x.getAnswers().size());
    }

    @Test
    public void haveEveryoneAnsweredSingleplayerTest() {
        Session x = new Session(false);
        x.addPlayer("test");
        Answer toAdd = new Answer("test",0,-1);
        x.addAnswer(toAdd);
        assertTrue(x.haveEveryoneAnswered());
    }

    @Test
    public void haveEveryoneAnsweredSingleplayer2Test() {
        Session x = new Session(false);
        x.addPlayer("test");
        assertFalse(x.haveEveryoneAnswered());
    }

    @Test
    public void getQuestionNotStartedTest() {
        Session x = new Session(false);
        assertNull(x.getCurrentQuestion());
    }

    @Test
    public void getQuestionStartedTest() {
        Session x = new Session(false);
        QuizzQuestion testQ = new QuizzQuestion("This is test question", new Activity("abc","abc","abc",10L,"abc"),  new Activity("bac","bac","bac",10L,"bac"),  new Activity("cab","cab","cab",10L,"cab"));
        x.startGame();
        assertEquals(testQ,x.getCurrentQuestion());
    }

    @Test
    public void isSingleplayerAvailable() {
        Session x = new Session(false);
        assertTrue(x.isAvailable("test"));
    }

    @Test
    public void isSingleplayerAvailableAfterAddition() {
        Session x = new Session(false);
        x.addPlayer("test");
        assertFalse(x.isAvailable("test2"));
    }

    @Test
    public void removeFromSession() {
        Session x = new Session(false);
        x.addPlayer("test");
        assertTrue(x.removePlayer("test"));
    }

    @Test
    public void removeFromEmptySession() {
        Session x = new Session(false);
        assertFalse(x.removePlayer("test"));
    }

    @Test
    public void isPlayerInSession() {
        Session x = new Session(false);
        x.addPlayer("test");
        assertTrue(x.isPlayerInSession("test"));
        assertFalse(x.isPlayerInSession("test2"));
    }

}
