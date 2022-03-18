package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class SingleplayerLobbyCtrl {
    private MainCtrl mainCtrl;
    private final ServerUtils serverUtils;
    private GlobalLeaderboardCtrl globalLeaderboardCtrl;

    @Inject
    public SingleplayerLobbyCtrl(MainCtrl mainCtrl,ServerUtils serverUtils,GlobalLeaderboardCtrl globalLeaderboardCtrl) {
        this.mainCtrl = mainCtrl;
        this.serverUtils=serverUtils;
        this.globalLeaderboardCtrl = globalLeaderboardCtrl;
    }

    @FXML
    public Text playerNameLabel;
    @FXML
    private Button leaveButton;
    @FXML
    private Button startGameButton;
    @FXML
    private Button leaderboardButton;

    public void leaderboardButton() {
        globalLeaderboardCtrl.init();
        mainCtrl.showGlobalLeaderboard(true);
    }

    public void toSingleplayer() {
        mainCtrl.showSingleplayer();
    }

    public void goBackToSplash() {
        mainCtrl.showSplash();
    }
}
