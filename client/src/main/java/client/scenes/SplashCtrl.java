package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javax.inject.Inject;

public class SplashCtrl {
    private MainCtrl mainCtrl;

    @Inject
    public SplashCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    @FXML
    private Button quit;
    @FXML
    private Button single;
    @FXML
    private Button multi;
    @FXML
    private Button leadeboard;
    @FXML
    private Label playerCounter;

    public void quitButton() {
        mainCtrl.closeSplash();
    }

    public void leaderboardButton() {
        mainCtrl.showGlobalLeaderboard();
    }
    public void goToMultiplayerQueue() {
        mainCtrl.showQueue();
    }
    public void toSinglePlayer() {
        mainCtrl.showSingleplayer();
    }
}
