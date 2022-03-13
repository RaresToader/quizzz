package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.inject.Inject;

public class QueueCtrl {
    private final MainCtrl mainCtrl;
    @FXML
    private Button goBack;

    @Inject
    public QueueCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void goBackToSplash() {
        mainCtrl.showSplash();
    }
}
