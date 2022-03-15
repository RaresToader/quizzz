package server;

import commons.QuizzQuestion;
import org.springframework.web.bind.annotation.*;

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
    public QuizzQuestion getCurrentQuestion(@PathVariable("nickname") String nickname) {
        int session = SessionContainer.findUserSession(nickname);
        if (session == -1) { //If not session provided for that user yet
            SessionContainer.createSession(false, nickname); //TODO
        }

        int sessionId = SessionContainer.findUserSession(nickname);
        //System.out.println("For "+nickname+" it is "+sessionId); //DEBUG LINE
        Session x = SessionContainer.getSession(sessionId);

        if(x.hasEnded()) {
            return new QuizzQuestionServerParsed(new QuizzQuestion("0",null,null,null),null,-1);
        }

        if(!x.isStarted()) { //If game is not started
            x.startGame();
        }

        QuizzQuestion retQ = x.getCurrentQuestion();
        if (retQ == null) return new QuizzQuestion("0", null, null, null); //DEBUG line
        else return retQ;
    }

}
