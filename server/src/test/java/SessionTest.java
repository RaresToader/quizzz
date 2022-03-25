import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Session;
import server.SessionController;
import server.api.TestActivityRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {

    private TestActivityRepository repo;
    private SessionController sess;

    @BeforeEach
    public void setup() {
        repo = new TestActivityRepository();
        for(int i=0; i<60; i++) {
            Activity toBeAdded = new Activity("test"+i, "test","10", 10L,"test");
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
    public void gameNotAvailableWhenStarted() {
        Session x = new Session(false,sess.get60RandomActivities());
        x.addPlayer("test2");
        x.startGame();
        assertFalse(x.isAvailable("test"));
    }

    @Test
    public void gameNotAvailableWhenAlreadyIn() {
        Session x = new Session(true,sess.get60RandomActivities());
        x.addPlayer("test2");
        assertFalse(x.isAvailable("test2"));
    }

    @Test
    public void endGameTest() {
        Session x = new Session(false, sess.get60RandomActivities());
        x.endGame();
        assertTrue(x.hasEnded());
    }
    @Test
    public void addPlayerFullSession() {
        Session x = new Session(false,sess.get60RandomActivities());
        x.addPlayer("test");
        assertFalse(x.addPlayer("test2"));
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
    public void removeFromNoneContainedSession() {
        Session x = new Session(false,sess.get60RandomActivities());
        x.addPlayer("test");
        assertFalse(x.removePlayer("test2"));
    }

    @Test
    public void gameAdminNullAfterRemoval() {
        Session x = new Session(false,sess.get60RandomActivities());
        x.addPlayer("test");
        x.removePlayer("test");
        assertNull(x.getGameAdmin());
    }

    @Test
    public void gameAdminOtherAfterRemoval() {
        Session x = new Session(true,sess.get60RandomActivities());
        x.addPlayer("test");
        x.addPlayer("test2");
        x.removePlayer("test");
        assertEquals("test2",x.getGameAdmin());
    }

    @Test
    public void isNullPlayerInSession() {
        Session x = new Session(false, sess.get60RandomActivities());
        assertFalse(x.isPlayerInSession(null));
    }


    @Test
    public void isPlayerInSession() {
        Session x = new Session(false,sess.get60RandomActivities());
        x.addPlayer("test");
        assertTrue(x.isPlayerInSession("test"));
        assertFalse(x.isPlayerInSession("test2"));
    }

    @Test
    public void getJokerListTest() {
        Session x = new Session(false,sess.get60RandomActivities()); //Create session
        x.addPlayer("test"); //Add player
        x.startGame(); //Start it
        x.getCurrentQuestion(); //Move question counter to 0

        assertEquals(new ArrayList<Joker>(), x.getJokersForCurrentQuestion("test2"));

        x.addJoker(0,"test",0);
        List<Joker> toBeObtained = new ArrayList<Joker>();
        toBeObtained.add(new Joker("test",0,0));
        assertEquals(new ArrayList<Joker>(), x.getJokersForCurrentQuestion("test"));
        assertEquals(toBeObtained, x.getJokersForCurrentQuestion("test2"));
    }

    @Test
    public void addJokerTest() {
        Session x = new Session(false,sess.get60RandomActivities()); //Create session
        x.addPlayer("test"); //Add player
        x.startGame(); //Start it
        x.getCurrentQuestion(); //Move question counter to 0

        x.addJoker(0,"test",0);
        List<Joker> toBeObtained = new ArrayList<Joker>();
        toBeObtained.add(new Joker("test",0,0));

        assertEquals(1,x.getJokersForCurrentQuestion("test2").size());
        assertEquals(toBeObtained,x.getJokersForCurrentQuestion("test2"));
    }

    @Test
    public void addJokerPlayerNotInSessionTest() {
        Session x = new Session(false,sess.get60RandomActivities()); //Create session
        x.addPlayer("test"); //Add player
        x.startGame(); //Start it
        x.getCurrentQuestion(); //Move question counter to 0

        x.addJoker(0,"test2",0);
        assertEquals(new ArrayList<>(),x.getJokersForCurrentQuestion("test"));
    }

    @Test
    public void addJokerPlayerNotCurrentQuestionTest() {
        Session x = new Session(false,sess.get60RandomActivities()); //Create session
        x.addPlayer("test"); //Add player
        x.startGame(); //Start it
        x.getCurrentQuestion(); //Move question counter to 0

        x.addJoker(0,"test",5);
        assertEquals(new ArrayList<>(),x.getJokersForCurrentQuestion("test2"));
    }

    @Test
    public void addDuplicateAnswerTest() {
        Session x = new Session(false,sess.get60RandomActivities()); //Create session
        x.addPlayer("test");
        x.startGame();
        x.getCurrentQuestion();

        assertTrue(x.addAnswer(new Answer("test",0,0)));
        assertFalse(x.addAnswer(new Answer("test",0,0)));
    }

    @Test
    public void endGameEventTest() {
        Session x = new Session(false,sess.get60RandomActivities()); //Create session
        x.addPlayer("test");
        x.startGame();
        x.getCurrentQuestion();
        x.setCurrentQuestionNum(25);
        assertEquals(25,x.getCurrentQuestionNum());
        assertEquals(Session.emptyQ,x.getCurrentQuestion());
        assertTrue(x.hasEnded());
    }

    @Test
    public void emptySessionToString() {
        Session x = new Session(false,sess.get60RandomActivities());
        String expected = "Session{" +
                "playerList=" + x.getPlayerList() +
                ", started=" + x.isStarted() +
                ", ended=" + x.hasEnded() +
                ", gameAdmin='" + x.getGameAdmin() + '\'' +
                ", gameType=" + x.isGameType() +
                ", questions=" + x.getQuestions() +
                ", answers=" + x.getAnswers() +
                ", usedJokers=" + x.getUsedJokers() +
                ", currentQuestion=" + x.getCurrentQuestionNum() +
                ", questionStartedAt=" + x.getQuestionStartedAt() +
                '}';

        assertEquals(expected,x.toString());
    }

    @Test
    public void addNullAnswer() {
        Session x = new Session(false,sess.get60RandomActivities());
        assertFalse(x.addAnswer(null));
    }

    @Test
    public void addPlayerNotInSessionAnswer() {
        Session x = new Session(false,sess.get60RandomActivities());
        assertFalse(x.addAnswer(new Answer("test",0,0)));
    }

    @Test
    public void addPlayerWrongQuestion() {
        Session x = new Session(false,sess.get60RandomActivities());
        x.addPlayer("test");
        assertFalse(x.addAnswer(new Answer("test",0,3)));
    }

    @Test
    public void nullEqualsTest() {
        Session x = new Session(false,sess.get60RandomActivities());
        assertFalse(x.equals(null));
    }

    @Test
    public void equalsSameDiffInstanceTest() {
        List<Activity> activities = sess.get60RandomActivities();
        Session x = new Session(false,activities);
        Session y = new Session(false,activities);
        assertFalse(x.equals(y));
    }

    @Test
    public void equalsNotSameTest() {
        List<Activity> activities = sess.get60RandomActivities();
        Session x = new Session(false,activities);
        Session y = new Session(true,activities);
        assertFalse(x.equals(y));
    }

    @Test
    public void equalsSameInstanceTest() {
        Session x = new Session(false,sess.get60RandomActivities());
        assertTrue(x.equals(x));
    }

    @Test
    public void setQuestionStartedAtTest() {
        Session x = new Session(false,sess.get60RandomActivities());
        x.setQuestionStartedAt(Long.valueOf(0));
        assertEquals(Long.valueOf(0),x.getQuestionStartedAt());
    }
    @Test
    public void addEmojiTest() {
        Session x = new Session(true, sess.get60RandomActivities());
        Emoji emoji1 = new Emoji("user1", "emoji1");
        Emoji emoji2 = new Emoji("user2", "emoij2");
        List<Emoji> emojiList = new ArrayList<Emoji>();
        emojiList.add(emoji1);
        emojiList.add(emoji2);
        x.addEmoij(emoji1);
        x.addEmoij(emoji2);
        assertEquals(emojiList, x.getEmojiList());
    }
    @Test
    public void getCurrentLeaderboardTest() {
        Session x = new Session(false,sess.get60RandomActivities());
        x.addPlayer("test");
        assertTrue(x.addAnswer(new Answer("test",10,x.getCurrentQuestionNum())));
        HashMap<String,Integer> expected = new HashMap<>();
        expected.put("test",10);
        assertEquals(new ArrayList<Map.Entry<String,Integer>>(expected.entrySet()),x.getCurrentLeaderboard());
    }

    @Test
    public void getEmojiListTest(){
        Session x = new Session(true,sess.get60RandomActivities());
        Emoji emoji1 = new Emoji("user1","emoji1");
        Emoji emoji2 = new Emoji("user2", "emoij2");
        List<Emoji> emojiList = new ArrayList<Emoji>();
        emojiList.add(emoji1);
        emojiList.add(emoji2);
        x.addEmoij(emoji1);
        x.addEmoij(emoji2);
        assertEquals(emojiList,x.getEmojiList());
    }

    @Test
    public void getActiveEmojiListTest(){
        Session x = new Session(true,sess.get60RandomActivities());
        Emoji emoji1 = new Emoji("user1","emoji1");
        Emoji emoji2 = new Emoji("user2", "emoij2");
        List<Emoji> emojiList = new ArrayList<Emoji>();
        emojiList.add(emoji1);
        emojiList.add(emoji2);
        x.addEmoij(emoji1);
        x.addEmoij(emoji2);
        assertEquals(emojiList,x.getActiveEmoijList());
        emoji2.setStartTimeEmoji(emoji2.getStartTimeEmoji() - 5500);
        List<Emoji> emojiList2 = new ArrayList<Emoji>();
        emojiList2.add(emoji1);
        assertEquals(emojiList2,x.getActiveEmoijList());
        assertEquals(emojiList2,x.getEmojiList());
        Emoji emoji3 = new Emoji("user1","emoji3");
        x.addEmoij(emoji3);
        emoji1.setStartTimeEmoji(emoji1.getStartTimeEmoji() - 2000);
        List<Emoji> emojiList3 = new ArrayList<Emoji>();
        emojiList3.add(emoji3);
        assertEquals(emojiList3,x.getActiveEmoijList());
        Emoji emoji4 = new Emoji("user1", "emoij4");
        emoji4.setStartTimeEmoji(emoji4.getStartTimeEmoji() + 1000);
        x.addEmoij(emoji4);
        emojiList3.remove(emoji3);
        emojiList3.add(emoji4);
        assertEquals(emojiList3,x.getActiveEmoijList());
    }
}
