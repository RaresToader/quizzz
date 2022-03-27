package client.scenes;

import client.Session;
import client.utils.Utils;
import commons.Emoji;
import commons.SessionLobbyStatus;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

import javax.inject.Inject;
import java.util.*;

public class MultiplayerLobbyCtrl {
    private MainCtrl mainCtrl;

    private boolean isLeader;
    private Timer playerUpdateTimer;
    private Pair<StackPane, Text> ownPlayerTag;
    private LinkedHashMap<Pair<StackPane, Text>, String> playerTags;

    private SessionLobbyStatus lobbyStatus;

    private int transitionTimeLeft;
    private Timeline transitionTimer;

    @Inject
    public MultiplayerLobbyCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    @FXML
    private Button startButton;
    @FXML
    private Button leaveButton;
    @FXML
    private Label label1;
    @FXML
    private ImageView leaderCrown;

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

    @FXML
    private StackPane playerNameBackground1;
    @FXML
    private Text playerNameLabel1;
    @FXML
    private StackPane playerNameBackground2;
    @FXML
    private Text playerNameLabel2;
    @FXML
    private StackPane playerNameBackground3;
    @FXML
    private Text playerNameLabel3;
    @FXML
    private StackPane playerNameBackground4;
    @FXML
    private Text playerNameLabel4;
    @FXML
    private StackPane playerNameBackground5;
    @FXML
    private Text playerNameLabel5;
    @FXML
    private StackPane playerNameBackground6;
    @FXML
    private Text playerNameLabel6;
    @FXML
    private StackPane playerNameBackground7;
    @FXML
    private Text playerNameLabel7;
    @FXML
    private StackPane playerNameBackground8;
    @FXML
    private Text playerNameLabel8;
    @FXML
    private StackPane playerNameBackground9;
    @FXML
    private Text playerNameLabel9;
    @FXML
    private StackPane playerNameBackground10;
    @FXML
    private Text playerNameLabel10;

    /**
     * Method which is called when the user switches to the screen
     * Initialises all components of the screen to either allow the player to interact with certain buttons
     * or to display other players
     */
    public void init() {

        // Reset tag data structures
        this.playerTags = new LinkedHashMap<>();
        this.ownPlayerTag = null;

        initialisePlayers();

        initialiseEmoteMenu();

        leaderCrown.resize(50, 50);

        if (isLeader) {
            startButton.setDisable(false);
            startButton.setVisible(true);
        }
        else {
            startButton.setDisable(true);
            startButton.setVisible(false);
        }
    }

    /**
     * Leave the lobby session
     */
    public void goBackToSplash() {
        playerUpdateTimer.cancel();
        setLeader(false);

        Utils.leaveSession();
        mainCtrl.showSplash();
    }

    /**
     * The lobby leader is able to press the start game button, this method is then called
     * Which tells the other players in the session and the session itself that the game is starting
     */
    public void startGameButtonPressed() {
        startButton.setDisable(true);
        startButton.setVisible(false);

        Utils.startSession();
    }

    /**
     * Initialise all player tags and display their names
     * Also initialises a timer that calls a series of update methods to display any new changes
     */
    private void initialisePlayers() {

        playerTags.put(new Pair<>(playerNameBackground1, playerNameLabel1), null);
        playerTags.put(new Pair<>(playerNameBackground2, playerNameLabel2), null);
        playerTags.put(new Pair<>(playerNameBackground3, playerNameLabel3), null);
        playerTags.put(new Pair<>(playerNameBackground4, playerNameLabel4), null);
        playerTags.put(new Pair<>(playerNameBackground5, playerNameLabel5), null);
        playerTags.put(new Pair<>(playerNameBackground6, playerNameLabel6), null);
        playerTags.put(new Pair<>(playerNameBackground7, playerNameLabel7), null);
        playerTags.put(new Pair<>(playerNameBackground8, playerNameLabel8), null);
        playerTags.put(new Pair<>(playerNameBackground9, playerNameLabel9), null);
        playerTags.put(new Pair<>(playerNameBackground10, playerNameLabel10), null);

        // Timer that regularly calls update methods
        playerUpdateTimer = new Timer();
        playerUpdateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                lobbyStatus = Utils.getLobbyStatus();

                if (Objects.equals(lobbyStatus.getGameAdmin(), Session.getNickname())) {
                    setLeader(true);
                    startButton.setDisable(false);
                    startButton.setVisible(true);
                }
                else {
                    setLeader(false);
                    startButton.setDisable(true);
                    startButton.setVisible(false);
                }

                playerUpdate();

                receiveEmotes();

                if (lobbyStatus.isStarted()) {
                    startGame();
                }

            }

        }, 0, 1000);

    }

    /**
     * Initialise all event handling for the emote menu
     */
    private void initialiseEmoteMenu() {
        // Emote button, once hovered will display the other emote buttons
        emoteMenu.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                showEmotes(true);
            }
        });
        // Invisible pane that acts as a hitbox to detect when the mouse is no longer hovering the emote buttons
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
        System.out.println("Emote send confirmation: " + Utils.setEmoji(Session.getNickname(), emoteType));
    }

    /**
     * Receive any emotes sent by other players and display them next to the corresponding player tag
     */
    private void receiveEmotes() {
        List<Emoji> activeEmojiList = Utils.getLobbyStatus().getEmojiList();
        System.out.println("\nEMOJI LIST: " + activeEmojiList + "\n");

        // Loop through all active emojis and display them according to the user that sent it
        for (Emoji emoji : activeEmojiList) {
            System.out.println("\nActive emoji: " + emoji.getEmojiType() + "\n");

            String userApplying = emoji.getUserApplying();
            StackPane playerLabel = null;

            // Find the right player tag for the given user that applied the emoji
            for (Pair<StackPane, Text> tag : playerTags.keySet()) {
                if (Objects.equals(userApplying, tag.getValue().getText())) {
                    playerLabel = tag.getKey();
                }
            }
            if (playerLabel != null) {
                Text test = new Text(
                        playerLabel.getLayoutX() + 350, playerLabel.getLayoutY(),
                        userApplying + " used: " + emoji.getEmojiType());
                test.setVisible(true);
            }
        }
    }

    /**
     * Constantly called method to update any changes in the players in the lobby
     * Their player tags, who the lobby leader is and which tag is their own
     */
    private void playerUpdate() {
        List<String> playerList = Utils.getCurrentSessionPlayers();

        // Run through all players and player labels and check if the players are still in the session
        for (Pair<StackPane, Text> tag : playerTags.keySet()) {
            // If the player is no longer in the session's player list
            if (!playerList.contains(playerTags.get(tag))) {
                playerTags.replace(tag, null);
            }

            // If it is the player's tag, and it has not been set yet
            if (Objects.equals(tag.getValue().getText(), Session.getNickname()) && ownPlayerTag == null) {
                ownPlayerTag = tag;
                ownPlayerTag.getValue().setFill(Color.web("#f15025"));
            }

            // If it is the tag of the lobby leader
            if (Objects.equals(tag.getValue().getText(), lobbyStatus.getGameAdmin())) {
                leaderCrown.setX(tag.getKey().getLayoutX() - 100);
                leaderCrown.setY(tag.getKey().getLayoutY());
            }
        }

        // Add any new players to a tag
        for (String player : playerList) {

            if (!playerTags.containsValue(player)) { // If the player is not yet displayed
                for (Pair<StackPane, Text> tag : playerTags.keySet()) {
                    if (playerTags.get(tag) == null) { // If the player tag is empty
                        playerTags.replace(tag, player);
                        tag.getValue().setText(player); // Set text to display players name
                        break; // Player has already been assigned a tag so break out of the inner loop
                    }
                }
            }
        }
    }

    /**
     * Once the lobby leader has started the game, this method is called for all other players in the session
     */
    private void startGame() {
        leaveButton.setDisable(true);
        leaveButton.setVisible(false);


        transitionTimeLeft = 5;
        transitionTimer = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> {
                            System.out.println("transitionTimeLeft = " + transitionTimeLeft); //DEBUG LINE
                            label1.setText("Game starting in " + transitionTimeLeft);

                            if (transitionTimeLeft == 0) {
                                playerUpdateTimer.cancel();
                                //TODO
                                // Start the game locally
                            }
                            else {
                                transitionTimeLeft -= 1;
                            }
                        }
                )
        );
        transitionTimer.setCycleCount(6);
        transitionTimer.play();
    }

    /**
     * Set the value to distinguish the current player is the lobby leader
     * @param leader is true if the player is the lobby leader and false otherwise
     */
    public void setLeader(boolean leader) {
        this.isLeader = leader;
    }

    /**
     * Get boolean of if the player is lobby leader
     * @return a boolean value, true if the player is the lobby leader and false otherwise
     */
    public boolean getLeader() {
        return this.isLeader;
    }
}
