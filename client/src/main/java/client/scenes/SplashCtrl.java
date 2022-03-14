package client.scenes;

import client.utils.ServerUtils;
import client.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javax.inject.Inject;

public class SplashCtrl {
    private MainCtrl mainCtrl;
    private final ServerUtils serverUtils;
    private GlobalLeaderboardCtrl globalLeaderboardCtrl;
    @Inject
    public SplashCtrl(MainCtrl mainCtrl,ServerUtils serverUtils,GlobalLeaderboardCtrl globalLeaderboardCtrl) {
        this.mainCtrl = mainCtrl;
        this.serverUtils=serverUtils;
        this.globalLeaderboardCtrl = globalLeaderboardCtrl;
    }

    @FXML
    private Button quit;
    @FXML
    private Button single;
    @FXML
    private Button multi;
    @FXML
    private Button leaderboard;
    @FXML
    private Label playerCounter;

    public void initialize() {
        playerCounter.setText(Utils.getActivePlayers());
    }

    public void quitButton() {
        mainCtrl.closeSplash();
    }

    public void leaderboardButton() {
        globalLeaderboardCtrl.initialize();
        mainCtrl.showGlobalLeaderboard();
    }

    public void goToMultiplayerQueue() {
        mainCtrl.showQueue();
    }

    public void toSinglePlayer() {
        mainCtrl.showSingleplayer();
    }
}
