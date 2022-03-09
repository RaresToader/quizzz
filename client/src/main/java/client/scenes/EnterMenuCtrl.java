package client.scenes;

import client.utils.ServerUtils;
import commons.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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

    @Inject
    public EnterMenuCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }



    @FXML
    public void cleanText(){
        username.clear();
        server.clear();
    }

    public void enterButton(){
        String nickname = username.getText();
        if(serverUtils.checkIfServerMatches(server.getText())){
            Player player = serverUtils.addPlayer(nickname);
            mainCtrl.showSplash();
        }
        else{
            server.setText("THE SERVER DOES NOT EXIST");
        }
    }
}
