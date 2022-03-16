package client.scenes;

import client.utils.ServerUtils;
import client.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;


public class SplashCtrl {
    private MainCtrl mainCtrl;
    private final ServerUtils serverUtils;
    private GlobalLeaderboardCtrl globalLeaderboardCtrl;
    private AdminPanelCtrl adminPanelCtrl;

    @Inject
    public SplashCtrl(MainCtrl mainCtrl,ServerUtils serverUtils,GlobalLeaderboardCtrl globalLeaderboardCtrl,AdminPanelCtrl adminPanelCtrl) {
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
    private Button quitButton;

    @FXML
    private Button confirmQuitButton;

    @FXML
    private Button cancelQuitButton;

    @FXML
    private AnchorPane confirmQuitAnchor;

    @FXML
    private Button adminPanelButton;

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
    }

    public void initialize() {
        playerCounter.setText(Utils.getActivePlayers());
    }

    /**
     * This method is triggered by the '(Confirm) Quit-Button' that is on the 'confirm-quit' pop-up. It will close
     * the splashscreen.
     */

    public void quit() {

        mainCtrl.closeSplash();
    }

    public void leaderboardButton() {
        globalLeaderboardCtrl.initialize();
        mainCtrl.showGlobalLeaderboard();
    }

    public void adminPanelButton(){
        adminPanelCtrl.initialize();
        mainCtrl.showAdminPanel();

    }

    public void goToMultiplayerQueue() {
        mainCtrl.showQueue();
    }

    public void toSinglePlayer() {
        mainCtrl.showSingleplayer();
    }
}
