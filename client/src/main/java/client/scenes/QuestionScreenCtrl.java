package client.scenes;

import client.utils.ServerUtils;
import client.utils.Utils;
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
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;


public class QuestionScreenCtrl {

    private final MainCtrl mainCtrl;
    private RandomSelection selection;
    private final ServerUtils serverUtils;
    private QuizzQuestion currQuestion = new QuizzQuestion("Not assigned", null,null,null);
    private int questionNo = 0;
    private Points receivedPoints = new Points();
    private String chosenAnswer;
    private String correctAnswer;
    private int points;
    private int totalPoints;
    private Timeline questionTimer = new Timeline(
            new KeyFrame(Duration.seconds(1),
                    new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            timeLeft-=1;
                            time.setText(Integer.toString(timeLeft));
                            if(timeLeft == 0){
                                timeRanOut();
                            }
                        }
                    }
            )
    );

    @Inject
    public QuestionScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        QuizzQuestion newQuestion = Utils.getCurrentQuestion().getQuestion();
                        if(!newQuestion.equals(currQuestion)){
                            questionNo += 1;
                            currQuestion = newQuestion;

                            question.setText(currQuestion.getQuestion());
                            firstChoice.setText(currQuestion.getFirstChoice().getTitle());
                            secondChoice.setText(currQuestion.getSecondChoice().getTitle());
                            thirdChoice.setText(currQuestion.getThirdChoice().getTitle());

                            correctAnswer = server.getCorrect();
                        }

                        if(questionNo > 20){
                            timer.cancel();
                        }
                    }
                }, 0, 1000
        );
    }

    int progress = 0;
    int timeLeft;

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
    @FXML
    private ProgressBar bar;
    @FXML
    private Label time;
    @FXML
    private Label pointCounter;

    /**
     * Initialise a singerplayer game
     */
    public void init() {
        selection= new RandomSelection();
        nextDisplay();
    }

    /**
     * checks if the game is over and if not display the next question and restarts the timer.
     */
    public void nextDisplay() {




        if(!selection.hasNext()){
            endOfGame();
            return;
        }
        currQuestion= selection.next();
        setNewQuestion();
        restartTimer();

    }

    /**
     * display the next question
     */
    public void setNewQuestion(){

        firstAnswer.setText("");
        secondAnswer.setText("");
        thirdAnswer.setText("");

        progress+=1;
        bar.setProgress(progress*0.05);

        firstBox.setStyle("-fx-background-color: #CED0CE");
        secondBox.setStyle("-fx-background-color: #CED0CE;");
        thirdBox.setStyle("-fx-background-color: #CED0CE;");

        this.question.setText(currQuestion.getQuestion());
        this.firstActivity.setText(currQuestion.getFirstChoice().getTitle());
        this.secondActivity.setText(currQuestion.getSecondChoice().getTitle());
        this.thirdActivity.setText(currQuestion.getThirdChoice().getTitle());

        firstChoice.setDisable(false);
        secondChoice.setDisable(false);
        thirdChoice.setDisable(false);
    }

    /**
     * restarts the timer
     */
    public void restartTimer(){
        timeLeft = 20;
        time.setText(Integer.toString(timeLeft));
        questionTimer.pause();
        questionTimer = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                timeLeft-=1;
                                time.setText(Integer.toString(timeLeft));
                                if(timeLeft == 0){
                                    timeRanOut();
                                }
                            }
                        }
                )
        );
        questionTimer.setCycleCount(20);
        questionTimer.play();
    }

    /**
     * handles when the time runs out
     */
    public void timeRanOut(){
        question.setText("Time ran out!");
        wrongAnswer();
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
     * checks if the answer chosen was the right one, and if so distributes the points. Display the wh for each
     * choice.
     * @param chosenBox box of the answer that was chosen
     */
    public void check(Pane chosenBox)  {

        questionTimer.pause();
        points = timeLeft*25 + 500;

        correctAnswer = currQuestion.getMostExpensive();
        boolean isRight = chosenAnswer.equals(correctAnswer);
        firstAnswer.setText("this consumes " + currQuestion.getFirstChoice().getConsumption_in_wh() + " watt per hour");
        secondAnswer.setText("this consumes " + currQuestion.getSecondChoice().getConsumption_in_wh() + " watt per hour");
        thirdAnswer.setText("this consumes " + currQuestion.getThirdChoice().getConsumption_in_wh() + " watt per hour");
        if (chosenAnswer.equals(correctAnswer)) {
            question.setText("Yeah, that's right!");
            chosenBox.setStyle("-fx-background-color: green;");
            totalPoints += points;
            pointCounter.setText(Integer.toString(totalPoints));
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

    /**
     * handles the transition between two questions.
     */
    public void transition(){
        firstChoice.setDisable(true);
        secondChoice.setDisable(true);
        thirdChoice.setDisable(true);

        Timeline timer = new Timeline(
                new KeyFrame(Duration.seconds(3),
                        new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                nextDisplay();
                            }
                        }
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
        firstBox.setVisible(false);
        //secondBox.setVisible(false);
        thirdBox.setVisible(false);
        thirdChoice.setVisible(false);
        bar.setVisible(false);
        this.question.setText("game over!");
        this.finalScreen.setDisable(false);
        this.finalScreen.setVisible(true);
        this.finalScore.setText("You scored " + totalPoints + "!"); //once score implemented, display here
        Timeline timer = new Timeline(
                new KeyFrame(Duration.seconds(5),
                        new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {

                                mainCtrl.showGlobalLeaderboard(false);
                                thirdChoice.setVisible(true);
                                finalScreen.setDisable(true);
                                finalScreen.setVisible(false);
                            }
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
