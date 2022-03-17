package server;

import commons.Activity;
import commons.Answer;
import commons.QuizzQuestion;
import commons.QuizzQuestionServerParsed;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class SessionController {

    public SessionController() {
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
            SessionContainer.createSession(false,nickname); //TODO It is: provide that implementation for multiplayer too
        }

        int sessionId = SessionContainer.findUserSession(nickname);
        //System.out.println("For "+nickname+" it is "+sessionId); //DEBUG LINE
        Session x = SessionContainer.getSession(sessionId);

        if(x.hasEnded()) {
            return new QuizzQuestionServerParsed(new QuizzQuestion("0",new Activity(),new Activity(),new Activity()),-1,-1);
        }

        if(!x.isStarted()) { //If game is not started
            x.startGame();
        }

        QuizzQuestionServerParsed retQ = x.getCurrentQuestion();
        if (retQ .equals( new QuizzQuestionServerParsed(new QuizzQuestion("0",new Activity(),new Activity(),new Activity()),-1,-1))) return new QuizzQuestionServerParsed(new QuizzQuestion("0",new Activity(),new Activity(),new Activity()),-1,-1); //DEBUG line
        else return retQ;
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

}
