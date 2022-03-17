package client.scenes;

import client.utils.ServerUtils;
import client.utils.Utils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;


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
    private Button single;

    @FXML
    private Button multi;

    @FXML
    private Button leaderboard;

    @FXML
    private Label playerCounter;

    @FXML
    private Label playerCounterLabel;

    @FXML
    private Button quitButton;

    @FXML
    private Button confirmQuitButton;

    @FXML
    private Button cancelQuitButton;

    @FXML
    private AnchorPane confirmQuitAnchor;

    @FXML
    private Button adminPanelButton;

    private Timer activePlayersTimer;

    //Dafault JFX method started after function init
    public void initialize() {}

    //Method to be inited after choosing this screen
    public void init() {
        activePlayersTimer = new Timer();

        activePlayersTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        playerCounter.setText(Utils.getActivePlayers());
                    }
                });
            }
        }, 0, 2*1000); //Update active player number every 2 seconds
    }

    /**
     * This method is triggered by the action: clicking on Quit button on the Splash screen.
     * The anchor with the 'pop-up' is made visible and enabled. The buttons "Quit" & "Cancel"
     * are also made visible and enabled.
     */

    public void visibleConfirmQuitPopUp(){
        confirmQuitAnchor.setVisible(true);
        confirmQuitAnchor.setDisable(false);
        confirmQuitButton.setDisable(false);
        cancelQuitButton.setDisable(false);
        quitButton.setVisible(false);
        quitButton.setDisable(true);
        leaderboard.setDisable(true);
        leaderboard.setVisible(false);
        playerCounterLabel.setVisible(false);
        playerCounter.setVisible(false);
        adminPanelButton.setDisable(true);
        adminPanelButton.setVisible(false);
    }

    /**
     * This method is triggered by the "Cancel"-button in the 'Pop-Up'-screen. It reverses the
     * visibleConfirmQuitPopUp-method and the screen will be the same as it first was.
     */

    public void invisibleConfirmQuitPopUp(){
        confirmQuitAnchor.setVisible(false);
        confirmQuitAnchor.setDisable(true);
        confirmQuitButton.setDisable(true);
        cancelQuitButton.setDisable(true);
        quitButton.setVisible(true);
        quitButton.setDisable(false);
        leaderboard.setVisible(true);
        leaderboard.setDisable(false);
        playerCounter.setVisible(false);
        playerCounterLabel.setVisible(true);
        playerCounter.setVisible(true);
        adminPanelButton.setDisable(false);
        adminPanelButton.setVisible(true);

    }



    /**
     * This method is triggered by the '(Confirm) Quit-Button' that is on the 'confirm-quit' pop-up. It will close
     * the splashscreen.
     */

    public void quit() {
        activePlayersTimer.cancel();
        mainCtrl.closeSplash();
    }

    public void leaderboardButton() {
        globalLeaderboardCtrl.initialize();
        activePlayersTimer.cancel();
        mainCtrl.showGlobalLeaderboard(true);
    }

    public void adminPanelButton(){
        mainCtrl.showAdminPanel();

    }

    public void goToMultiplayerQueue() {
        activePlayersTimer.cancel();
        mainCtrl.showQueue();
    }

    public void toSinglePlayer() {
        activePlayersTimer.cancel();
        mainCtrl.showSingleplayer();
    }
}
