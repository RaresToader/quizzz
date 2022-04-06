package server;

import commons.*;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityRepository;

import java.util.*;

@RestController
public class SessionController {

    private final ActivityRepository activityRepository;

    public SessionController(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /**
     * Controller for getting number of active players
     */
    @GetMapping("/session/activeplayers")
    public int getActivePlayers() {
        return SessionContainer.getNumOfActivePlayers();
    }

    @GetMapping("/session/validusername/{username}")
    public boolean isUsernameValid(@PathVariable("username") String username) {
        return SessionContainer.isUsernameValid(username);
    }

    /**
     * Controller for creating/joining multiplayer session
     * @param nickname - nickname of user creating the request
     * @return Boolean value whether the operation was successful or not
     */
    @GetMapping("/session/joinsession/{nickname}")
    public boolean joinSession(@PathVariable("nickname") String nickname) {
        int session = SessionContainer.findUserSession(nickname);
        if(session != -1) return false; //User is already in session

        int foundSess = SessionContainer.findAvailableSession(nickname);
        if(foundSess != -1) return true; //Available session was found

        SessionContainer.createSession(true,nickname,this.activityRepository.findAll()); //Create session
        return true;
    }

    /**
     * Controller for starting multiplayer game by gameAdmin
     * @param nickname - nickname of user creating the request
     * @return Boolean value whether the operation was successful or not
     */
    @GetMapping("/session/startsession/{nickname}")
    public boolean startSession(@PathVariable("nickname") String nickname) {
        int session = SessionContainer.findUserSession(nickname);
        if(session == -1) return false; //There's no session to start

        Session x = SessionContainer.getSession(session);

        //If game is started OR players is not game admin OR (it is multiplayer AND number of players is <=1)
        if(x.isStarted() || !x.getGameAdmin().equals(nickname) || (x.isGameType() && x.getPlayerList().size() <=1)) {
            return false;
        }

        x.startGame();
        return true;
    }

    /**
     * Controller for getting current question
     *
     * @param nickname - nickname of the user creating the request
     */
    @GetMapping("/session/question/{nickname}/{sessionType}")
    public QuizzQuestionServerParsed getCurrentQuestion(@PathVariable("nickname") String nickname, @PathVariable("sessionType") boolean sessionType) {
        int session = SessionContainer.findUserSession(nickname);
        if(session == -1) { //If not session provided for that user yet
            SessionContainer.createSession(sessionType,nickname,activityRepository.findAll());
        }

        int sessionId = SessionContainer.findUserSession(nickname);
        //System.out.println("For "+nickname+" it is "+sessionId); //DEBUG LINE
        Session x = SessionContainer.getSession(sessionId);

        if(!x.isStarted()) { //If game is not started
            x.startGame();
        }

        return x.getCurrentQuestion();
    }

    /**
     * Controller for getting the list of players in the session the user is in
     * @param nickname - user asking for the list of players
     * @return List<String> containing all the players in that session
     */
    @GetMapping("/session/playersinsession/{nickname}")
    public List<String> getPlayersInSession(@PathVariable("nickname") String nickname) {
        List<String> playerList = new ArrayList<String>();
        int sessionId = SessionContainer.findUserSession(nickname);
        if(sessionId == -1) { //if there is no session yet, the player is the only one in the session.
            playerList.add(nickname);
        }
        else {
            Session session = SessionContainer.getSession(sessionId);
            playerList = session.getPlayerList();
        }
        return playerList;
    }

    @GetMapping("/session/currentleaderboard/{nickname}")
    public List<Map.Entry<String,Integer>> getCurrentLeaderboard(@PathVariable("nickname") String nickname) {
        List<Map.Entry<String,Integer>> currentLeaderboard = new ArrayList<>();
        int sessionId = SessionContainer.findUserSession(nickname);
        if(sessionId == -1) { //if there is no session
            return new ArrayList<>();
        }
        else {
            Session session = SessionContainer.getSession(sessionId);
            currentLeaderboard = session.getCurrentLeaderboard();
        }
        return currentLeaderboard;
    }

    /**
     * Sets the chosen emoji in the list with emoji's of the session the player is in
     * @param nickname - nickname of the user who chose the emoji
     * @param emojiType - type of emoji the user chose.
     * @return boolean value whether operation was successful
     */
    @GetMapping("/session/setEmoji/{nickname}/{emojitype}")
    public boolean setEmoji(@PathVariable("nickname") String nickname, @PathVariable("emojitype") String emojiType){
        boolean successful = false;
        int sessionId = SessionContainer.findUserSession(nickname);
        if (sessionId != -1){
            Session session = SessionContainer.getSession(sessionId);
            Emoji emoji = new Emoji(nickname,emojiType);
            session.addEmoij(emoji);
            successful = true;
        }
        return successful;
    }

    /**
     * Gets a list of active emojis, whether game has started and name of gameAdmin
     * @param nickname - nickname of the user doing the request
     * @return SessionLobbyStatus object containing active emojis, gameStarted status and name of gameAdmin
     */
    @GetMapping("/session/getlobbystatus/{nickname}")
    public SessionLobbyStatus getLobbyStatus(@PathVariable("nickname") String nickname) {
        int sessionId = SessionContainer.findUserSession(nickname);
        if(sessionId == -1) return new SessionLobbyStatus(new ArrayList<Emoji>(), false, "none");

        Session session = SessionContainer.getSession(sessionId);
        return new SessionLobbyStatus(session.getActiveEmoijList(),session.isStarted(),session.getGameAdmin());
    }

    /**
     * Controller for submitting answer to current question
     * @param nickname - user submitting the answer
     * @param answer - answer submitted
     * @param questionNum - number of question the answer is submitted to
     * @return boolean value depending on whether the operation was successful
     */
    @GetMapping("/session/answer/{nickname}/{answer}/{questionNum}")
    public boolean submitAnswer(@PathVariable("nickname") String nickname, @PathVariable("answer") int answer, @PathVariable("questionNum") int questionNum) {
        int session = SessionContainer.findUserSession(nickname);

        //User not in the session
        if(session == -1) {
            return false;
        }

        //User submits answer to not current question or for not started session
        Session x = SessionContainer.getSession(session);
        if(!x.isStarted() || questionNum != x.getCurrentQuestionNum() || questionNum == -1) {
            return false;
        }
        //Provided answer is not in correct format


        Date date = new Date();
        //If question submitted 20 seconds or more after init of question
        if(date.getTime() - x.getQuestionStartedAt() > 20000) {
            return false;
        }

        return x.addAnswer(new Answer(nickname,answer,questionNum));
    }

    /**
     * Adds joker to given session
     * @param jokerType - type of joker used
     * @param nickname - user applying joker
     * @param questionNum - question that joker applies to
     * @return Boolean value indicating whether operation was successful
     */
    @GetMapping("/session/addjoker/{nickname}/{jokertype}/{question}")
    public boolean addJoker(@PathVariable("nickname") String nickname, @PathVariable("jokertype") int jokerType, @PathVariable("question") int questionNum) {
        int session = SessionContainer.findUserSession(nickname);

        //User not in the session
        if(session == -1) {
            return false;
        }

        Session x = SessionContainer.getSession(session);
        if(!x.isStarted() || x.hasEnded() || questionNum != x.getCurrentQuestionNum() || questionNum == -1) {
            return false;
        }

        //Wrong jokerType provided
        if(jokerType < 1 || jokerType > 3) {
            return false;
        }

        return x.addJoker(jokerType,nickname,questionNum);
    }

    /**
     * Controller for leaving session
     * @param nickname - user who wants to leave current session
     * @return Boolean value whether operation was successful
     */
    @GetMapping("/session/leavesession/{nickname}")
    public boolean leaveSession(@PathVariable("nickname") String nickname) {
        int session = SessionContainer.findUserSession(nickname);

        //User not in the session
        if(session == -1) {
            return false;
        }

        Session x = SessionContainer.getSession(session);
        if(!x.hasEnded() && x.getPlayerList().size() == 1) { //End game if that was the last player
            x.endGame();
        }

        return x.removePlayer(nickname);
    }


    /**
     * !!!ONLY FOR TESTING PURPOSES !!!
     * Controller for displaying all the data regarding user's current session
     * @param nickname - user requesting data
     * @return String containing all the session data
     */
    @GetMapping("/session/display/{nickname}")
    public String displayAllSessionDetails(@PathVariable("nickname") String nickname) {
        int session = SessionContainer.findUserSession(nickname);

        if(session == -1) return "NO SESSION";

        Session x = SessionContainer.getSession(session);
        return x.toString();
    }


    /**
     * Produces list of 60 random activities from activityBank
     * @return List<Activity> of size 60
     */
    public List<Activity> get60RandomActivities() {
        List<Activity> list = this.activityRepository.findAll();
        if (list.size() < 60)
            return null;
        Collections.shuffle(list);
        return list.subList(0, 60);
    }

}
