package client.scenes;

import client.Session;
import client.utils.Utils;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import client.utils.ServerUtils;
import javafx.util.Duration;

import javax.inject.Inject;

public class EnterMenuCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils serverUtils;
    @FXML
    private TextField username;
    @FXML
    private Button cancel;
    @FXML
    private Button enter;
    @FXML
    private TextField server;
    @FXML
    private Label errorText;

    @Inject
    public EnterMenuCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }

    @FXML
    public void cleanText() {
        username.clear();
        server.clear();
    }

    public void enterButton() {
        String nickname = username.getText();
        String serverAddr = server.getText();
        if (nickname != null && nickname.length() > 0 && Utils.isAlphaNumeric(nickname)) { //Invalid nickname
            if (nickname.length() > 8) {
                displayErrorText("Username is too long! (max 8 characters)");
                return;
            }
            if (serverAddr == null || serverAddr.length() == 0 || !Utils.validateServer(serverAddr)) { //Invalid serverAddr
                displayErrorText("Server address is invalid!");
                return;
            }

            Session.setNickname(nickname);
            this.serverUtils.addPlayer(nickname);
            Session.setServerAddr(serverAddr);
            mainCtrl.showSplash();
        }
        else {
            displayErrorText("Provided username is invalid!");
        }
    }

    private void displayErrorText(String text) {
        errorText.setText(text);
        errorText.setOpacity(1);
        FadeTransition errorFade = new FadeTransition(Duration.seconds(4), errorText);
        errorFade.setFromValue(1);
        errorFade.setToValue(0);
        errorFade.play();
    }
}
