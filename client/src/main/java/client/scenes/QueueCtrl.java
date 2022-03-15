package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;

import javax.inject.Inject;

public class QueueCtrl {
    private final MainCtrl mainCtrl;

    @FXML
    private Button goBack;

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



    public void goBackToSplash() {
        mainCtrl.showSplash();
    }

    public void runLoadingAnimation() {
        mainCtrl.rotationAnimation1.play();
        mainCtrl.rotationAnimation2.play();
        mainCtrl.rotationAnimation3.play();
        mainCtrl.rotationAnimation4.play();
    }
}

