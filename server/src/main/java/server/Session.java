package server;

import commons.Activity;
import commons.Answer;
import commons.QuizzQuestion;
import commons.QuizzQuestionServerParsed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Session {

    private static final int playerLimit = 20; //To be determined
    private List<String> playerList;
    private boolean started;
    private boolean ended;
    private String gameAdmin;
    private boolean gameType; //0 for singleplayer || 1 for multiplayer
    private List<QuizzQuestion> questions;
    private List<Answer> answers;
    private int currentQuestion;
    private long questionStartedAt;

    public Session(boolean gameType) {

        this.playerList = new ArrayList<String>();
        this.started = false;
        this.ended = false;
        this.gameAdmin = null;
        this.gameType = gameType;
        this.questions = new ArrayList<QuizzQuestion>();
        this.answers = new ArrayList<Answer>();
        this.currentQuestion = -1;
        this.questionStartedAt = -1;

        this.generateTestQuestions(); //Temporary until we construct function that generates random question set
    }

    private void generateTestQuestions() {
        QuizzQuestion q1 = new QuizzQuestion("This is test question", new Activity("abc", "abc", "abc", 10L, "abc"), new Activity("bac", "bac", "bac", 10L, "bac"), new Activity("cab", "cab", "cab", 10l, "cab"));
        QuizzQuestion q2 = new QuizzQuestion("This is test question2", new Activity("adfabc", "abc", "abc", 10L, "abc"), new Activity("bac", "bac", "bac", 10L, "bac"), new Activity("cab", "cab", "cab", 10l, "cab"));
        QuizzQuestion q3 = new QuizzQuestion("This is test question3", new Activity("a3bc", "abc", "abc", 10L, "abc"), new Activity("bac", "bac", "bac", 10L, "bac"), new Activity("cab", "cab", "cab", 10l, "cab"));
        QuizzQuestion q4 = new QuizzQuestion("This is test question4", new Activity("a2bc", "abc", "abc", 10L, "abc"), new Activity("bac", "bac", "bac", 10L, "bac"), new Activity("cab", "cab", "cab", 10l, "cab"));
        QuizzQuestion q5 = new QuizzQuestion("This is test question5", new Activity("a7bc", "abc", "abc", 10L, "abc"), new Activity("bac", "bac", "bac", 10L, "bac"), new Activity("cab", "cab", "cab", 10l, "cab"));
        QuizzQuestion q6 = new QuizzQuestion("This is test question76", new Activity("abc", "abc", "abc", 10L, "abc"), new Activity("bac", "bac", "bac", 10L, "bac"), new Activity("cab", "cab", "cab", 10l, "cab"));
        QuizzQuestion q7 = new QuizzQuestion("This is test question8", new Activity("abc", "abc", "abc", 10L, "abc"), new Activity("bac", "bac", "bac", 10L, "bac"), new Activity("cab", "cab", "cab", 10l, "cab"));
        QuizzQuestion q8= new QuizzQuestion("This is test question8", new Activity("abc", "abc", "abc", 10L, "abc"), new Activity("bac", "bac", "bac", 10L, "bac"), new Activity("cab", "cab", "cab", 10l, "cab"));
        QuizzQuestion q9 = new QuizzQuestion("This is test questio66n", new Activity("abc", "abc", "abc", 10L, "abc"), new Activity("bac", "bac", "bac", 10L, "bac"), new Activity("cab", "cab", "cab", 10l, "cab"));
        QuizzQuestion q10 = new QuizzQuestion("This is test question6", new Activity("abc", "abc", "abc", 10L, "abc"), new Activity("bac", "bac", "bac", 10L, "bac"), new Activity("cab", "cab", "cab", 10l, "cab"));
        this.questions.add(q1);
        this.questions.add(q2);
        this.questions.add(q3);
        this.questions.add(q4);
        this.questions.add(q5);
        this.questions.add(q6);
        this.questions.add(q7);
        this.questions.add(q8);
        this.questions.add(q9);
        this.questions.add(q10);
    }

    public boolean haveEveryoneAnswered() {
        int playerNum = playerList.size();
        int answersNum = 0;
        for (Answer x : answers) {
            if (x.getQuestionNum() == this.currentQuestion) answersNum++;
        }

        return answersNum == playerNum;
    }

    public QuizzQuestionServerParsed getCurrentQuestion() {
        if (!this.started) return null;

        //If there have already been 20 questions, end the game
        if(currentQuestion >= 20) {
            this.endGame();
            return null;
        }

        Date date = new Date();
        //If everyone has answered that question OR this is first question OR time has passed then get new question
        if(this.haveEveryoneAnswered() || questionStartedAt == -1 || date.getTime()-questionStartedAt >= 20000) {
            this.currentQuestion++;
            this.questionStartedAt = date.getTime();
        }


        return new QuizzQuestionServerParsed(this.questions.get(currentQuestion),this.questionStartedAt,this.currentQuestion);
    }

    /**
     * @param p - Player to be added
     * @return boolean value whether addition of player is possible
     */
    public boolean isAvailable(String p) {
        //Game is full OR player is already in game OR has started
        if (this.playerList.size() >= Session.playerLimit ||
                this.playerList.contains(p) || this.started) return false;

        //Attempting to add another player to singleplayer
        if (!this.gameType && this.playerList.size() == 1) return false;

        return true;
    }

    /**
     * @param p - Player to be added to game
     * @return boolean value whether operation of addition was successful
     */
    public boolean addPlayer(String p) {
        if (!this.isAvailable(p)) return false;

        //Setting up gameAdmin
        if (this.playerList.size() == 0) {
            this.gameAdmin = p;
        }

        this.playerList.add(p);
        return true;
    }

    /**
     * Adds player answer
     *
     * @param x - Answer object
     * @return boolean value whether operation of addition was successful
     */
    public boolean addAnswer(Answer x) {
        if(x == null) return false; //If null object

        //Is player who submits an answer member of the session
        //If answer is submitted to other question than current
        if(!this.isPlayerInSession(x.getNickname()) || currentQuestion != x.getQuestionNum()) return false;


        //If answer has already been submitted
        for(Answer ans : this.answers) {
            if(ans.getQuestionNum() == x.getQuestionNum() && ans.getNickname().equals(x.getNickname())) {
                return false;
            }
        }

        this.answers.add(x);
        return true;
    }

    /**
     * @param p - Player to be removed from game
     * @return Boolean value depending on whether deletion operation was successful
     */
    public boolean removePlayer(String p) {
        if (this.playerList.size() == 0 || !playerList.contains(p)) return false;

        playerList.remove(p); //Remove player from playerList

        if (gameAdmin.equals(p)) { //If player is the game's admin
            if (playerList.size() == 0) gameAdmin = null;
            else gameAdmin = playerList.get(0);
        }

        return true;
    }

    /**
     * @param x - Player to find for
     * @return Boolean value determining whether player is inside this session
     */
    public boolean isPlayerInSession(String x) {
        if (x == null) return false;

        return this.playerList.contains(x);
    }

    public boolean hasEnded() {
        return ended;
    }

    public int getPlayerNum() {
        return this.playerList.size();
    }

    public void startGame() {
        this.started = true;
    }

    public void endGame() {
        this.started = false;
    }

    public List<String> getPlayerList() {
        return playerList;
    }

    public boolean isStarted() {
        return started;
    }

    public String getGameAdmin() {
        return gameAdmin;
    }

    public boolean isGameType() {
        return gameType;
    }

    public int getCurrentQuestionNum() {
        return currentQuestion;
    }

    public List<QuizzQuestion> getQuestions() {
        return this.questions;
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }

    public long getQuestionStartedAt() {
        return this.questionStartedAt;
    }

    @Override
    public String toString() {
        return "Session{" +
                "playerList=" + playerList +
                ", started=" + started +
                ", gameAdmin=" + gameAdmin +
                ", gameType=" + gameType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return started == session.started && gameType == session.gameType && Objects.equals(playerList, session.playerList) && Objects.equals(gameAdmin, session.gameAdmin);
    }
}
