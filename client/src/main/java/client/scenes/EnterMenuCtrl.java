package client.scenes;

import client.Session;
import client.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import client.utils.ServerUtils;
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
        String nickname   = username.getText();
        String serverAddr = server.getText();
        if(nickname != null && nickname.length() > 0 && Utils.isAlphaNumeric(nickname)) { //Invalid nickname
            if(serverAddr == null || serverAddr.length() == 0) { //Invalid serverAddr
                errorText.setText("Server address is invalid");
                return;
            }

            Session.setNickname(nickname);
            Session.setServerAddr(serverAddr);
            mainCtrl.showSplash();
        } else errorText.setText("Provided username is invalid");
    }
}
