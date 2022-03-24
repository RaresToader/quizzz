package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.inject.Inject;

public class MultiplayerLobbyCtrl {
    private MainCtrl mainCtrl;

    @FXML
    private Button leaveButton;
    @FXML
    private Button startButton;

    @Inject
    public MultiplayerLobbyCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;

    }

    public void goBackToSplash() {
        mainCtrl.showSplash();
    }

    public void startGame() {
        //TODO
        return;
    }
}
