package client.scenes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


public class QuitCtrl {

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

    /**
     * This method pops up a new FXML-scene that displays a message that asks if you really want to quit.
     *
     * @param event (the press of the exit in a scene)
     */
    @FXML
    public void popUp(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConfirmQuitPopUp.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();

    }

    /**
     * This need to be linked to the button in the 'QuitPopUpScene' if the player excepts the quit than there needs to be a link
     * back to the main screen
     *
     * @param event (the press of the 'confirm quit'-button in the 'ConfirmQuitPopUpScene'
     */




}
