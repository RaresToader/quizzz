package client.scenes;

import client.Session;
import client.utils.ServerUtils;
import client.utils.Utils;
import commons.*;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class QuestionScreenMultiplayerCtrl {

    private final MainCtrl mainCtrl;
    private boolean toEnd = false;
    private final ServerUtils serverUtils;
    private Question displayedQ   = new QuizzQuestion("Not assigned", null, null, null);
    private Question currQuestion = new QuizzQuestion("Not assigned", null, null, null);
    private String chosenAnswer;
    private String correctAnswer;
    private boolean sessionType;
    private int points;
    private int totalPoints;
    private List<Joker> usedJokers;

    private Timeline questionTimer = new Timeline(
            new KeyFrame(Duration.seconds(1),
                    new EventHandler<>() {

                        @Override
                        public void handle(ActionEvent event) {
                            timeLeft -= 1;
                            time.setText(timeLeft + " seconds");
                            if (timeLeft <= 0) {
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

    private SessionLobbyStatus lobbyStatus;
    private ArrayList<Emoji> emojisReceivedThisQuestion;

    private boolean doublePoints;
    private boolean removeAnAnswer;
    private boolean reduceTime;

    private boolean readyForNextQuestion;

    private Timer emoteJokerUpdateTimer;

    @Inject
    public QuestionScreenMultiplayerCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
    }

    @FXML
    private Label question;

    @FXML
    private Button endButton;

    @FXML
    private Label questionNumber;

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
    private TextField guess;

    @FXML
    private Label guessLabel;

    @FXML
    private Button submit;

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

    @FXML
    private Button jokerButton1; // Double Points
    @FXML
    private Button jokerButton2; // 50/50 Remove one wrong answer
    @FXML
    private Button jokerButton3; // Reduce time

    @FXML
    private Button emoteButtonSmile;
    @FXML
    private Button emoteButtonSad;
    @FXML
    private Button emoteButtonAngry;
    @FXML
    private Button emoteButtonSurprise;
    @FXML
    private Button emoteButtonCelebrate;
    @FXML
    private Button emoteButtonSunglasses;

    @FXML
    private ScrollPane chatBox;
    @FXML
    private VBox chatBoxContent;

    /**
     * Initialise a multiplayer game
     */
    public void init(boolean sessionType) {
        usedJokers = new ArrayList<>(); //Clear jokerList

        Session.setQuestionNum(0);
        this.sessionType = sessionType;
        restartTimer();
        transitionTimer.setVisible(false);
        this.pointCounter.getScene().getWindow().setOnCloseRequest(e -> {
            boolean actuallyQuit = confirmQuitGame();
            if(actuallyQuit){
                Utils.leaveSession();
            }
            else{
                e.consume();
            }
        });
        // Reset variables every question
        this.doublePoints = false;
        this.removeAnAnswer = false;
        this.emojisReceivedThisQuestion = new ArrayList<>();

        initialiseEmotesAndJokers();

        questionUpdateTimer = new Timer();
        questionUpdateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            QuizzQuestionServerParsed quizzQuestionServerParsed = Utils.getCurrentQuestion(sessionType); //gathers current question
                            if (quizzQuestionServerParsed.equals(Session.emptyQ)) { //If gathered question is equal to empty Question
                                questionUpdateTimer.cancel();
                                toEnd = true;
                            } else {
                                Date date = new Date();
                                Question newQuestion = quizzQuestionServerParsed.getQuestion();
                                Session.setQuestionNum(quizzQuestionServerParsed.getQuestionNum());

                                //Only update base for timer upon receivement of new question or when reduceTime joker is not applied
                                if(newQuestion.equals(currQuestion) && !readyForNextQuestion && !reduceTime) timeLeft = (int)(20-(date.getTime()-quizzQuestionServerParsed.getStartTime())/1000); //Sync timer with server

                                receiveJokers(quizzQuestionServerParsed.getJokerList());

                                if(!newQuestion.equals(currQuestion)) {
                                    currQuestion = newQuestion;

                                    //Reset jokers on new question
                                    doublePoints   = false;
                                    reduceTime     = false;
                                    removeAnAnswer = false;
                                    if (readyForNextQuestion || Session.getQuestionNum() == 0) {
                                        nextDisplay();
                                    }
                                }
                            }
                        } catch (org.json.simple.parser.ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }, 0, 1000);
    }

    /**
     * When you click 'X' a box is displayed to ask you if
     * you are really sure you want to quit the game
     *
     * @return a boolean, whether the user really wants to quit, or not
     */
    public boolean confirmQuitGame() {
        Stage confirmStage = new Stage();
        confirmStage.initModality(Modality.APPLICATION_MODAL);
        confirmStage.setTitle("Quit");
        confirmStage.setMinWidth(200);
        Label text = new Label();
        text.setText("Are you sure you want to quit?");
        Button confirmQuit = new Button("Yes");
        Button closeQuit = new Button("No");
        confirmQuit.setStyle("-fx-background-color: #f15025;");
        confirmQuit.setStyle("-fx-font-size: 38pt;");
        confirmQuit.setStyle("-fx-line-spacing: -16;");
        confirmQuit.setStyle("-fx-padding: 0;");
        confirmQuit.setStyle("-fx-indent: 0;");
        confirmQuit.setStyle("-fx-end-margin: 0;");
        confirmQuit.setStyle("-fx-start-margin: 0;");
        confirmQuit.setStyle("-fx-background-radius: 22;");
        confirmQuit.setStyle("-fx-cursor: hand;");
        closeQuit.setStyle(confirmQuit.getStyle());
        AtomicBoolean yesOrNo = new AtomicBoolean(false);
        confirmQuit.setOnMouseClicked(e -> {
            yesOrNo.set(true);
            confirmStage.close();
        });
        closeQuit.setOnMouseClicked(e -> {
            yesOrNo.set(false);
            confirmStage.close();
        });
        VBox screenLayout = new VBox(20);
        screenLayout.getChildren().addAll(text, confirmQuit, closeQuit);
        screenLayout.setAlignment(Pos.CENTER);
        Scene theScene = new Scene(screenLayout);
        confirmStage.setScene(theScene);
        confirmStage.showAndWait();
        return yesOrNo.get();

    }

    /**
     * checks if the game is over and if not display the next question and restarts the timer.
     */
    public void nextDisplay() {
        if (toEnd) {
            endOfGame();
            return;
        }

        if(!currQuestion.equals(displayedQ)) {
            setNewQuestion();
            restartTimer();
        } else if(readyForNextQuestion) {
            question.setText("Wait for other players");
        }
    }

    /**
     * display the next question
     */
    public void setNewQuestion() {
        readyForNextQuestion = false;
        displayedQ = currQuestion;
        questionNumber.setText(Session.getQuestionNum()+1 + "/20");
        endButton.setDisable(false);
        questionNumber.setVisible(true);
        if (currQuestion instanceof QuizzQuestion) {
            showQuizzPage();
            initQuizzQuestion();
        } else if (currQuestion instanceof ConsumpQuestion) {
            newConsumpQuestion();
        } else if (currQuestion instanceof InsteadOfQuestion) {
            consumpPage();
            question.setText(((InsteadOfQuestion) currQuestion).getQuestion());
            activity.setText(((InsteadOfQuestion) currQuestion).getPromptActivity().getTitle());
            String path = "/photos/" + ((InsteadOfQuestion) currQuestion).getPromptActivity().getImage_path();
            activityImage.setImage(new Image(Objects.requireNonNull(QuestionScreenMultiplayerCtrl.class.getResourceAsStream(path)), 300, 300, false, false));
            firstConsump.setText(((InsteadOfQuestion) currQuestion).getFirstChoice().getTitle());
            secondConsump.setText(((InsteadOfQuestion) currQuestion).getSecondChoice().getTitle());
            thirdConsump.setText(((InsteadOfQuestion) currQuestion).getThirdChoice().getTitle());
            firstConsump.setStyle("-fx-background-color: #CED0CE;");
            secondConsump.setStyle("-fx-background-color: #CED0CE;");
            thirdConsump.setStyle("-fx-background-color: #CED0CE;");
            firstConsump.setDisable(false);
            secondConsump.setDisable(false);
            thirdConsump.setDisable(false);
            firstConsump.setStyle("-fx-font-size: 10pt;");
            secondConsump.setStyle("-fx-font-size: 10pt;");
            thirdConsump.setStyle("-fx-font-size: 10pt;");
        } else {
            guessPage();
            question.setText(((GuessQuestion) currQuestion).getQuestion());
            activity.setText(((GuessQuestion) currQuestion).getActivity().getTitle());
            String path = "/photos/" + ((GuessQuestion) currQuestion).getActivity().getImage_path();
            activityImage.setImage(new Image(Objects.requireNonNull(QuestionScreenMultiplayerCtrl.class.getResourceAsStream(path)), 300, 300, false, false));
            guess.setText("");
            guess.setDisable(false);
            submit.setDisable(false);
            guessOnlyNum();
            guess.setStyle("-fx-background-color: #CED0CE;");
            guess.setCursor(Cursor.TEXT);
            guess.requestFocus();
            submit.setDefaultButton(true);
        }
        guessLabel.setText("");
        firstAnswerLabel.setText("");
        secondAnswerLabel.setText("");
        thirdAnswerLabel.setText("");
    }

    public void newConsumpQuestion() {
        consumpPage();
        question.setText(((ConsumpQuestion) currQuestion).getQuestion());
        activity.setText(((ConsumpQuestion) currQuestion).getActivity().getTitle());
        String path = "/photos/" + ((ConsumpQuestion) currQuestion).getActivity().getImage_path();
        activityImage.setImage(new Image(Objects.requireNonNull(QuestionScreenMultiplayerCtrl.class.getResourceAsStream(path)), 300, 300, false, false));
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

    /**
     * Sets a new question for the question type "QuizzQuestion"
     */
    public void initQuizzQuestion() {
        question.setText(currQuestion.getQuestion());
        firstAnswer.setText(((QuizzQuestion) currQuestion).getFirstChoice().getTitle());
        String path = "/photos/" + ((QuizzQuestion) currQuestion).getFirstChoice().getImage_path();
        firstAnswerImage.setImage(new Image(Objects.requireNonNull(QuestionScreenMultiplayerCtrl.class.getResourceAsStream(path)), 300, 300, false, false));
        secondAnswer.setText(((QuizzQuestion) currQuestion).getSecondChoice().getTitle());
        path = "/photos/" + ((QuizzQuestion) currQuestion).getSecondChoice().getImage_path();
        secondAnswerImage.setImage(new Image(Objects.requireNonNull(QuestionScreenMultiplayerCtrl.class.getResourceAsStream(path)), 300, 300, false, false));
        thirdAnswer.setText(((QuizzQuestion) currQuestion).getThirdChoice().getTitle());
        path = "/photos/" + ((QuizzQuestion) currQuestion).getThirdChoice().getImage_path();
        thirdAnswerImage.setImage(new Image(Objects.requireNonNull(QuestionScreenMultiplayerCtrl.class.getResourceAsStream(path)), 300, 300, false, false));

        firstAnswer.setStyle("-fx-background-color: #CED0CE;");
        secondAnswer.setStyle("-fx-background-color: #CED0CE;");
        thirdAnswer.setStyle("-fx-background-color: #CED0CE;");

        firstAnswer.setDisable(false);
        secondAnswer.setDisable(false);
        thirdAnswer.setDisable(false);
    }

    /**
     * Makes it so that only numbers can be entered in the guess TextField
     */
    public void guessOnlyNum() {
        guess.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    guess.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }


    /**
     * restarts the timer
     */
    public void restartTimer() {
        timeLeft = 20;
        time.setText(timeLeft + " seconds");
        questionTimer.pause();
        questionTimer = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> {
                            timeLeft -= 1;
                            time.setText(timeLeft + " seconds");
                            if (timeLeft <= 0) {
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
    public void timeRanOut() {
        questionTimer.pause();
        question.setText("Time ran out!");
        if (currQuestion instanceof ConsumpQuestion || currQuestion instanceof QuizzQuestion || currQuestion instanceof InsteadOfQuestion) {
            wrongAnswer();
        } else {
            guess.setStyle("-fx-background-color: red;");
        }
        transition();
    }


    /**
     * After clicking a button again, reset its status and
     * make the other buttons clickable again
     */

    public void chooseFirst() {
        if (currQuestion instanceof QuizzQuestion) {
            chosenAnswer = ((QuizzQuestion) currQuestion).getFirstChoice().getTitle();
            check(firstAnswer);
        }
        if (currQuestion instanceof ConsumpQuestion) {
            chosenAnswer = Long.toString(((ConsumpQuestion) currQuestion).getFirst());
            check(firstConsump);
        }
        if (currQuestion instanceof InsteadOfQuestion) {
            chosenAnswer = ((InsteadOfQuestion) currQuestion).getFirstChoice().toString();
            check(firstConsump);
        }
    }

    /**
     * Works the same way as for the first button
     */
    public void chooseSecond() {
        if (currQuestion instanceof QuizzQuestion) {
            chosenAnswer = ((QuizzQuestion) currQuestion).getSecondChoice().getTitle();
            check(secondAnswer);
        }
        if (currQuestion instanceof ConsumpQuestion) {
            chosenAnswer = Long.toString(((ConsumpQuestion) currQuestion).getSecond());
            check(secondConsump);
        }
        if (currQuestion instanceof InsteadOfQuestion) {
            chosenAnswer = ((InsteadOfQuestion) currQuestion).getSecondChoice().toString();
            check(secondConsump);
        }
    }


    /**
     * Works the same as for the previous buttons
     */
    public void chooseThird() {
        if (currQuestion instanceof QuizzQuestion) {
            chosenAnswer = ((QuizzQuestion) currQuestion).getThirdChoice().getTitle();
            check(thirdAnswer);
        }
        if (currQuestion instanceof ConsumpQuestion) {
            chosenAnswer = Long.toString(((ConsumpQuestion) currQuestion).getThird());
            check(thirdConsump);
        }
        if (currQuestion instanceof InsteadOfQuestion) {
            chosenAnswer = ((InsteadOfQuestion) currQuestion).getThirdChoice().toString();
            check(thirdConsump);
        }
    }

    /**
     * checks if the answer chosen was the right one, and if so distributes the points. Display the wh for each
     * choice.
     *
     * @param chosenBox box of the answer that was chosen
     */
    public void check(Button chosenBox) {
        questionTimer.pause();
        points = timeLeft * 25 + 500;

        if (currQuestion instanceof QuizzQuestion) {
            correctAnswer = ((QuizzQuestion) currQuestion).getMostExpensive();
            firstAnswerLabel.setText("this consumes " + ((QuizzQuestion) currQuestion).getFirstChoice().getConsumption_in_wh() + " wh");
            secondAnswerLabel.setText("this consumes " + ((QuizzQuestion) currQuestion).getSecondChoice().getConsumption_in_wh() + " wh");
            thirdAnswerLabel.setText("this consumes " + ((QuizzQuestion) currQuestion).getThirdChoice().getConsumption_in_wh() + " wh");
        }
        if (currQuestion instanceof ConsumpQuestion) {
            correctAnswer = ((ConsumpQuestion) currQuestion).getConsump();
        }
        if (currQuestion instanceof InsteadOfQuestion) {
            correctAnswer = ((InsteadOfQuestion) currQuestion).getCorrectChoice().toString();
        }

        if (chosenAnswer.equals(correctAnswer)) {
            question.setText("Yeah, that's right!");
            chosenBox.setStyle("-fx-background-color: green;");

            //If joker is applied
            if(doublePoints) {
                points *= 2;
                doublePoints = false;
            }

            totalPoints += points;
            pointCounter.setText("current points: " + totalPoints);
        } else {
            points = 0;
            question.setText("That's wrong!");
            wrongAnswer();
        }

        Utils.submitAnswer(points);
        transition();
    }

    /**
     * When the user clicks on the submit button, this method calculates the points that should be
     * received, shows whether the question was answered correctly and if not it shows the correct answer
     */
    public void submitGuess() {
        if (guess.getText() == "") {
            return;
        }
        if (currQuestion instanceof GuessQuestion) {
            questionTimer.pause();
            points = timeLeft * 25 + 500;

            chosenAnswer = guess.getText();
            correctAnswer = ((GuessQuestion) currQuestion).getCorrectGuess();
            if (chosenAnswer.equals(correctAnswer)) {
                question.setText("Yeah, that's right!");
                guess.setStyle("-fx-background-color: green;");
                points = points * 2;
                //If double points joker was applied
                if(doublePoints) {
                    points*=2;
                    doublePoints = false;
                }
                totalPoints += points;
                pointCounter.setText("current points: " + totalPoints);
            } else if (Math.abs(Long.parseLong(correctAnswer) - Long.parseLong(chosenAnswer)) < Long.parseLong(correctAnswer) * 0.3) {
                question.setText("Very close!");
                guess.setStyle("-fx-background-color: orange;");
                //If double points joker was applied
                if(doublePoints) {
                    points*=2;
                    doublePoints = false;
                }
                totalPoints += points;
                pointCounter.setText("current points: " + totalPoints);
                guessLabel.setText("this consumes " + ((GuessQuestion) currQuestion).getActivity().getConsumption_in_wh() + " wh");
            } else {
                points = 0;
                question.setText("That's wrong!");
                guess.setStyle("-fx-background-color: red;");
                guessLabel.setText("this consumes " + ((GuessQuestion) currQuestion).getActivity().getConsumption_in_wh() + " wh");
            }

            Utils.submitAnswer(points);
            submit.setDisable(true);
            transition();
        }
    }


    /**
     * handles the display when the chosen answer was not the right answer.
     */
    public void wrongAnswer() {
        String first = "", second = "", third = "";
        if (currQuestion instanceof QuizzQuestion) {
            correctAnswer = ((QuizzQuestion) currQuestion).getMostExpensive();
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
            correctAnswer = ((ConsumpQuestion) currQuestion).getConsump();
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
        if (currQuestion instanceof InsteadOfQuestion) {
            correctAnswer = ((InsteadOfQuestion) currQuestion).getCorrectChoice().toString();
            first = ((InsteadOfQuestion) currQuestion).getFirstChoice().toString();
            second = ((InsteadOfQuestion) currQuestion).getSecondChoice().toString();
            third = ((InsteadOfQuestion) currQuestion).getThirdChoice().toString();
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
    public void transition() {
        confirmButton.setVisible(false);
        notConfirmButton.setVisible(false);
        confirmButton.setDisable(true);
        endButton.setDisable(true);
        notConfirmButton.setDisable(true);
        if (currQuestion instanceof QuizzQuestion) {
            firstAnswer.setDisable(true);
            firstAnswerLabel.setOpacity(1);
            secondAnswer.setDisable(true);
            secondAnswerLabel.setOpacity(1);
            thirdAnswer.setDisable(true);
            thirdAnswerLabel.setOpacity(1);
        } else if (currQuestion instanceof ConsumpQuestion || currQuestion instanceof InsteadOfQuestion) {
            firstConsump.setDisable(true);
            secondConsump.setDisable(true);
            thirdConsump.setDisable(true);
        } else {
            guess.setDisable(true);
            submit.setDisable(true);
        }
        transitionStuff();
        Timeline timer = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> {
                            if (transitionTimeLeft == 0) {
                                transitionTimerAnimation.stop();
                                transitionTimer.setOpacity(1);
                                transitionTimer.setVisible(false);
                                timeBarFill.setVisible(true);
                                timeBarBackground.setVisible(true);
                                endButton.setDisable(false);
                                time.setVisible(true);
                                readyForNextQuestion = true;
                                nextDisplay();
                            } else {
                                transitionTimeLeft -= 1;
                                transitionTimer.setText(transitionTimeLeft + " seconds until next question!");
                            }
                        }
                )
        );
        timer.setCycleCount(3);
        timer.play();
    }

    /**
     * Initialise all event handling for the emote and joker buttons (Should be called every new question)
     */
    public void initialiseEmotesAndJokers() {
        // Timer that regularly calls update methods
        emoteJokerUpdateTimer = new Timer();
        emoteJokerUpdateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                lobbyStatus = Utils.getLobbyStatus();

                receiveEmotes();
            }
        }, 0, 20);

        // Set up the on action event for each emote button corresponding to their image
        emoteButtonSmile.setOnAction(e -> sendEmote("smile"));
        emoteButtonSad.setOnAction(e -> sendEmote("sad"));
        emoteButtonAngry.setOnAction(e -> sendEmote("angry"));
        emoteButtonSurprise.setOnAction(e -> sendEmote("surprise"));
        emoteButtonCelebrate.setOnAction(e -> sendEmote("celebrate"));
        emoteButtonSunglasses.setOnAction(e -> sendEmote("sunglasses"));

        // Add some space at the top of the chat box
        Label blankLabel = new Label();
        blankLabel.setOpacity(1);
        blankLabel.setStyle("-fx-font-size: 12pt; -fx-text-fill: black;");
        blankLabel.setTextAlignment(TextAlignment.CENTER);
        Platform.runLater(() -> chatBoxContent.getChildren().add(new Label()));

        // Listener to scroll to the bottom of the chat box once a new message has been received
        chatBox.needsLayoutProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                chatBox.setVvalue(chatBox.getVmax()); // Auto scroll to the bottom so player can always see the latest message
            }
        });

        //TODO
        // Turn off joker buttons that have already been used (or cant be used for the question type)
    }

    /**
     * Once the player clicks an emote button, this method is called
     * It then sends the emote to the other players in the session
     *
     * @param emoteType identifies which emote button was clicked and what emote to send
     */
    private void sendEmote(String emoteType) {
        Utils.setEmoji(Session.getNickname(), emoteType);
    }

    /**
     * Receive any emotes sent by other players and display them in the chat box
     */
    private void receiveEmotes() {
        List<Emoji> activeEmojiList = lobbyStatus.getEmojiList();

        // Loop through all active emojis and display them according to the user that sent it
        for (Emoji emoji : activeEmojiList) {

            if (emojisReceivedThisQuestion.contains(emoji)) {
                continue;
            }

            emojisReceivedThisQuestion.add(emoji);

            String emoteText;

            // Get the right emote text
            switch (emoji.getEmojiType()) {
                case "smile" -> emoteText = "\uD83D\uDE00";
                case "sad" -> emoteText = "\uD83D\uDE22";
                case "angry" -> emoteText = "\uD83D\uDE21";
                case "surprise" -> emoteText = "\uD83D\uDE32";
                case "celebrate" -> emoteText = "\uD83C\uDF89";
                case "sunglasses" -> emoteText = "\uD83D\uDE0E";
                default -> throw new IllegalStateException("Unexpected value: " + emoji.getEmojiType());
            }

            Label message = new Label("  " + emoji.getUserApplying() + ": " + emoteText);
            message.setOpacity(1);
            message.setStyle("-fx-font-size: 12pt; -fx-text-fill: black;");
            message.setTextAlignment(TextAlignment.CENTER);

            Platform.runLater(() -> {
                chatBoxContent.getChildren().add(message);
            });
        }
    }

    /*
    Joker types:
        - 1 double points
        - 2 remove one incorrect answer
        - 3 reduce time joker
     */

    /**
     * Applies doublePoints joker
     */
    public void useJokerDoublePoints() {
        doublePoints = true;
        jokerButton1.setDisable(true);

        Utils.addJoker(new Joker(Session.getNickname(),1,(int)Session.getQuestionNum()));
    }

    /**
     * Applies removeAnAnswer joker
     */
    public void useJokerRemoveAnAnswer() {
        if(currQuestion instanceof GuessQuestion) {
            guess.setText(((GuessQuestion) currQuestion).getCorrectGuess());
        } else if(currQuestion instanceof QuizzQuestion) {
            String corrAnswer = ((QuizzQuestion) currQuestion).getMostExpensive();
            double randomPick = Math.random(); //Random number 0 OR 1 telling which answer to pick

            if(((QuizzQuestion)currQuestion).getFirstChoice().getTitle().equals(corrAnswer)) {
                if(randomPick < 0.5) secondAnswer.setStyle("-fx-background-color: red;");
                else thirdAnswer.setStyle("-fx-background-color: red;");
            } else if(((QuizzQuestion)currQuestion).getSecondChoice().getTitle().equals(corrAnswer)) {
                if(randomPick < 0.5) firstAnswer.setStyle("-fx-background-color: red;");
                else thirdAnswer.setStyle("-fx-background-color: red;");
            } else {
                if(randomPick < 0.5) firstAnswer.setStyle("-fx-background-color: red;");
                else secondAnswer.setStyle("-fx-background-color: red;");
            }
        } else if(currQuestion instanceof ConsumpQuestion) {
            String corrAnswer = ((ConsumpQuestion) currQuestion).getConsump();
            double randomPick = Math.random(); //Random number 0 OR 1 telling which answer to pick

            if(Long.toString(((ConsumpQuestion)currQuestion).getFirst()).equals(corrAnswer)) {
                if(randomPick < 0.5) secondConsump.setStyle("-fx-background-color: red;");
                else thirdConsump.setStyle("-fx-background-color: red;");
            } else if(Long.toString(((ConsumpQuestion)currQuestion).getSecond()).equals(corrAnswer)) {
                if(randomPick < 0.5) firstConsump.setStyle("-fx-background-color: red;");
                else thirdConsump.setStyle("-fx-background-color: red;");
            } else {
                if(randomPick < 0.5) firstConsump.setStyle("-fx-background-color: red;");
                else secondConsump.setStyle("-fx-background-color: red;");
            }
        } else {
            String corrAnswer = ((InsteadOfQuestion)currQuestion).getCorrectChoice().toString();
            double randomPick = Math.random(); //Random number 0 OR 1 telling which answer to pick

            if(((InsteadOfQuestion)currQuestion).getFirstChoice().toString().equals(corrAnswer)) {
                if(randomPick < 0.5) secondConsump.setStyle("-fx-background-color: red;");
                else thirdConsump.setStyle("-fx-background-color: red;");
            } else if(((InsteadOfQuestion)currQuestion).getSecondChoice().toString().equals(corrAnswer)) {
                if(randomPick < 0.5) firstConsump.setStyle("-fx-background-color: red;");
                else thirdConsump.setStyle("-fx-background-color: red;");
            } else {
                if(randomPick < 0.5) firstConsump.setStyle("-fx-background-color: red;");
                else secondConsump.setStyle("-fx-background-color: red;");
            }
        }
        jokerButton2.setDisable(true);
    }

    /**
     * Applies reduceTime joker
     */
    public void useJokerReduceTime() {
        //Send reduceTime joker to server
        Utils.addJoker(new Joker(Session.getNickname(),3,(int)Session.getQuestionNum()));

        jokerButton3.setDisable(true);
    }

    /**
     * Provides name for jokerType
     * @param jokerType - Integer value <1,3> being type of Joker
     * @return String description of given joker
     */
    private String getJokerDescription(int jokerType) {
        switch(jokerType) {
            case 1 -> {
                return "2x";
            }
            case 2 -> {
                return "50/50";
            }
            case 3 -> {
                return "time/2";
            }
        }

        return "";
    }

    /**
     * Manages jokerList
     * @param receivedJokers
     */
    private void receiveJokers(List<Joker> receivedJokers) {
        if(receivedJokers == null || receivedJokers.size() == 0) return;

        //For each received joker
        for(Joker x : receivedJokers) {
            if(usedJokers.contains(x)) continue;

            //Display to others that PLAYER x has used joker
            Label message = new Label("  " + x.getUsedBy()+" used "+this.getJokerDescription(x.getJokerType()));
            message.setOpacity(1);
            message.setStyle("-fx-font-size: 12pt; -fx-text-fill: black;");
            message.setTextAlignment(TextAlignment.CENTER);
            Platform.runLater(() -> {
                chatBoxContent.getChildren().add(message);
            });

            //Append joker to used joker list
            usedJokers.add(x);
            //If reduced time joker was used by not me on current question
            if(!x.getUsedBy().equals(Session.getNickname()) && x.getJokerType() == 3 && x.getQuestionNum() == Session.getQuestionNum()) {
                timeLeft/=2;
                reduceTime = true;
            }
        }
    }


    public void transitionStuff() {
        timeBarAnimation.pause();
        transitionTimeLeft = 2;
        transitionTimer.setVisible(true);
        transitionTimer.setText(transitionTimeLeft + " seconds until next question!");

        transitionTimerAnimation = new ScaleTransition(Duration.seconds(1), transitionTimer);
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
    }

    /**
     * handles the end of a game.
     */
    public void endOfGame() {
        endButton.setDisable(true);
        questionNumber.setVisible(false);
        questionTimer.pause();
        timeBarAnimation.stop();
        questionUpdateTimer.cancel();
        Player player = serverUtils.getPlayer(Session.getNickname());
        if (player.getScore() < totalPoints) {
            serverUtils.updatePlayerInRepo(Session.getNickname(), totalPoints);
            transitionTimer.setText("Congratulations! You improved your score!");
        } else {
            transitionTimer.setText("You had a higher score before! Try again!");
        }
        notQuizzPage();
        notGuessPage();
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

                            confirmButton.setDisable(true);
                            confirmButton.setVisible(false);
                            notConfirmButton.setDisable(true);
                            notConfirmButton.setVisible(false);

                            question.setText(currQuestion.getQuestion());
                        }
                )
        );
        toEnd = false;
        timer.setCycleCount(1);
        timer.play();
    }

    /**
     * Makes everything that is shown on the "QuizzQuestion" screen invisible and unclickable
     */
    public void notQuizzPage() {
        firstAnswer.setVisible(false);
        firstAnswer.setDisable(true);
        secondAnswer.setVisible(false);
        secondAnswer.setDisable(true);
        thirdAnswer.setVisible(false);
        thirdAnswer.setDisable(true);
        firstAnswerLabel.setVisible(false);
        secondAnswerLabel.setVisible(false);
        thirdAnswerLabel.setVisible(false);
    }

    /**
     * Shows and sets to front everything that should be shown for the "QuizzQuestion" question type
     */
    public void showQuizzPage() {
        notConsumpPage();
        notGuessPage();
        firstAnswer.setVisible(true);
        secondAnswer.setVisible(true);
        thirdAnswer.setVisible(true);
        firstAnswer.setDisable(false);
        secondAnswer.setDisable(false);
        thirdAnswer.setDisable(false);
        firstAnswerLabel.setVisible(true);
        secondAnswerLabel.setVisible(true);
        thirdAnswerLabel.setVisible(true);
        firstAnswer.toFront();
        secondAnswer.toFront();
        thirdAnswer.toFront();
        firstAnswerImage.toFront();
        secondAnswerImage.toFront();
        thirdAnswerImage.toFront();
        firstAnswerLabel.toFront();
        secondAnswerLabel.toFront();
        thirdAnswerLabel.toFront();
    }


    /**
     * Makes everything that is shown on the "ConsumpQuestion" screen invisible and unclickable
     */
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

    /**
     * Show, make clickable and set to front everything that should be shown for the "ConsumpQuestion" question type
     */
    public void consumpPage() {
        notGuessPage();
        notQuizzPage();
        firstConsump.setVisible(true);
        firstConsump.setDisable(false);
        secondConsump.setVisible(true);
        secondConsump.setDisable(false);
        thirdConsump.setVisible(true);
        thirdConsump.setDisable(false);
        activityImage.setVisible(true);
        activity.setVisible(true);
        firstConsump.toFront();
        secondConsump.toFront();
        thirdConsump.toFront();
        activity.toFront();
        activityImage.toFront();
    }

    /**
     * Makes everything that is shown on the "GuessQuestion" screen invisible and unclickable
     */
    public void notGuessPage() {
        activity.setVisible(false);
        activityImage.setVisible(false);
        guess.setVisible(false);
        submit.setVisible(false);
        submit.setDisable(true);
        guessLabel.setVisible(false);
    }

    /**
     * Show, make clickable and set to front everything that should be shown for the "GuessQuestion" question type
     */
    public void guessPage() {
        notQuizzPage();
        notConsumpPage();
        activity.setVisible(true);
        activityImage.setVisible(true);
        guess.setVisible(true);
        guessLabel.setVisible(true);
        submit.setVisible(true);
        submit.setDisable(false);
        activity.toFront();
        activityImage.toFront();
        guess.toFront();
        submit.toFront();
    }

    /**
     * When the "finish now" button is clicked, it asks whether you are sure you want to quit
     */
    public void confirmPage() {
        notQuizzPage();
        notConsumpPage();
        notGuessPage();
        timeBarFill.setVisible(false);
        timeBarBackground.setVisible(false);
        time.setVisible(false);
        firstAnswer.setVisible(false);
        transitionTimer.setVisible(true);
        transitionTimer.setText("Are you sure?");
        confirmButton.setDisable(false);
        confirmButton.setVisible(true);
        notConfirmButton.setDisable(false);
        notConfirmButton.setVisible(true);
        confirmButton.toFront();
        transitionTimer.toFront();
        notConfirmButton.toFront();
    }

    /**
     * Closes the confirm screen when the "no" button is clicked
     */
    public void closeConfirmPage() {
        timeBarFill.setVisible(true);
        timeBarBackground.setVisible(true);
        time.setVisible(true);
        if (currQuestion instanceof QuizzQuestion) {
            showQuizzPage();
        }
        if (currQuestion instanceof ConsumpQuestion) {
            consumpPage();
        }
        if (currQuestion instanceof InsteadOfQuestion) {
            consumpPage();
        }
        if (currQuestion instanceof GuessQuestion) {
            guessPage();
        }
        transitionTimer.setText("");
        confirmButton.setDisable(true);
        confirmButton.setVisible(false);
        notConfirmButton.setDisable(true);
        notConfirmButton.setVisible(false);
    }

    /**
     * Ends the game when the "yes" button is clicked
     */
    public void confirmQuit() {
        transitionTimer.setVisible(true);
        questionUpdateTimer.cancel();
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
