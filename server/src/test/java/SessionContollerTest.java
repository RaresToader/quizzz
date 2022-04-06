import commons.Activity;
import commons.Emoji;
import commons.Answer;
import commons.SessionLobbyStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Session;
import server.SessionContainer;
import server.SessionController;
import server.api.TestActivityRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SessionContollerTest {

    private TestActivityRepository repo;

    @BeforeEach
    public void setup() {
        repo = new TestActivityRepository();
        for(int i=0; i<10000; i++) {
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
    public void get60RandomActivitiesLessThan60Test() {
        TestActivityRepository emptyRepo = new TestActivityRepository();
        for(int i=0; i<59; i++) {
            Activity toBeAdded = new Activity("test"+i, "test","10", 10L       ,"test");
            emptyRepo.save(toBeAdded);
        }
        SessionController sess = new SessionController(emptyRepo);
        assertNull(sess.get60RandomActivities());
    }

    @Test
    public void get60RandomActivitiesTest() {
        TestActivityRepository emptyRepo = new TestActivityRepository();
        for(int i=0; i<60; i++) {
            Activity toBeAdded = new Activity("test"+i, "test","10", 10L       ,"test");
            emptyRepo.save(toBeAdded);
        }
        SessionController sess = new SessionController(emptyRepo);
        assertNotNull(sess.get60RandomActivities());
    }

    @Test
    public void lobbyNotAssignedStatusTest() {
        SessionController sess = new SessionController(repo);
        assertEquals(new SessionLobbyStatus(new ArrayList<Emoji>(),false, "none"), sess.getLobbyStatus("test"));
    }

    @Test
    public void displayAllDetailsEmpty() {
        SessionController sess = new SessionController(repo);
        assertEquals("NO SESSION",sess.displayAllSessionDetails("test"));
    }

    @Test
    public void displayAllDetailsNonEmpty() {
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(false,"test",repo.activities);
        Session x = SessionContainer.getSession(SessionContainer.findUserSession("test"));
        assertEquals(x.toString(),sess.displayAllSessionDetails("test"));
    }

    @Test
    public void getLeaderboardNoSession() {
        SessionController sess = new SessionController(repo);
        assertEquals(new ArrayList<>(),sess.getCurrentLeaderboard("test"));
    }

    @Test
    public void getLeaderboardNonEmpty() {
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(false,"test",repo.activities);
        Session x = SessionContainer.getSession(SessionContainer.findUserSession("test"));
        assertEquals(x.getCurrentLeaderboard(),sess.getCurrentLeaderboard("test"));
    }



    @Test
    public void getActivePlayersTest() {
        SessionController sess = new SessionController(repo);
        int singlePlayerSessCount = 55;
        for(int i=0; i<singlePlayerSessCount; i++) {
            String username = "test"+i;
            SessionContainer.createSession(false,username,repo.activities);
        }
        assertEquals(singlePlayerSessCount, sess.getActivePlayers());
    }

    @Test
    public void getPlayersInSessionTest(){
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(true,"test",repo.activities);
        List<String> list = new ArrayList<String>();
        list.add("test");
        assertEquals(list,sess.getPlayersInSession("test"));
        int sessionId = SessionContainer.findUserSession("test");
        Session session = SessionContainer.getSession(sessionId);
        session.addPlayer("test1");
        session.addPlayer("test2");
        session.addPlayer("test3");
        list.add("test1");
        list.add("test2");
        list.add("test3");
        assertEquals(list,sess.getPlayersInSession("test"));
        session.removePlayer("test2");
        assertNotEquals(list,sess.getPlayersInSession("test"));
    }
    @Test
    public void getCurrentLeaderboardTest(){
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(true,"test",repo.activities);
        List<String> list = new ArrayList<String>();
        list.add("test");
        int sessionId = SessionContainer.findUserSession("test");
        Session session = SessionContainer.getSession(sessionId);
        session.addPlayer("test1");
        session.addPlayer("test2");
        session.addPlayer("test3");
        list.add("test1");
        list.add("test2");
        list.add("test3");
        HashMap<String,Integer> expected = new HashMap<>();
        expected.put("test1",10);
        expected.put("test",0);
        expected.put("test2",0);
        expected.put("test3",0);
        session.addAnswer(new Answer("test1",10,-1));
        assertEquals(new ArrayList<>(expected.entrySet()),session.getCurrentLeaderboard());
    }

    @Test
    public void getPlayersInSessionNotFoundTest() {
        SessionController sess = new SessionController(repo);
        List<String> list = new ArrayList<String>();
        list.add("test");
        assertEquals(list,sess.getPlayersInSession("test"));
    }
    @Test
    public void setEmojiTest(){
        SessionController sess = new SessionController(repo);
        assertFalse(sess.setEmoji("user","emoji"));
        SessionContainer.createSession(true,"test",repo.activities);
        int sessionId = SessionContainer.findUserSession("test");
        Session session = SessionContainer.getSession(sessionId);
        session.addPlayer("test1");
        session.addPlayer("test2");
        session.addPlayer("test3");
        List<Emoji> list = new ArrayList<>();
        Emoji emoji1 = new Emoji("test1","emoji1");
        Emoji emoji2 = new Emoji("test2","emoji2");
        list.add(emoji1);
        assertTrue(sess.setEmoji("test1", "emoji1"));
        assertEquals(list,session.getActiveEmoijList());
        assertTrue(sess.setEmoji("test1", "emoji3"));
        list.remove(emoji1);
        Emoji emoji3 = new Emoji("test1","emoji3");
        list.add(emoji3);
        assertEquals(list,session.getActiveEmoijList());
    }
    @Test
    public void getLobbyStatusTest(){
        List<Emoji> list = new ArrayList<>();
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(true,"test",repo.activities);
        int sessionId = SessionContainer.findUserSession("test");

        Session session = SessionContainer.getSession(sessionId);

        session.addPlayer("test1");
        session.addPlayer("test2");
        session.addPlayer("test3");

        Emoji emoji1 = new Emoji("test1","emoji1");
        list.add(emoji1);
        session.addEmoij(emoji1);

        SessionLobbyStatus lobbyStatus = new SessionLobbyStatus(list,false,"test");
        assertEquals(lobbyStatus,sess.getLobbyStatus("test"));
    }

    @Test
    public void getCurrentQuestionTest() {
        SessionController sess = new SessionController(repo);
        Date date = new Date();
        assertNotEquals(Session.emptyQ,sess.getCurrentQuestion("test", false));
    }

    @Test
    public void submitAnswerOutOfGameTest() {
        SessionController sess = new SessionController(repo);
        assertFalse(sess.submitAnswer("test",0,0));
    }

    @Test
    public void submitAnswerInvalidAnswerFormatTest() {
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(false,"test",repo.activities);
        Session x = (Session)SessionContainer.getSession(SessionContainer.findUserSession("test"));
        x.startGame();
        x.getCurrentQuestion();
        assertFalse(sess.submitAnswer("test",10,5));
    }

    @Test
    public void submitAnswerNotStartedTest() {
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(false,"test",repo.activities);
        assertFalse(sess.submitAnswer("test",2,0));
    }

    @Test
    public void submitAnswerNotCurrentQuestionTest() {
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(false,"test",repo.activities);
        Session x = (Session)SessionContainer.getSession(SessionContainer.findUserSession("test"));
        x.startGame();
        x.getCurrentQuestion();
        assertFalse(sess.submitAnswer("test",2,1));
    }

    @Test
    public void submitAnswerTest() {
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(false,"test",repo.activities);
        Session x = (Session)SessionContainer.getSession(SessionContainer.findUserSession("test"));
        x.startGame();
        x.getCurrentQuestion();
        assertTrue(sess.submitAnswer("test",2,0));
    }

    @Test
    public void submitAnswerTimedOutTest() {
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(false,"test",repo.activities);
        Session x = (Session)SessionContainer.getSession(SessionContainer.findUserSession("test"));
        x.startGame();
        x.getCurrentQuestion();
        x.setQuestionStartedAt(Long.valueOf(0));
        assertFalse(sess.submitAnswer("test",2,0));
    }

    @Test
    public void addJokerNoSessionTest() {
        SessionController sess = new SessionController(repo);
        assertFalse(sess.addJoker("test",0,0));
    }

    @Test
    public void addJokerNoStartedTest() {
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(false,"test",repo.activities);
        Session x = (Session)SessionContainer.getSession(SessionContainer.findUserSession("test"));
        assertFalse(sess.addJoker("test",0,-1));
    }

    @Test
    public void addJokerWrongQuestionNumTest() {
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(false,"test",repo.activities);
        Session x = (Session)SessionContainer.getSession(SessionContainer.findUserSession("test"));
        x.startGame();
        x.getCurrentQuestion();
        assertFalse(sess.addJoker("test",0,5));
    }


    @Test
    public void addJokerTest() {
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(false,"test",repo.activities);
        Session x = (Session)SessionContainer.getSession(SessionContainer.findUserSession("test"));
        x.startGame();
        x.getCurrentQuestion();
        assertTrue(sess.addJoker("test",1,0));
    }

    @Test
    public void leaveSessionNotFoundTest() {
        SessionController sess = new SessionController(repo);
        assertFalse(sess.leaveSession("test"));
    }

    @Test
    public void leaveSessionWithEnding() {
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(false,"test", repo.activities);
        Session x = SessionContainer.getSession(SessionContainer.findUserSession("test"));
        assertTrue(sess.leaveSession("test"));
        assertTrue(x.hasEnded());
    }
    @Test
    public void validUsernameTest() {
        SessionController sess = new SessionController(repo);
        List<Session> sessionList = new ArrayList<>();
        sessionList.add(null);
        List<Activity> activities = repo.activities;
        SessionContainer.setSessionList(sessionList);
        assertTrue(SessionContainer.createSession(false,"test",activities));
        assertTrue(sess.isUsernameValid("test2"));
        assertFalse(sess.isUsernameValid("test"));
    }

    @Test
    public void joinSessionNotFoundTest() {
        SessionController sess = new SessionController(repo);
        assertTrue(sess.joinSession("test"));
    }

    @Test
    public void joinSessionAlreadyInTest() {
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(false,"test",repo.activities);
        assertFalse(sess.joinSession("test"));
    }

    @Test
    public void joinSessionAvailableFoundTest() {
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(true,"test",repo.activities);
        assertTrue(sess.joinSession("test3"));
    }

    @Test
    public void joinSessionNotAvailableFoundTest() {
        SessionController sess = new SessionController(repo);
        assertTrue(sess.joinSession("test3"));
    }

    @Test
    public void startSessionNotFoundTest() {
        SessionController sess = new SessionController(repo);
        assertFalse(sess.startSession("test"));
    }

    @Test
    public void startSessionNotEnoughTest() {
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(true,"test",repo.activities);
        assertFalse(sess.startSession("test"));
    }

    @Test
    public void startSessionSuccessTest() {
        SessionController sess = new SessionController(repo);
        SessionContainer.createSession(false,"test",repo.activities);
        assertTrue(sess.startSession("test"));
    }


}
