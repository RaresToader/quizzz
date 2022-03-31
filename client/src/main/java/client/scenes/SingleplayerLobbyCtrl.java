package client.scenes;

import client.utils.ServerUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.inject.Inject;

public class SingleplayerLobbyCtrl {
    private MainCtrl mainCtrl;
    private final ServerUtils serverUtils;
    private GlobalLeaderboardCtrl globalLeaderboardCtrl;
    private int transitionTimeLeft;
    private Timeline timerForStart = new Timeline();
    private QuestionScreenCtrl questionScreenCtrl;

    @Inject
    public SingleplayerLobbyCtrl(MainCtrl mainCtrl, ServerUtils serverUtils, GlobalLeaderboardCtrl globalLeaderboardCtrl, QuestionScreenCtrl questionScreenCtrl) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
        this.globalLeaderboardCtrl = globalLeaderboardCtrl;
        this.questionScreenCtrl = questionScreenCtrl;
    }

    @FXML
    public Text playerNameLabel;
    @FXML
    private Button leaveButton;
    @FXML
    public Button startGameButton;
    @FXML
    private Button leaderboardButton;
    @FXML
    private Text label1;

    public void leaderboardButton() {
        if (timerForStart != null) {
            timerForStart.stop();
            label1.setText("Play by yourself and place on the global leaderboard!");
            startGameButton.setDisable(false);
        }
        globalLeaderboardCtrl.init();
        mainCtrl.showGlobalLeaderboard(true, true);
    }

    public void toSingleplayer() {
        startGameButton.setDisable(true);
        transitionTimeLeft = 5;
        label1.setText("Starting in " + transitionTimeLeft + "!");
        timerForStart = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> {
                            //System.out.println("transitionTimeLeft = " + transitionTimeLeft); //DEBUG LINE
                            if (transitionTimeLeft == 0) {
                                label1.setText("Play by yourself and place on the global leaderboard!");
                                startGameButton.setDisable(false);
                                mainCtrl.showSingleplayer();
                            } else {
                                transitionTimeLeft -= 1;
                                label1.setText("Starting in " + transitionTimeLeft + "!");
                            }
                        }
                )
        );
        timerForStart.setCycleCount(6);
        timerForStart.play();
    }

    public void goBackToSplash() {
        if (timerForStart != null) {
            timerForStart.stop();
            label1.setText("Play by yourself and place on the global leaderboard!");
            startGameButton.setDisable(false);
        }
        mainCtrl.showSplash();
    }
}
