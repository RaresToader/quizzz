package client.scenes;

import client.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;
import javax.naming.event.EventContext;
import java.awt.event.ActionEvent;

public class SplashCtrl {
    private MainCtrl mainCtrl;

    @Inject
    public SplashCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    @FXML
    private Button single;

    @FXML
    private Button multi;

    @FXML
    private Button leaderboard;

    @FXML
    private Label playerCounter;

    @FXML
    private Button quitButton;

    @FXML
    private Button confirmQuitButton;

    @FXML
    private Button cancelQuitButton;

    @FXML
    private AnchorPane confirmQuitAnchor;

    public void visibleConfirmQuitPopUp(){
        confirmQuitAnchor.setVisible(true);
        confirmQuitButton.setDisable(false);
        cancelQuitButton.setDisable(false);
        quitButton.setVisible(false);
        quitButton.setDisable(true);
    }

    public void invisibleConfirmQuitPopUp(){
        confirmQuitAnchor.setVisible(false);
        confirmQuitButton.setDisable(true);
        cancelQuitButton.setDisable(true);
        quitButton.setVisible(true);
        quitButton.setDisable(false);
    }

    public void initialize() {
        playerCounter.setText(Utils.getActivePlayers());
    }

    public void quit() {

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
