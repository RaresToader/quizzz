package client.scenes;

import client.utils.ServerUtils;
import client.utils.Utils;
import commons.Points;
import commons.QuizzQuestion;
import commons.RandomSelection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javax.inject.Inject;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class QuestionScreenCtrl {

    private final ServerUtils serverUtils;
    private String currQuestion = "";
    private int questionNo = 0;
    private Points receivedPoints = new Points();
    private String chosenAnswer;
    private String correctAnswer;
    private int points = 0;

    @Inject
    public QuestionScreenCtrl(ServerUtils server) {
        this.serverUtils = server;

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        String newQuestion = Utils.getCurrentQuestion();
                        if(!newQuestion.equals(currQuestion)){
                            questionNo += 1;
                            currQuestion = newQuestion;
                        }

                        if(questionNo > 20){
                            timer.cancel();
                        }
                    }
                }, 0, 1000
        );

        this.correctAnswer = server.getCorrect();
    }

    @FXML
    private Button firstChoice;

    @FXML
    private Button secondChoice;

    @FXML
    private Button thirdChoice;

    @FXML
    private Label question;

    /**
     * Clicking the first button disables the other buttons and changes
     * the button's background color so the player knows which button he clicked.
     */

    public void chooseFirst() {
        chosenAnswer = firstChoice.toString();
        secondChoice.setDisable(true);
        thirdChoice.setDisable(true);
        firstChoice.setStyle("-fx-background-color: black;");
        /*
            I think the checking part should be done by the server side.
            setBackground(firstChoice, secondChoice, thirdChoice);
         */
        firstChoice.setOnAction(event -> clickedAgainResetFirst());

    }

    /**
     * Initialise a singerplayer game
     */
    public void init() {
        List<QuizzQuestion> list = new RandomSelection(serverUtils.get60RandomActivities()).getListOfQuestions();
    }

    /**
     * After clicking a button again, reset its status and
     * make the other buttons clickable again
     */
    public void clickedAgainResetFirst() {

        firstChoice.setStyle("-fx-background-color: #474747#474747");
        thirdChoice.setDisable(false);
        secondChoice.setDisable(false);
        firstChoice.setOnAction(e -> chooseFirst());
    }

    /**
     * Works the same way as for the first button
     */
    public void chooseSecond() {
        chosenAnswer = secondChoice.toString();
        firstChoice.setDisable(true);
        thirdChoice.setDisable(true);
        secondChoice.setStyle("-fx-background-color: black;");
        secondChoice.setOnAction(e -> clickedAgainResetSecond());
        /**
         * I think this should be done in the server side, and in a slightly different way.
         */
        // setBackground(secondChoice, firstChoice, thirdChoice);
    }

    /**
     * Works the same way as for the first button
     */
    public void clickedAgainResetSecond() {

        secondChoice.setStyle("-fx-background-color: #474747#474747");
        thirdChoice.setDisable(false);
        firstChoice.setDisable(false);
        secondChoice.setOnAction(e -> chooseSecond());
    }

    /**
     * Works the same as for the previous buttons
     */
    public void chooseThird() {
        chosenAnswer = thirdChoice.toString();
        firstChoice.setDisable(true);
        secondChoice.setDisable(true);
        thirdChoice.setStyle("-fx-background-color: black");
        thirdChoice.setOnAction(e -> clickedAgainResetThird());
        /**
         * I think this should be checked by the server
         */
        //setBackground(thirdChoice, firstChoice, secondChoice);
    }

    public void clickedAgainResetThird() {
        firstChoice.setDisable(false);
        secondChoice.setDisable(false);
        thirdChoice.setStyle("-fx-background-color: #474747#474747");
        thirdChoice.setOnAction(e -> chooseThird());
    }

    /**
     * Changes the color of the background so the player can see if
     * they answered the question correctly
     */
    public void setBackground(Button yourAnswer, Button nextAnswer, Button lastAnswer) {
        if (chosenAnswer.equals(correctAnswer)) {
            yourAnswer.setStyle("-fx-background-color: green;");
            points += receivedPoints.getPoints(true);
        } else {
            yourAnswer.setStyle("-fx-background-color: red;");
            if (correctAnswer.equals(nextAnswer.toString())) {
                nextAnswer.setStyle("-fx-background-color: green;");
            } else {
                lastAnswer.setStyle("-fx-background-color: green;");
            }
        }
    }

    /**
     * Gets the answer chosen by the player
     */
    public String getChosenAnswer() {
        return chosenAnswer;
    }

    /**
     * gets the correct answer to the current question
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * gets the amount of points the player currently has
     */
    public int getPoints() {
        return points;
    }

    /**
     * sets the correct answer to the given string
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

}
