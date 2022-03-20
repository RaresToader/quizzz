package client.scenes;

import client.Session;
import client.utils.ServerUtils;
import client.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import commons.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;


public class QuestionScreenCtrl {

    private final MainCtrl mainCtrl;
    private boolean toEnd = false;
    private final ServerUtils serverUtils;
    private QuizzQuestion currQuestion = new QuizzQuestion("Not assigned", null,null,null);
    private String chosenAnswer;
    private String correctAnswer;
    private int points;
    private int totalPoints;
    private Timeline questionTimer = new Timeline(
            new KeyFrame(Duration.seconds(1),
                    new EventHandler<>() {

                        @Override
                        public void handle(ActionEvent event) {
                            timeLeft -= 1;
                            time.setText(timeLeft + " seconds");
                            if (timeLeft == 0) {
                                timeRanOut();
                            }
                        }
                    }
            )
    );
    public int timeLeft;
    private Timer questionUpdateTimer;
    private TimerTask timeBarTimerTask;
    private Timer timeBarTimer;

    @Inject
    public QuestionScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;

        this.timeBarTimerTask = new TimerTask() {
            @Override
            public void run() {
                timeBarFill.setWidth(timeBarFill.getWidth() - 0.48);

            }
        };
    }

    int progress = 0;

    @FXML
    private Label question;

    @FXML
    private Button firstActivity;
    @FXML
    private Button secondActivity;
    @FXML
    private Button thirdActivity;

    @FXML
    private Label firstAnswer;
    @FXML
    private Label secondAnswer;
    @FXML
    private Label thirdAnswer;

    @FXML
    private Rectangle timeBarBackground;
    @FXML
    private Rectangle timeBarFill;
    @FXML
    private Label time;
    @FXML
    private Label pointCounter;

    /**
     * Initialise a singleplayer game
     */
    public void init() {
        restartTimer();

        questionUpdateTimer = new Timer();
        questionUpdateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        QuizzQuestionServerParsed quizzQuestionServerParsed = Utils.getCurrentQuestion(); //gathers current question
                        //System.out.println(quizzQuestionServerParsed); //DEBUG LINE

                        if(quizzQuestionServerParsed.equals(Session.emptyQ)) { //If gathered question is equal to empty Question
                            questionUpdateTimer.cancel();
                            toEnd = true;
                        } else {
                            QuizzQuestion newQuestion = quizzQuestionServerParsed.getQuestion();
                            Session.setQuestionNum(quizzQuestionServerParsed.getQuestionNum());

                            if(!newQuestion.equals(currQuestion)) {
                                currQuestion = newQuestion;
                                if(Session.getQuestionNum() == 0) {
                                    setNewQuestion();
                                }
                            }
                        }
                        System.out.println(Session.getQuestionNum());
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });

            }
        }, 0, 1000);
    }

    /**
     * checks if the game is over and if not display the next question and restarts the timer.
     */
    public void nextDisplay() {
        if(toEnd) {
            endOfGame();
            return;
        }
        setNewQuestion();
        restartTimer();
    }

    /**
     * display the next question
     */
    public void setNewQuestion(){

        progress+=1;

        question.setText(progress + ". " + currQuestion.getQuestion());
        firstActivity.setText(currQuestion.getFirstChoice().getTitle());
        secondActivity.setText(currQuestion.getSecondChoice().getTitle());
        thirdActivity.setText(currQuestion.getThirdChoice().getTitle());

        firstAnswer.setText("");
        secondAnswer.setText("");
        thirdAnswer.setText("");

        firstActivity.setStyle("-fx-background-color: #CED0CE;");
        secondActivity.setStyle("-fx-background-color: #CED0CE;");
        thirdActivity.setStyle("-fx-background-color: #CED0CE;");

        firstActivity.setDisable(false);
        secondActivity.setDisable(false);
        thirdActivity.setDisable(false);
    }

    /**
     * restarts the timer
     */
    public void restartTimer(){
        timeLeft = 20;
        time.setText(timeLeft + " seconds");
        questionTimer.pause();
        questionTimer = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> {
                            timeLeft-=1;
                            time.setText(timeLeft + " seconds");
                            if(timeLeft == 0){
                                timeRanOut();
                            }
                        }
                )
        );
        questionTimer.setCycleCount(20);
        questionTimer.play();

        timeBarFill.setWidth(timeBarBackground.getWidth());
        timeBarTimerTask = new TimerTask() {
            @Override
            public void run() {
                timeBarFill.setWidth(timeBarFill.getWidth() - 0.48);

            }
        };

        if (timeBarTimer != null) {
            timeBarTimer.cancel();
            timeBarTimer.purge();
        }
        timeBarTimer = new Timer();
        timeBarTimer.scheduleAtFixedRate(timeBarTimerTask, 0, 10L);
    }

    /**
     * handles when the time runs out
     */
    public void timeRanOut(){
        question.setText("Time ran out!");
        //wrongAnswer();
        transition();

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
        check(firstActivity);

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
        check(secondActivity);
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
        check(thirdActivity);
    }

    /**
     * checks if the answer chosen was the right one, and if so distributes the points. Display the wh for each
     * choice.
     * @param chosenBox box of the answer that was chosen
     */
    public void check(Button chosenBox)  {

        Utils.submitAnswer(0);

        questionTimer.pause();
        points = timeLeft*25 + 500;

        correctAnswer = currQuestion.getMostExpensive();
        firstAnswer.setText("this consumes " + currQuestion.getFirstChoice().getConsumption_in_wh() + " watt per hour");
        secondAnswer.setText("this consumes " + currQuestion.getSecondChoice().getConsumption_in_wh() + " watt per hour");
        thirdAnswer.setText("this consumes " + currQuestion.getThirdChoice().getConsumption_in_wh() + " watt per hour");
        if (chosenAnswer.equals(correctAnswer)) {
            question.setText("Yeah, that's right!");
            chosenBox.setStyle("-fx-background-color: green;");
            totalPoints += points;
            pointCounter.setText("current points: " + totalPoints);
        } else {
            question.setText("That's wrong!");
            wrongAnswer();
        }
        transition();
    }

    /**
     * handles the display when the chosen answer was not the right answer.
     */
    public void wrongAnswer(){
        if (correctAnswer.equals(currQuestion.getFirstChoice().getTitle())) {
            firstActivity.setStyle("-fx-background-color: green;");
            secondActivity.setStyle("-fx-background-color: red;");
            thirdActivity.setStyle("-fx-background-color: red;");
        } else if (correctAnswer.equals(currQuestion.getSecondChoice().getTitle())) {
            firstActivity.setStyle("-fx-background-color: red;");
            secondActivity.setStyle("-fx-background-color: green;");
            thirdActivity.setStyle("-fx-background-color: red;");
        } else if (correctAnswer.equals(currQuestion.getThirdChoice().getTitle())) {
            firstActivity.setStyle("-fx-background-color: red;");
            secondActivity.setStyle("-fx-background-color: red;");
            thirdActivity.setStyle("-fx-background-color: green;");
        }
    }

    /**
     * handles the transition between two questions.
     */
    public void transition(){
        firstActivity.setDisable(true);
        firstAnswer.setOpacity(1);

        secondActivity.setDisable(true);
        secondAnswer.setOpacity(1);

        thirdActivity.setDisable(true);
        thirdAnswer.setOpacity(1);

        timeBarTimer.cancel();

        Timeline timer = new Timeline(
                new KeyFrame(Duration.seconds(3),
                        event -> nextDisplay()
                )
        );
        timer.setCycleCount(1);
        timer.play();
    }

    /**
     * handles the end of a game.
     */
    public void endOfGame(){
        questionTimer.pause();
        timeBarTimer.cancel();
        timeBarTimer.purge();
        Player player = serverUtils.getPlayer(Session.getNickname());
        if(player.getScore()<totalPoints){
            serverUtils.updatePlayerInRepo(Session.getNickname(),totalPoints);
        }
        firstActivity.setVisible(false);
        secondActivity.setVisible(false);
        thirdActivity.setVisible(false);
        time.setVisible(false);
        timeBarBackground.setVisible(false);
        timeBarFill.setVisible(false);
        question.setText("Game Over!");
        pointCounter.setText("You scored " + totalPoints + "!"); //once score implemented, display here
        Timeline timer = new Timeline(
                new KeyFrame(Duration.seconds(5),
                        event -> {

                            mainCtrl.showGlobalLeaderboard(false);
                            firstActivity.setVisible(true);
                            secondActivity.setVisible(true);
                            thirdActivity.setVisible(true);
                            totalPoints = 0;
                            pointCounter.setText("current points: " + totalPoints);
                        }
                )
        );
        timer.setCycleCount(1);
        timer.play();
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
