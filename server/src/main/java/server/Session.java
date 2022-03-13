package server;

import commons.Activity;
import commons.Answer;
import commons.QuizzQuestion;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private LocalDate questionStartedAt;

    public Session(boolean gameType) {
        this.playerList        = new ArrayList<String>();
        this.started           = false;
        this.ended             = false;
        this.gameAdmin         = null;
        this.gameType          = gameType;
        this.questions         = new ArrayList<QuizzQuestion>();
        this.answers           = new ArrayList<Answer>();
        this.currentQuestion   = -1;
        this.questionStartedAt = LocalDate.of(2030,1,1);

        this.generateTestQuestions(); //Temporary until we construct function that generates random question set
    }

    private void generateTestQuestions() {
        QuizzQuestion q1 = new QuizzQuestion("This is test question", new Activity("abc",55,"abc"), new Activity("bac",66,"bac"), new Activity("cab", 566, "cab"));
        this.questions.add(q1);
    }

    public boolean haveEveryoneAnswered() {
        int playerNum = playerList.size();
        int answersNum = 0;
        for(Answer x : answers) {
            if(x.getQuestionNum() == this.currentQuestion) answersNum++;
        }

        return answersNum == playerNum;
    }

    public QuizzQuestion getCurrentQuestion() {
        if(!this.started) return null;

        //If there have already been 20 questions, end the game
        if(currentQuestion >= 20) {
            this.endGame();
            return null;
        }

        //If everyone has answered that question OR this is first question OR time has passed then get new question
        if(this.haveEveryoneAnswered() || questionStartedAt.getYear() == 2030 || Duration.between(questionStartedAt.atStartOfDay(), LocalDate.now().atStartOfDay()).toSeconds() > 20) {
            this.currentQuestion++;
            this.questionStartedAt = LocalDate.now();
        }


        return this.questions.get(currentQuestion);
    }

    /**
     *
     * @param p - Player to be added
     * @return boolean value whether addition of player is possible
     */
    public boolean isAvailable(String p) {
        //Game is full OR player is already in game OR has started
        if(this.playerList.size() >= Session.playerLimit ||
                this.playerList.contains(p) || this.started) return false;

        //Attempting to add another player to singleplayer
        if(!this.gameType && this.playerList.size() == 1) return false;

        return true;
    }

    /**
     * @param p - Player to be added to game
     * @return boolean value whether operation of addition was successful
     */
    public boolean addPlayer(String p) {
        if(!this.isAvailable(p)) return false;

        //Setting up gameAdmin
        if(this.playerList.size() == 0) {
            this.gameAdmin = p;
        }

        this.playerList.add(p);
        return true;
    }

    /**
     * Adds player answer
     * @param x - Answer object
     */
    public void addAnswer(Answer x) {
        if(x == null) return; //If null object

        //Is player who submits an answer member of the session
        //If answer is submitted to other question than current
        if(!this.isPlayerInSession(x.getNickname()) || currentQuestion != x.getQuestionNum()) return;


        //If answer has already been submitted
        for(Answer ans : this.answers) {
            if(ans.getQuestionNum() == x.getQuestionNum() && ans.getNickname().equals(x.getNickname())) {
                return;
            }
        }

        this.answers.add(x);
    }

    /**
     *
     * @param p - Player to be removed from game
     * @return Boolean value depending on whether deletion operation was successful
     */
    public boolean removePlayer(String p) {
        if(this.playerList.size() == 0 || !playerList.contains(p)) return false;

        playerList.remove(p); //Remove player from playerList

        if(gameAdmin.equals(p)) { //If player is the game's admin
            if(playerList.size() == 0) gameAdmin = null;
            else gameAdmin = playerList.get(0);
        }

        return true;
    }

    /**
     *
     * @param x - Player to find for
     * @return Boolean value determining whether player is inside this session
     */
    public boolean isPlayerInSession(String x) {
        if(x == null) return false;

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

    public List<QuizzQuestion> getQuestions() {
        return this.questions;
    }

    public List<Answer> getAnswers() {
        return this.answers;
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
