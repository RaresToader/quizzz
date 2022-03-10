package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class EnterMenuCtrl {

    private final MainCtrl mainCtrl;
    @FXML
    private TextField username;
    @FXML
    private Button cancel;
    @FXML
    private Button enter;
    @FXML
    private TextField server;

    @Inject
    public EnterMenuCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    @FXML
    public void cleanText() {
        username.clear();
        server.clear();
    }

    public void enterButton() {
        mainCtrl.showSplash();
    }

}
