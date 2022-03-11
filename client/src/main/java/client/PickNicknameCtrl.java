package client;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class PickNicknameCtrl{

    @FXML
    private Button EnterNicknameButton;

    @FXML
    private TextField NicknameTextField;

    private String nickname;

    public void nicknameConstructor(ActionEvent actionEvent) {
        String nickname = NicknameTextField.getText();
        if (!nickname.equals("")) {
            this.nickname = nickname;
        }
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
