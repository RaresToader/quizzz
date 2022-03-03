package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class EnterMenuCtrl {

    @FXML
    private TextField username;
    @FXML
    private Button cancel;
    @FXML
    private Button enter;
    @FXML
    private TextField server;


    @FXML
    public void cleanText(){
        username.clear();
        server.clear();
    }

}
