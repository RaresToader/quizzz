package client.scenes;

import client.Session;
import client.utils.Utils;
import commons.Emoji;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import commons.SessionLobbyStatus;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import kotlin.Triple;

import javax.inject.Inject;
import java.util.*;

public class MultiplayerLobbyCtrl {
    private MainCtrl mainCtrl;

    private boolean isLeader;
    private Timer playerUpdateTimer;
    private Timer numberOfPlayersTimer;
    private Triple<StackPane, Text, ImageView> ownPlayerTag;
    private LinkedHashMap<Triple<StackPane, Text, ImageView>, String> playerTags;

    private SessionLobbyStatus lobbyStatus;
    private ArrayList<Emoji> recentlyReceivedEmojis;
    private Timer removeEmojiTimer;

    private int transitionTimeLeft;

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
    private Label numberOfPlayersLabel;

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

    @FXML
    private ImageView playerEmote1;
    @FXML
    private ImageView playerEmote2;
    @FXML
    private ImageView playerEmote3;
    @FXML
    private ImageView playerEmote4;
    @FXML
    private ImageView playerEmote5;
    @FXML
    private ImageView playerEmote6;
    @FXML
    private ImageView playerEmote7;
    @FXML
    private ImageView playerEmote8;
    @FXML
    private ImageView playerEmote9;
    @FXML
    private ImageView playerEmote10;


    /**
     * Method which is called when the user switches to the screen
     * Initialises all components of the screen to either allow the player to interact with certain buttons
     * or to display other players
     */
    public void init() {

        // Reset tag data structures
        this.playerTags = new LinkedHashMap<>();
        this.ownPlayerTag = null;
        this.recentlyReceivedEmojis = new ArrayList<>();

        initialisePlayers();
        updateNumberOfPlayersLobby();
        initialiseEmoteMenu();

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
        if (removeEmojiTimer != null) {
            removeEmojiTimer.cancel();
        }
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

        System.out.println("\n\n\n\t\t\t" + Utils.startSession() + "\n\n\n");
    }

    /**
     * Initialise all player tags and display their names
     * Also initialises a timer that calls a series of update methods to display any new changes
     */
    private void initialisePlayers() {

        // Set up all the tags
        // Triple<StackPane, Text, ImageView>
        playerTags.put(new Triple<>(playerNameBackground1, playerNameLabel1, playerEmote1), null);
        playerTags.put(new Triple<>(playerNameBackground2, playerNameLabel2, playerEmote2), null);
        playerTags.put(new Triple<>(playerNameBackground3, playerNameLabel3, playerEmote3), null);
        playerTags.put(new Triple<>(playerNameBackground4, playerNameLabel4, playerEmote4), null);
        playerTags.put(new Triple<>(playerNameBackground5, playerNameLabel5, playerEmote5), null);
        playerTags.put(new Triple<>(playerNameBackground6, playerNameLabel6, playerEmote6), null);
        playerTags.put(new Triple<>(playerNameBackground7, playerNameLabel7, playerEmote7), null);
        playerTags.put(new Triple<>(playerNameBackground8, playerNameLabel8, playerEmote8), null);
        playerTags.put(new Triple<>(playerNameBackground9, playerNameLabel9, playerEmote9), null);
        playerTags.put(new Triple<>(playerNameBackground10, playerNameLabel10, playerEmote10), null);

        // Timer that regularly calls update methods
        playerUpdateTimer = new Timer();
        playerUpdateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                lobbyStatus = Utils.getLobbyStatus();

                if (Objects.equals(lobbyStatus.getGameAdmin(), Session.getNickname()) && !lobbyStatus.isStarted()) {
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
                    System.out.println("\n\n\n\t\t\tlobbyStatus.isStarted()\n\n\n");
                    startGame();
                }

            }

        }, 0, 20);

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

        // Set up the on action event for each emote button corresponding to their image
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
        Utils.setEmoji(Session.getNickname(), emoteType);
    }

    /**
     * Updates the number of players in the lobby every 2 seconds.
     **/
    private void updateNumberOfPlayersLobby() {
        numberOfPlayersTimer = new Timer();

        numberOfPlayersTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        List<String> list = Utils.getCurrentSessionPlayers();
                        int numberOfPlayers = list.size();
                        if (numberOfPlayers == 1) {
                            numberOfPlayersLabel.setText(numberOfPlayers + " player waiting in lobby");
                        }
                        else {
                            numberOfPlayersLabel.setText(numberOfPlayers + " players waiting in lobby");
                        }
                    }
                });
            }
        }, 0, 2*1000); //run every 2 seconds
    }


    /**
     * Receive any emotes sent by other players and display them next to the corresponding player tag
     */
    private void receiveEmotes() {
        List<Emoji> activeEmojiList = lobbyStatus.getEmojiList();

        // Loop through all active emojis and display them according to the user that sent it
        for (Emoji emoji : activeEmojiList) {

            // Prevent the emoji from being received again in the time that it has not expired
            if (recentlyReceivedEmojis.contains(emoji)) {
                continue;
            }
            recentlyReceivedEmojis.add(emoji);
            removeEmojiTimer = new Timer();
            removeEmojiTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    recentlyReceivedEmojis.remove(emoji);
                }
            }, 6000);

            
            String userApplying = emoji.getUserApplying();
            ImageView playerEmoteImageView = null;

            // Find the right player tag for the given user that applied the emoji
            for (Triple<StackPane, Text, ImageView> tag : playerTags.keySet()) {
                if (Objects.equals(userApplying, playerTags.get(tag))) {
                    playerEmoteImageView = tag.getThird();
                }
            }

            if (playerEmoteImageView != null) {
                String emotePNG;

                // Get the right emote image
                switch (emoji.getEmojiType()) {
                    case "smile" -> emotePNG = "/photos/emoteSmile.png";
                    case "sad" -> emotePNG = "/photos/emoteSad.png";
                    case "angry" -> emotePNG = "/photos/emoteAngry.png";
                    case "surprise" -> emotePNG = "/photos/emoteSurprise.png";
                    case "celebrate" -> emotePNG = "/photos/emoteCelebrate.png";
                    case "sunglasses" -> emotePNG = "/photos/emoteSunglasses.png";
                    default -> throw new IllegalStateException("Unexpected value: " + emoji.getEmojiType());
                }

                playerEmoteImageView.setImage(new Image(emotePNG));

                playerEmoteImageView.setOpacity(1);
                FadeTransition emoteFadeOut = new FadeTransition(Duration.seconds(2), playerEmoteImageView);
                emoteFadeOut.setFromValue(1);
                emoteFadeOut.setToValue(0);
                emoteFadeOut.setDelay(Duration.seconds(2));

                emoteFadeOut.play();
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
        for (Triple<StackPane, Text, ImageView> tag : playerTags.keySet()) {
            // If the player is no longer in the session's player list
            if (!playerList.contains(playerTags.get(tag))) {
                playerTags.replace(tag, null);
            }

            // If it is the player's tag, and it has not been set yet
            if (Objects.equals(tag.getSecond().getText(), Session.getNickname()) && ownPlayerTag == null) {
                ownPlayerTag = tag;
                ownPlayerTag.getSecond().setFill(Color.web("#f15025"));
            }

            // If it is the tag of the lobby leader
            if (Objects.equals(tag.getSecond().getText(), lobbyStatus.getGameAdmin())) {
                leaderCrown.setX(tag.getFirst().getLayoutX() - 100);
                leaderCrown.setY(tag.getFirst().getLayoutY() - 25);
            }
        }

        // Add any new players to a tag
        for (String player : playerList) {

            if (!playerTags.containsValue(player)) { // If the player is not yet displayed
                for (Triple<StackPane, Text, ImageView> tag : playerTags.keySet()) {
                    if (playerTags.get(tag) == null) { // If the player tag is empty
                        playerTags.replace(tag, player);
                        tag.getSecond().setText(player); // Set text to display players name
                        break; // Player has already been assigned a tag so break out of the inner loop
                    }
                }
            }
        }
    }

    /**
     * Once the lobby leader has started the game, this method is called for all other players in the session
     */
    @FXML
    private void startGame() {
        leaveButton.setDisable(true);
        leaveButton.setVisible(false);
        startButton.setDisable(true);
        playerUpdateTimer.cancel();
        if (removeEmojiTimer != null) {
            removeEmojiTimer.cancel();
        }

        System.out.println("\n\n\n\t\t\tIN startGame()\n\n\n");

        transitionTimeLeft = 5;
        Timeline transitionTimer = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> {
                            System.out.println("transitionTimeLeft = " + transitionTimeLeft); //DEBUG LINE
                            label1.setText("Game starting in " + transitionTimeLeft);

                            if (transitionTimeLeft <= 0) {
                                startButton.setDisable(false);
                                mainCtrl.showMultiplayer();
                            } else {
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
