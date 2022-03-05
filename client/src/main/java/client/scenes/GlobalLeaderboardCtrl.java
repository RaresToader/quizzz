package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.inject.Inject;

public class GlobalLeaderboardCtrl {

    private final MainCtrl mainCtrl;
    @FXML
    private Button goBack;
    @Inject
    public GlobalLeaderboardCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }
    public void goBackToSplash(){
        mainCtrl.showSplash();
    }
}
