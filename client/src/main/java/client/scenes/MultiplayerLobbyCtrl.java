package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import javax.inject.Inject;

public class MultiplayerLobbyCtrl {
    private MainCtrl mainCtrl;

    @FXML
    private Button startButton;
    @FXML
    private Pane emoteBox;
    @FXML
    private Button emoteMenu;
    @FXML
    private Button emoteButton1;
    @FXML
    private Button emoteButton2;
    @FXML
    private Button emoteButton3;
    @FXML
    private Button emoteButton4;
    @FXML
    private Button emoteButton5;
    @FXML
    private Button emoteButton6;

    @Inject
    public MultiplayerLobbyCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;

    }

    public void init() {
        initialiseEmoteMenu();

        if (true) { //If is lobby leader
            startButton.setDisable(false);
            startButton.setVisible(true);
        }
        else {
            startButton.setDisable(true);
            startButton.setVisible(false);
        }
        //TODO
    }

    public void goBackToSplash() {
        mainCtrl.showSplash();
    }

    public void startGame() {
        //TODO
        return;
    }

    /**
     * Initialise all event handling for the emote menu
     */
    private void initialiseEmoteMenu() {
        emoteMenu.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                showEmotes(true);
            }
        });
        emoteBox.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                showEmotes(false);
            }
        });

        emoteButton1.setOnAction(e -> sendEmote(1));
        emoteButton2.setOnAction(e -> sendEmote(2));
        emoteButton3.setOnAction(e -> sendEmote(3));
        emoteButton4.setOnAction(e -> sendEmote(4));
        emoteButton5.setOnAction(e -> sendEmote(5));
        emoteButton6.setOnAction(e -> sendEmote(6));
    }

    /**
     * Sets the status of the emote buttons, either allows the user to view and click the emote buttons
     * or disables and hides the emote buttons
     *
     * @param enable value for enabled (true) or disabled (false)
     */
    private void showEmotes(boolean enable) {
        emoteMenu.setVisible(!enable);

        emoteButton1.setDisable(!enable);
        emoteButton1.setVisible(enable);
        emoteButton2.setDisable(!enable);
        emoteButton2.setVisible(enable);
        emoteButton3.setDisable(!enable);
        emoteButton3.setVisible(enable);
        emoteButton4.setDisable(!enable);
        emoteButton4.setVisible(enable);
        emoteButton5.setDisable(!enable);
        emoteButton5.setVisible(enable);
        emoteButton6.setDisable(!enable);
        emoteButton6.setVisible(enable);
    }

    /**
     * Once the player clicks an emote button, this method is called
     * It then sends the emote to the other players in the session
     *
     * @param emoteNumber identifies which emote button was clicked and what emote to send
     */
    private void sendEmote(int emoteNumber) {
        System.out.println("emote button pressed: " + emoteNumber); //DEBUG LINE
        //TODO
    }
}
