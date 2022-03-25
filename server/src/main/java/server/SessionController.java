package server;

import commons.Activity;
import commons.Answer;
import commons.Emoji;
import commons.QuizzQuestionServerParsed;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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

    /**
     * Controller for getting current question
     *
     * @param nickname - nickname of the user creating the request
     */
    @GetMapping("/session/question/{nickname}/{sessionType}")
    public QuizzQuestionServerParsed getCurrentQuestion(@PathVariable("nickname") String nickname, @PathVariable("sessionType") boolean sessionType) {
        int session = SessionContainer.findUserSession(nickname);
        if(session == -1) { //If not session provided for that user yet
            SessionContainer.createSession(sessionType,nickname,this.get60RandomActivities());
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

    /**
     * Sets the chosen emoji in the list with emoji's of the session the player is in
     * @param nickname - nickname of the user who chose the emoji
     * @param emojiType - type of emoji the user chose.
     */
    @GetMapping("/session/setEmoji/{nickname}/{emojitype}")
    public void setEmoji(@PathVariable("nickname") String nickname, @PathVariable("emojitype") String emojiType){
        int sessionId = SessionContainer.findUserSession(nickname);
        if (sessionId != -1){
            Session session = SessionContainer.getSession(sessionId);
            Emoji emoji = new Emoji(nickname,emojiType);
            session.addEmoij(emoji);
        }
    }

    /**
     * Gets a list with the active emoji's in the session
     * @param nickname - nickname of the user who asks for the emoji's
     * @return List<Emoji> - list with all emoji's which contain also the name of the user and the emoji-types
     */
    @GetMapping("/session/getActiveSessionEmojis/{nickname}")
    public List<Emoji> getActiveSessionEmojis(@PathVariable("nickname") String nickname){
        List<Emoji> list = new ArrayList<Emoji>();
        int sessionId = SessionContainer.findUserSession(nickname);
        if (sessionId == -1){
            return list;
        }
        else{
            Session session = SessionContainer.getSession(sessionId);
            list = session.getActiveEmoijList();
            return list;
        }
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
        if(answer<0 || answer > 3) {
            return false;
        }

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

        //TODO when we know values of jokerType we need to add validation for those too
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
