package server;

import commons.*;

import java.util.*;

public class Session {
    private static final int playerLimit = 20; //To be determined
    private List<String> playerList;
    private HashMap<String,Integer> currentScores;
    private boolean started;
    private boolean ended;
    private String gameAdmin;
    private boolean gameType; //0 for singleplayer || 1 for multiplayer
    private List<Question> questions;
    private List<Answer> answers;
    private List<Joker> usedJokers;
    private int currentQuestion;
    private long questionStartedAt;
    private List<Emoji> emojiList;

    public static QuizzQuestionServerParsed emptyQ = new QuizzQuestionServerParsed(new QuizzQuestion("0",new Activity("0","0","0",Long.valueOf(0),"0"),new Activity("0","0","0",Long.valueOf(0),"0"),new Activity("0","0","0",Long.valueOf(0),"0")),-1,-1, new ArrayList<Joker>());

    public Session(boolean gameType, List<Activity> activities) {
        this.playerList        = new ArrayList<String>();
        this.currentScores     = new HashMap<>();
        this.started           = false;
        this.ended             = false;
        this.gameAdmin         = null;
        this.gameType          = gameType;
        this.questions         = new ArrayList<Question>();
        this.answers           = new ArrayList<Answer>();
        this.usedJokers        = new ArrayList<Joker>();
        this.currentQuestion   = -1;
        this.questionStartedAt = -1;
        this.emojiList         = new ArrayList<Emoji>();
        this.generateTestQuestions(activities);
    }

    private void generateTestQuestions(List<Activity> activities) {
        RandomSelection randS = new RandomSelection(activities);
        questions             = randS.getListOfQuestions();
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
        if (!this.started || this.ended) return Session.emptyQ;

        Date date = new Date();
        //If everyone has answered that question OR this is first question OR time has passed then get new question
        if(this.haveEveryoneAnswered() || questionStartedAt == -1 || date.getTime()-questionStartedAt > 20000) {
            this.currentQuestion++;
            this.questionStartedAt = date.getTime();
        }

        //If there have already been 20 questions, end the game
        if(currentQuestion >= questions.size()) {
            this.endGame();
            return Session.emptyQ;
        }

        List<Joker> jokers = this.getJokersForCurrentQuestion("test");

        return new QuizzQuestionServerParsed(this.questions.get(currentQuestion),this.questionStartedAt,this.currentQuestion,jokers);
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

        this.currentScores.put(x.getNickname(),x.getAnswer());
        this.answers.add(x);
        return true;
    }

    /**
     * Adds joker to current session
     * @param jokerType - type of joker used
     * @param username - user applying joker
     * @param questionNum - question that joker applies to
     * @return Boolean value indicating whether operation was successful
     */
    public boolean addJoker(int jokerType, String username, int questionNum) {
        if(!this.isPlayerInSession(username) || questionNum != this.currentQuestion) return false;

        this.usedJokers.add(new Joker(username,jokerType,questionNum));
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
     * Returns all jokers that apply to current question and weren't applied by that user
     * @param username - User that requests joker list
     * @return List of jokers for given question
     */
    public List<Joker> getJokersForCurrentQuestion(String username) {
        List<Joker> retList = new ArrayList<Joker>();
        for(Joker x : usedJokers) {
            if(x.getQuestionNum() != this.currentQuestion || x.getUsedBy().equals(username)) continue;

            retList.add(x);
        }

        return retList;
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
        this.ended = true;
    }

    /**
     * Adds an Emoij to the list of emoijs in the session.
     * @param emoij - The information about the added emoij (with username and emoijType)
     */
    public void addEmoij(Emoji emoij){
        this.emojiList.add(emoij);
    }

    /**
     * Check if there are expired emoji's in the session emoij-list and only gives the newest emoji of the players and get the list with active emoji's
     * @return List<Emoji> that contains all the 'active' emoji's.
     */
    public List<Emoji> getActiveEmoijList(){
        List<Emoji> activeEmojiList = new ArrayList<Emoji>();
        Date date = new Date();
        int emojiListLength = emojiList.size();
        for (int i = 0; i < emojiListLength; i++){
            if(emojiList.get(i).getStartTimeEmoji() > (date.getTime() - 5000 )){ //emoji's have an expiry time of 5 seconds
                for (int a = 0; a < activeEmojiList.size(); a++){
                    if(emojiList.get(i).getUserApplying() == activeEmojiList.get(a).getUserApplying() &&
                    emojiList.get(i).getStartTimeEmoji() >= activeEmojiList.get(a).getStartTimeEmoji()){
                        activeEmojiList.remove(a);

                    }
                }
                activeEmojiList.add(emojiList.get(i));
            }

        }
        this.emojiList = activeEmojiList;
        return activeEmojiList;
    }

    /**
     * Setter for currentQuestionNumber ONLY FOR TESTING PURPOSES
     */
    public void setCurrentQuestionNum(int currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    /**
     * Setter for questionStartedAt ONLY FOR TESTING PURPOSES
     */
    public void setQuestionStartedAt(Long questionStartedAt) {
        this.questionStartedAt = questionStartedAt;
    }

    public List<String> getPlayerList() {
        return playerList;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isEnded() { return ended; }

    public String getGameAdmin() {
        return gameAdmin;
    }

    public boolean isGameType() {
        return gameType;
    }

    public int getCurrentQuestionNum() {
        return currentQuestion;
    }

    public List<Question> getQuestions() {
        return this.questions;
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }

    public long getQuestionStartedAt() {
        return this.questionStartedAt;
    }

    public List<Joker> getUsedJokers() {
        return this.usedJokers;
    }
    public List<Emoji> getEmojiList(){
        return this.emojiList;
    }

    public HashMap<String, Integer> getCurrentScores() {
        return currentScores;
    }

    public ArrayList<Map.Entry<String,Integer>> getCurrentLeaderboard() {
        return new ArrayList<Map.Entry<String,Integer>>(currentScores.entrySet());
    }

    @Override
    public String toString() {
        return "Session{" +
                "playerList=" + playerList +
                ", started=" + started +
                ", ended=" + ended +
                ", gameAdmin='" + gameAdmin + '\'' +
                ", gameType=" + gameType +
                ", questions=" + questions +
                ", answers=" + answers +
                ", usedJokers=" + usedJokers +
                ", currentQuestion=" + currentQuestion +
                ", questionStartedAt=" + questionStartedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return started == session.started && ended == session.ended && gameType == session.gameType && currentQuestion == session.currentQuestion && questionStartedAt == session.questionStartedAt && Objects.equals(playerList, session.playerList) && Objects.equals(gameAdmin, session.gameAdmin) && Objects.equals(questions, session.questions) && Objects.equals(answers, session.answers) && Objects.equals(usedJokers, session.usedJokers);
    }

}
