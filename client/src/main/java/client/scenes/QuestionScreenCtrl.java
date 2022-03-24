package client.scenes;

import client.Session;
import client.utils.ServerUtils;
import client.utils.Utils;
import commons.*;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;


public class QuestionScreenCtrl {

    private final MainCtrl mainCtrl;
    private boolean toEnd = false;
    private final ServerUtils serverUtils;
    private Question currQuestion = new QuizzQuestion("Not assigned", null,null,null);
    private String chosenAnswer;
    private String correctAnswer;
    private boolean sessionType;
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
    public int transitionTimeLeft;
    private Timer questionUpdateTimer;

    private ScaleTransition timeBarAnimation;
    private ScaleTransition transitionTimerAnimation;

    @Inject
    public QuestionScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
    }

    int progress = 0;

    @FXML
    private Label question;

    @FXML
    private Button activity;

    @FXML
    private ImageView activityImage;

    @FXML
    private Button firstConsump;

    @FXML
    private Button secondConsump;

    @FXML
    private Button thirdConsump;

    @FXML
    private Button firstAnswer;
    @FXML
    private Button secondAnswer;
    @FXML
    private Button thirdAnswer;

    @FXML
    private Label firstAnswerLabel;
    @FXML
    private Label secondAnswerLabel;
    @FXML
    private Label thirdAnswerLabel;

    @FXML
    private Rectangle timeBarBackground;
    @FXML
    private Rectangle timeBarFill;
    @FXML
    private Label time;
    @FXML
    private Label pointCounter;

    @FXML
    private Label transitionTimer;
    @FXML
    private Button confirmButton;
    @FXML
    private Button notConfirmButton;
    @FXML
    private ImageView firstAnswerImage;
    @FXML
    private ImageView secondAnswerImage;
    @FXML
    private ImageView thirdAnswerImage;

    /**
     * Initialise a singleplayer game
     */
    public void init(boolean sessionType) {
        this.sessionType = sessionType;

        restartTimer();
        transitionTimer.setVisible(false);

        questionUpdateTimer = new Timer();
        questionUpdateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            QuizzQuestionServerParsed quizzQuestionServerParsed = Utils.getCurrentQuestion(sessionType); //gathers current question
                            //System.out.println(quizzQuestionServerParsed); //DEBUG LINE

                            if(quizzQuestionServerParsed.equals(Session.emptyQ)) { //If gathered question is equal to empty Question
                                questionUpdateTimer.cancel();
                                toEnd = true;
                            } else {
                                Question newQuestion = quizzQuestionServerParsed.getQuestion();
                                Session.setQuestionNum(quizzQuestionServerParsed.getQuestionNum());

                                if(!newQuestion.equals(currQuestion)) {
                                    currQuestion = newQuestion;
                                    if (Session.getQuestionNum() == 0) {
                                        nextDisplay();
                                    }
                                }
                            }
//                            System.out.println(Session.getQuestionNum()); //DEBUG LINE
                        } catch (org.json.simple.parser.ParseException e) {
                            e.printStackTrace();
                        }
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
        progress += 1;

        if(currQuestion instanceof QuizzQuestion) {
            notConsumpPage();
            showQuizzPage();
            question.setText(progress + ". " + currQuestion.getQuestion());
            firstAnswer.setText(((QuizzQuestion) currQuestion).getFirstChoice().getTitle());
            String path = "/photos/"+((QuizzQuestion) currQuestion).getFirstChoice().getImage_path();
            firstAnswerImage.setImage(new Image(QuestionScreenCtrl.class.getResourceAsStream(path), 300, 300, false, false));
            secondAnswer.setText(((QuizzQuestion) currQuestion).getSecondChoice().getTitle());
            path = "/photos/"+((QuizzQuestion) currQuestion).getSecondChoice().getImage_path();
            secondAnswerImage.setImage(new Image(QuestionScreenCtrl.class.getResourceAsStream(path), 300, 300, false, false));
            thirdAnswer.setText(((QuizzQuestion) currQuestion).getThirdChoice().getTitle());
            path = "/photos/"+((QuizzQuestion) currQuestion).getThirdChoice().getImage_path();
            thirdAnswerImage.setImage(new Image(QuestionScreenCtrl.class.getResourceAsStream(path), 300, 300, false, false));

            firstAnswer.setStyle("-fx-background-color: #CED0CE;");
            secondAnswer.setStyle("-fx-background-color: #CED0CE;");
            thirdAnswer.setStyle("-fx-background-color: #CED0CE;");

            firstAnswer.setDisable(false);
            secondAnswer.setDisable(false);
            thirdAnswer.setDisable(false);
        }

        if(currQuestion instanceof ConsumpQuestion) {
            consumpPage();
            question.setText(progress + ". " + ((ConsumpQuestion) currQuestion).getQuestion());
            activity.setText(((ConsumpQuestion) currQuestion).getActivity().getTitle());
            String path = "/photos/"+((ConsumpQuestion) currQuestion).getActivity().getImage_path();
            activityImage.setImage(new Image(QuestionScreenCtrl.class.getResourceAsStream(path), 300, 300, false, false));
            firstConsump.setText(Long.toString(((ConsumpQuestion) currQuestion).getFirst()));
            secondConsump.setText(Long.toString(((ConsumpQuestion) currQuestion).getSecond()));
            thirdConsump.setText(Long.toString(((ConsumpQuestion) currQuestion).getThird()));

            firstConsump.setStyle("-fx-background-color: #CED0CE;");
            secondConsump.setStyle("-fx-background-color: #CED0CE;");
            thirdConsump.setStyle("-fx-background-color: #CED0CE;");

            firstConsump.setDisable(false);
            secondConsump.setDisable(false);
            thirdConsump.setDisable(false);
        }

        firstAnswerLabel.setText("");
        secondAnswerLabel.setText("");
        thirdAnswerLabel.setText("");

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

        timeBarAnimation = new ScaleTransition(Duration.seconds(20), timeBarFill);
        timeBarAnimation.setFromX(1);
        timeBarAnimation.setToX(0);
        timeBarAnimation.playFromStart();
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
        if(currQuestion instanceof QuizzQuestion) {
            chosenAnswer = ((QuizzQuestion) currQuestion).getFirstChoice().getTitle();
            check(firstAnswer);
        }
        if(currQuestion instanceof ConsumpQuestion) {
            chosenAnswer = Long.toString(((ConsumpQuestion) currQuestion).getFirst());
            check(firstConsump);
        }
        //firstChoice.setStyle("-fx-background-color: black;");
        /*
            I think the checking part should be done by the server side.
            setBackground(firstChoice, secondChoice, thirdChoice);
         */
        //firstChoice.setOnAction(event -> clickedAgainResetFirst());

    }

    /**
     * Works the same way as for the first button
     */
    public void chooseSecond() {
        if(currQuestion instanceof QuizzQuestion) {
            chosenAnswer = ((QuizzQuestion) currQuestion).getSecondChoice().getTitle();
            check(secondAnswer);
        }
        if(currQuestion instanceof ConsumpQuestion) {
            chosenAnswer = Long.toString(((ConsumpQuestion) currQuestion).getSecond());
            check(secondConsump);
        }        //secondChoice.setStyle("-fx-background-color: black;");
        //secondChoice.setOnAction(e -> clickedAgainResetSecond());
        /**
         * I think this should be done in the server side, and in a slightly different way.
         */
        // setBackground(secondChoice, firstChoice, thirdChoice);
    }


    /**
     * Works the same as for the previous buttons
     */
    public void chooseThird() {
        if(currQuestion instanceof QuizzQuestion) {
            chosenAnswer = ((QuizzQuestion) currQuestion).getThirdChoice().getTitle();
            check(thirdAnswer);
        }
        if(currQuestion instanceof ConsumpQuestion) {
            chosenAnswer = Long.toString(((ConsumpQuestion) currQuestion).getThird());
            check(thirdConsump);
        }

        //thirdChoice.setStyle("-fx-background-color: black");
        //thirdChoice.setOnAction(e -> clickedAgainResetThird());
        /**
         * I think this should be checked by the server
         */
        //setBackground(thirdChoice, firstChoice, secondChoice);
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

        if(currQuestion instanceof QuizzQuestion) {
            correctAnswer = ((QuizzQuestion) currQuestion).getMostExpensive();
            firstAnswerLabel.setText("this consumes " + ((QuizzQuestion) currQuestion).getFirstChoice().getConsumption_in_wh() + " watt per hour");
            secondAnswerLabel.setText("this consumes " + ((QuizzQuestion) currQuestion).getSecondChoice().getConsumption_in_wh() + " watt per hour");
            thirdAnswerLabel.setText("this consumes " + ((QuizzQuestion) currQuestion).getThirdChoice().getConsumption_in_wh() + " watt per hour");
        }
        else if(currQuestion instanceof ConsumpQuestion) {
            correctAnswer = ((ConsumpQuestion) currQuestion).getConsump();
        }
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
        String first = "";
        String second = "";
        String third = "";

        if (currQuestion instanceof QuizzQuestion) {
            first = ((QuizzQuestion) currQuestion).getFirstChoice().getTitle();
            second = ((QuizzQuestion) currQuestion).getSecondChoice().getTitle();
            third = ((QuizzQuestion) currQuestion).getThirdChoice().getTitle();

            if (correctAnswer.equals(first)) {
                firstAnswer.setStyle("-fx-background-color: green");
                secondAnswer.setStyle("-fx-background-color: red;");
                thirdAnswer.setStyle("-fx-background-color: red;");
            } else if (correctAnswer.equals(second)) {
                firstAnswer.setStyle("-fx-background-color: red");
                secondAnswer.setStyle("-fx-background-color: green;");
                thirdAnswer.setStyle("-fx-background-color: red;");
            } else if (correctAnswer.equals(third)) {
                firstAnswer.setStyle("-fx-background-color: red");
                secondAnswer.setStyle("-fx-background-color: red;");
                thirdAnswer.setStyle("-fx-background-color: green;");
            }
        }

        if (currQuestion instanceof ConsumpQuestion) {
            first = Long.toString(((ConsumpQuestion) currQuestion).getFirst());
            second = Long.toString(((ConsumpQuestion) currQuestion).getSecond());
            third = Long.toString(((ConsumpQuestion) currQuestion).getThird());

            if (correctAnswer.equals(first)) {
                firstConsump.setStyle("-fx-background-color: green");
                secondConsump.setStyle("-fx-background-color: red;");
                thirdConsump.setStyle("-fx-background-color: red;");
            } else if (correctAnswer.equals(second)) {
                firstConsump.setStyle("-fx-background-color: red");
                secondConsump.setStyle("-fx-background-color: green;");
                thirdConsump.setStyle("-fx-background-color: red;");
            } else if (correctAnswer.equals(third)) {
                firstConsump.setStyle("-fx-background-color: red");
                secondConsump.setStyle("-fx-background-color: red;");
                thirdConsump.setStyle("-fx-background-color: green;");
            }
        }

    }

    /**
     * handles the transition between two questions.
     */
    public void transition(){
        if(currQuestion instanceof QuizzQuestion) {
            firstAnswer.setDisable(true);
            firstAnswerLabel.setOpacity(1);

            secondAnswer.setDisable(true);
            secondAnswerLabel.setOpacity(1);

            thirdAnswer.setDisable(true);
            thirdAnswerLabel.setOpacity(1);
        }

        if(currQuestion instanceof ConsumpQuestion) {
            firstConsump.setDisable(true);
            secondConsump.setDisable(true);
            thirdConsump.setDisable(true);
        }

        timeBarAnimation.pause();

        transitionTimeLeft = 5;
        transitionTimer.setVisible(true);
        transitionTimer.setText(transitionTimeLeft + " seconds until next question!");

        transitionTimerAnimation = new ScaleTransition(Duration.seconds(2), transitionTimer);
        transitionTimerAnimation.setFromX(1);
        transitionTimerAnimation.setToX(0.8);
        transitionTimerAnimation.setFromY(1);
        transitionTimerAnimation.setToY(0.8);
        transitionTimerAnimation.setAutoReverse(true);
        transitionTimerAnimation.setCycleCount(20);
        transitionTimerAnimation.play();

        timeBarFill.setVisible(false);
        timeBarBackground.setVisible(false);
        time.setVisible(false);

        Timeline timer = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> {
//                            System.out.println("transitionTimeLeft = " + transitionTimeLeft); //DEBUG LINE
                    if (transitionTimeLeft == 0) {
                        transitionTimerAnimation.stop();
                        transitionTimer.setOpacity(1);
                        transitionTimer.setVisible(false);
                        timeBarFill.setVisible(true);
                        timeBarBackground.setVisible(true);
                        time.setVisible(true);
                        nextDisplay();
                    }
                    else {
                        transitionTimeLeft -= 1;
                        transitionTimer.setText(transitionTimeLeft + " seconds until next question!");
                    }
                }
                )
        );
        timer.setCycleCount(6);
        timer.play();
    }

    /**
     * handles the end of a game.
     */
    public void endOfGame(){
        questionTimer.pause();
        timeBarAnimation.stop();
        Player player = serverUtils.getPlayer(Session.getNickname());
        if(player.getScore()<totalPoints){
            serverUtils.updatePlayerInRepo(Session.getNickname(),totalPoints);
            transitionTimer.setText("Congratulations! You improved your score!");
        }
        else {
            transitionTimer.setText("You had a higher score before! Try again!");
        }
        notConsumpPage();
        firstAnswer.setStyle("-fx-background-color: #ced0ce;");
        secondAnswer.setStyle("-fx-background-color: #ced0ce;");
        thirdAnswer.setStyle("-fx-background-color: #ced0ce;");
        firstAnswer.setVisible(false);
        secondAnswer.setVisible(false);
        thirdAnswer.setVisible(false);
        firstAnswerLabel.setVisible(false);
        secondAnswerLabel.setVisible(false);
        thirdAnswerLabel.setVisible(false);
        time.setVisible(false);
        timeBarBackground.setVisible(false);
        timeBarFill.setVisible(false);
        transitionTimer.setVisible(true);
        question.setText("Game Over!");
        pointCounter.setText("You scored " + totalPoints + "!"); //once score implemented, display here
        Timeline timer = new Timeline(
                new KeyFrame(Duration.seconds(5),
                        event -> {

                            showQuizzPage();

                            mainCtrl.showGlobalLeaderboard(false);

                            time.setVisible(true);
                            timeBarBackground.setVisible(true);
                            timeBarFill.setVisible(true);
                            transitionTimer.setVisible(true);
                            totalPoints = 0;
                            pointCounter.setText("current points: " + totalPoints);

                            notConsumpPage();

                            confirmButton.setDisable(true);
                            confirmButton.setVisible(false);
                            notConfirmButton.setDisable(true);
                            notConfirmButton.setVisible(false);

                            question.setText(currQuestion.getQuestion());
                        }
                )
        );
        timer.setCycleCount(1);
        timer.play();
    }

    public void showQuizzPage() {
        firstAnswer.setVisible(true);
        secondAnswer.setVisible(true);
        thirdAnswer.setVisible(true);
        firstAnswer.setDisable(false);
        secondAnswer.setDisable(false);
        thirdAnswer.setDisable(false);
        firstAnswerLabel.setVisible(true);
        secondAnswerLabel.setVisible(true);
        thirdAnswerLabel.setVisible(true);
    }

    public void notConsumpPage() {
        firstConsump.setVisible(false);
        firstConsump.setDisable(true);
        secondConsump.setVisible(false);
        secondConsump.setDisable(true);
        thirdConsump.setVisible(false);
        thirdConsump.setDisable(true);
        activityImage.setVisible(false);
        activity.setVisible(false);
    }

    public void consumpPage() {
        firstAnswer.setVisible(false);
        firstAnswer.setDisable(true);
        secondAnswer.setVisible(false);
        secondAnswer.setDisable(true);
        thirdAnswer.setVisible(false);
        thirdAnswer.setDisable(true);
        firstAnswerLabel.setVisible(false);
        secondAnswerLabel.setVisible(false);
        thirdAnswerLabel.setVisible(false);
        firstConsump.setVisible(true);
        firstConsump.setDisable(false);
        secondConsump.setVisible(true);
        secondConsump.setDisable(false);
        thirdConsump.setVisible(true);
        thirdConsump.setDisable(false);
        activityImage.setVisible(true);
        activity.setVisible(true);
    }

    public void confirmPage(){
        notConsumpPage();
        timeBarFill.setVisible(false);
        timeBarBackground.setVisible(false);
        time.setVisible(false);
        firstAnswer.setVisible(false);
        firstAnswer.setDisable(true);
        secondAnswer.setVisible(false);
        secondAnswer.setDisable(true);
        thirdAnswer.setVisible(false);
        thirdAnswer.setDisable(true);
        firstAnswerLabel.setVisible(false);
        secondAnswerLabel.setVisible(false);
        thirdAnswerLabel.setVisible(false);
        transitionTimer.setVisible(true);
        transitionTimer.setText("Are you sure?");
        confirmButton.setDisable(false);
        confirmButton.setVisible(true);
        notConfirmButton.setDisable(false);
        notConfirmButton.setVisible(true);
    }
    public void closeConfirmPage(){
        timeBarFill.setVisible(true);
        timeBarBackground.setVisible(true);
        time.setVisible(true);
        if (currQuestion instanceof QuizzQuestion) {
            firstAnswer.setVisible(true);
            firstAnswer.setDisable(false);
            secondAnswer.setVisible(true);
            secondAnswer.setDisable(false);
            thirdAnswer.setVisible(true);
            thirdAnswer.setDisable(false);
            firstAnswerLabel.setVisible(true);
            secondAnswerLabel.setVisible(true);
            thirdAnswerLabel.setVisible(true);
        }
        else {
            consumpPage();
        }
        transitionTimer.setText("");
        confirmButton.setDisable(true);
        confirmButton.setVisible(false);
        notConfirmButton.setDisable(true);
        notConfirmButton.setVisible(false);
    }
    public void confirmQuit(){
        transitionTimer.setVisible(true);
        transitionTimer.setText("You interrupted the game");
        confirmButton.setVisible(false);
        notConfirmButton.setVisible(false);
        confirmButton.setDisable(true);
        notConfirmButton.setDisable(true);

        endOfGame();
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
