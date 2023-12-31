package client.scenes;

import client.utils.Utils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import javax.inject.Inject;

public class QueueCtrl {
    private final MainCtrl mainCtrl;

    private int transitionTimeLeft;
    private Timeline transitionTimer;

    @FXML
    public Circle loadingCircle1;
    @FXML
    public Circle loadingCircle2;
    @FXML
    public Circle loadingCircle3;
    @FXML
    public Circle loadingCircle4;
    @FXML
    public Circle pivot;

    @Inject
    public QueueCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void init() {
        transitionTimeLeft = 3;
        transitionTimer = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> {
                        System.out.println("transitionTimeLeft = " + transitionTimeLeft); //DEBUG LINE
                            if (transitionTimeLeft == 0) {
                                if (Utils.joinSession()) {
                                    mainCtrl.showMultiplayerLobby();
                                }
                                else{ // ERROR
                                    System.out.println("Joining a lobby has failed!");
                                    init(); // Try again
                                }
                            }
                            else {
                                transitionTimeLeft -= 1;
                            }
                        }
                )
        );
        transitionTimer.setCycleCount(4);
        transitionTimer.play();
    }

    public void goBackToSplash() {
        transitionTimer.stop();
        mainCtrl.showSplash();
    }

    /**
     * Runs the loading icon animation for each element. The animation is created in MainCtrl
     */
    public void runLoadingAnimation() {
        mainCtrl.rotationAnimation1.play();
        mainCtrl.rotationAnimation2.play();
        mainCtrl.rotationAnimation3.play();
        mainCtrl.rotationAnimation4.play();
    }
}

