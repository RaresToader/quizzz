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
    private Button emoteButtonSmile;
    @FXML
    private Button emoteButtonSad;
    @FXML
    private Button emoteButtonAngry;
    @FXML
    private Button emoteButtonSurprise;
    @FXML
    private Button emoteButtonCelebrate;
    @FXML
    private Button emoteButtonSunglasses;

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

        emoteButtonSmile.setOnAction(e -> sendEmote("smile"));
        emoteButtonSad.setOnAction(e -> sendEmote("sad"));
        emoteButtonAngry.setOnAction(e -> sendEmote("angry"));
        emoteButtonSurprise.setOnAction(e -> sendEmote("surprise"));
        emoteButtonCelebrate.setOnAction(e -> sendEmote("celebrate"));
        emoteButtonSunglasses.setOnAction(e -> sendEmote("sunglasses"));
    }

    /**
     * Sets the status of the emote buttons, either allows the user to view and click the emote buttons
     * or disables and hides the emote buttons
     *
     * @param enable value for enabled (true) or disabled (false)
     */
    private void showEmotes(boolean enable) {
        emoteMenu.setVisible(!enable);

        emoteButtonSmile.setDisable(!enable);
        emoteButtonSmile.setVisible(enable);
        emoteButtonSad.setDisable(!enable);
        emoteButtonSad.setVisible(enable);
        emoteButtonAngry.setDisable(!enable);
        emoteButtonAngry.setVisible(enable);
        emoteButtonSurprise.setDisable(!enable);
        emoteButtonSurprise.setVisible(enable);
        emoteButtonCelebrate.setDisable(!enable);
        emoteButtonCelebrate.setVisible(enable);
        emoteButtonSunglasses.setDisable(!enable);
        emoteButtonSunglasses.setVisible(enable);
    }

    /**
     * Once the player clicks an emote button, this method is called
     * It then sends the emote to the other players in the session
     *
     * @param emoteType identifies which emote button was clicked and what emote to send
     */
    private void sendEmote(String emoteType) {
        System.out.println("emote button pressed: " + emoteType); //DEBUG LINE
        //TODO
    }
}
