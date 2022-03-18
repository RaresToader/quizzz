package server;

import commons.Activity;
import commons.Answer;
import commons.QuizzQuestionServerParsed;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityRepository;

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
    @GetMapping("/session/question/{nickname}")
    public QuizzQuestionServerParsed getCurrentQuestion(@PathVariable("nickname") String nickname) {
        int session = SessionContainer.findUserSession(nickname);
        if(session == -1) { //If not session provided for that user yet
            SessionContainer.createSession(false,nickname,this.get60RandomActivities()); //TODO It is: provide that implementation for multiplayer too
        }

        int sessionId = SessionContainer.findUserSession(nickname);
        //System.out.println("For "+nickname+" it is "+sessionId); //DEBUG LINE
        Session x = SessionContainer.getSession(sessionId);

        if(x.hasEnded()) {
            return Session.emptyQ;
        }

        if(!x.isStarted()) { //If game is not started
            x.startGame();
        }

        return x.getCurrentQuestion();
    }

    /**
     * Controller for submitting answer to current question
     * @param nickname - user submitting the answer
     * @param answer - answer submitted
     * @param questionNum - number of question the answer is submitted to
     * @return boolean value depeneding on whether the operation was successful
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
        //If question submitted 30 seconds or more after init of question
        if(date.getTime() - x.getQuestionStartedAt() > 20000) {
            return false;
        }

        return x.addAnswer(new Answer(nickname,answer,questionNum));
    }

    public List<Activity> get60RandomActivities() {
        List<Activity> list = this.activityRepository.findAll();
        if (list.size() < 60)
            return null;
        Collections.shuffle(list);
        return list.subList(0, 60);
    }

}
