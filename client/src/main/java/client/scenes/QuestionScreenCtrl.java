package client.scenes;

import client.utils.ServerUtils;
import commons.Points;
import commons.QuizzQuestion;
import commons.RandomSelection;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import javax.inject.Inject;

public class QuestionScreenCtrl {

    private final MainCtrl mainCtrl;
    private final RandomSelection selection = new RandomSelection();
    private final ServerUtils serverUtils;
    private Points receivedPoints = new Points();
    private String chosenAnswer;
    private String correctAnswer;
    private int points = 0;
    private QuizzQuestion currQuestion;

    @Inject
    public QuestionScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
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
    private Label firstAnswer;
    @FXML
    private Label secondAnswer;
    @FXML
    private Label thirdAnswer;

    @FXML
    private Pane firstBox;
    @FXML
    private Pane secondBox;
    @FXML
    private Pane thirdBox;

    @FXML
    AnchorPane finalScreen;
    @FXML
    private Label finalScore;


    /**
     * Clicking the first button disables the other buttons and changes
     * the button's background color so the player knows which button he clicked.
     */



    /**
     * Initialise a singerplayer game
     */
    public void initialize() {
        nextDisplay();
    }

    public void nextDisplay() {

        firstAnswer.setText("");
        secondAnswer.setText("");
        thirdAnswer.setText("");

        firstBox.setStyle("-fx-background-color: #CED0CE");
        secondBox.setStyle("-fx-background-color: #CED0CE;");
        thirdBox.setStyle("-fx-background-color: #CED0CE;");


        if(!selection.hasNext()){
            firstActivity.setText("");
            secondActivity.setText("");
            thirdActivity.setText("");
            thirdChoice.setVisible(false);
            this.question.setText("game over!");
            this.finalScreen.setDisable(false);
            this.finalScreen.setVisible(true);
            this.finalScore.setText("You scored " + 0 + "!"); //once score implemented, display here
            Timeline timer = new Timeline(
                    new KeyFrame(Duration.seconds(5),
                            new EventHandler<ActionEvent>() {

                                @Override
                                public void handle(ActionEvent event) {
                                    mainCtrl.showGlobalLeaderboard();
                                }
                            }
                    )
            );
            timer.setCycleCount(1);
            timer.play();
            return;
        }
        currQuestion= selection.next();
        this.question.setText(currQuestion.getQuestion());
        this.firstActivity.setText(currQuestion.getFirstChoice().getTitle());
        this.secondActivity.setText(currQuestion.getSecondChoice().getTitle());
        this.thirdActivity.setText(currQuestion.getThirdChoice().getTitle());


        firstChoice.setDisable(false);
        secondChoice.setDisable(false);
        thirdChoice.setDisable(false);
    }

    /**
     * After clicking a button again, reset its status and
     * make the other buttons clickable again
     */

    public void chooseFirst() {
        chosenAnswer = currQuestion.getFirstChoice().getTitle();

        //firstChoice.setStyle("-fx-background-color: black;");
        /*
            I think the checking part should be done by the server side.
            setBackground(firstChoice, secondChoice, thirdChoice);
         */
        //firstChoice.setOnAction(event -> clickedAgainResetFirst());
        check(firstBox);

    }

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
    public void check(Pane chosenBox)  {

        firstChoice.setDisable(true);
        secondChoice.setDisable(true);
        thirdChoice.setDisable(true);

        correctAnswer = currQuestion.getMostExpensive();
        boolean isRight = chosenAnswer.equals(correctAnswer);
        firstAnswer.setText("this consumes " + currQuestion.getFirstChoice().getConsumption_in_wh() + " watt per hour");
        secondAnswer.setText("this consumes " + currQuestion.getSecondChoice().getConsumption_in_wh() + " watt per hour");
        thirdAnswer.setText("this consumes " + currQuestion.getThirdChoice().getConsumption_in_wh() + " watt per hour");
        if (chosenAnswer.equals(correctAnswer)) {
            question.setText("Yeah, that's right!");
            chosenBox.setStyle("-fx-background-color: green;");
            points += receivedPoints.getPoints(true);
        } else {
            question.setText("That's wrong!");
            if (correctAnswer.equals(currQuestion.getFirstChoice().getTitle())) {
                firstBox.setStyle("-fx-background-color: green");
                secondBox.setStyle("-fx-background-color: red;");
                thirdBox.setStyle("-fx-background-color: red;");
            } else if (correctAnswer.equals(currQuestion.getSecondChoice().getTitle())) {
                firstBox.setStyle("-fx-background-color: red");
                secondBox.setStyle("-fx-background-color: green;");
                thirdBox.setStyle("-fx-background-color: red;");
            } else if (correctAnswer.equals(currQuestion.getThirdChoice().getTitle())) {
                firstBox.setStyle("-fx-background-color: red");
                secondBox.setStyle("-fx-background-color: red;");
                thirdBox.setStyle("-fx-background-color: green;");
            }
        }
        Timeline timer = new Timeline(
                new KeyFrame(Duration.seconds(3),
                        new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                distributePoints(isRight);
                            }
                        }
                )
        );
        timer.setCycleCount(1);
        timer.play();
    }

    public void distributePoints(boolean isRight) {

        nextDisplay();
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
