package client.scenes;

import client.utils.ServerUtils;
import commons.Points;
import commons.QuizzQuestion;
import commons.RandomSelection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import javax.inject.Inject;
import java.util.List;


public class QuestionScreenCtrl {

    private final ServerUtils serverUtils;
    private Points receivedPoints = new Points();
    private String chosenAnswer;
    private String correctAnswer;
    private int points = 0;
    private QuizzQuestion currQuestion;

    @Inject
    public QuestionScreenCtrl(ServerUtils server) {
        this.serverUtils = server;
    }

    @FXML
    private Button firstChoice;

    @FXML
    private Button secondChoice;

    @FXML
    private Button thirdChoice;

    @FXML
    private Label question;

    @FXML
    private Label firstActivity;
    @FXML
    private Label secondActivity;
    @FXML
    private Label thirdActivity;

    @FXML
    private Pane firstBox;
    @FXML
    private Pane secondBox;
    @FXML
    private Pane thirdBox;

    /**
     * Clicking the first button disables the other buttons and changes
     * the button's background color so the player knows which button he clicked.
     */

    public void chooseFirst() {
        chosenAnswer = currQuestion.getFirstChoice().getTitle();
        secondChoice.setDisable(true);
        thirdChoice.setDisable(true);
        //firstChoice.setStyle("-fx-background-color: black;");
        /*
            I think the checking part should be done by the server side.
            setBackground(firstChoice, secondChoice, thirdChoice);
         */
        //firstChoice.setOnAction(event -> clickedAgainResetFirst());
        check(thirdBox);

    }

    /**
     * Initialise a singerplayer game
     */
    public void initialize() {
        RandomSelection selection = new RandomSelection(); //serverUtils.get60RandomActivities()
        currQuestion= selection.next();
        this.question.setText(currQuestion.getQuestion());
        this.firstActivity.setText(currQuestion.getFirstChoice().getTitle());
        this.secondActivity.setText(currQuestion.getSecondChoice().getTitle());
        this.thirdActivity.setText(currQuestion.getThirdChoice().getTitle());
    }

    public void nextDisplay() {

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
        chosenAnswer = currQuestion.getSecondChoice().getTitle();
        firstChoice.setDisable(true);
        thirdChoice.setDisable(true);
        //secondChoice.setStyle("-fx-background-color: black;");
        //secondChoice.setOnAction(e -> clickedAgainResetSecond());
        /**
         * I think this should be done in the server side, and in a slightly different way.
         */
        // setBackground(secondChoice, firstChoice, thirdChoice);
        check(secondBox);
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
        chosenAnswer = currQuestion.getThirdChoice().getTitle();
        firstChoice.setDisable(true);
        secondChoice.setDisable(true);
        //thirdChoice.setStyle("-fx-background-color: black");
        //thirdChoice.setOnAction(e -> clickedAgainResetThird());
        /**
         * I think this should be checked by the server
         */
        //setBackground(thirdChoice, firstChoice, secondChoice);
        check(thirdBox);
    }

    public void clickedAgainResetThird() {
        firstChoice.setDisable(false);
        secondChoice.setDisable(false);
        //thirdChoice.setStyle("-fx-background-color: #474747#474747");
        //thirdChoice.setOnAction(e -> chooseThird());
    }

    /**
     * Changes the color of the background so the player can see if
     * they answered the question correctly
     */
    public void check(Pane chosenBox) {
        correctAnswer = currQuestion.getMostExpensive();
        boolean isRight = chosenAnswer.equals(correctAnswer);
        if (chosenAnswer.equals(correctAnswer)) {
            chosenBox.setStyle("-fx-background-color: green;");
            points += receivedPoints.getPoints(true);
        } else if(correctAnswer.equals(currQuestion.getFirstChoice().getTitle())){
                firstBox.setStyle("-fx-background-color: green");
                secondBox.setStyle("-fx-background-color: red;");
                thirdBox.setStyle("-fx-background-color: red;");
        } else if(correctAnswer.equals(currQuestion.getSecondChoice().getTitle())) {
            firstBox.setStyle("-fx-background-color: red");
            secondBox.setStyle("-fx-background-color: green;");
            thirdBox.setStyle("-fx-background-color: red;");
        } else if(correctAnswer.equals(currQuestion.getThirdChoice().getTitle())) {
            firstBox.setStyle("-fx-background-color: red");
            secondBox.setStyle("-fx-background-color: red;");
            thirdBox.setStyle("-fx-background-color: green;");
        }
        distributePoints(isRight);
    }

    public static void distributePoints(boolean isRight){

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
