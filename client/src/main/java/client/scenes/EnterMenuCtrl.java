package client.scenes;

import client.Session;
import client.utils.ServerUtils;
import client.utils.Utils;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class EnterMenuCtrl {

    private static final String usernameFileName = "username.log";

    private final MainCtrl mainCtrl;
    private final MultiplayerLobbyCtrl mLobbyCtrl;
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
    public EnterMenuCtrl(MainCtrl mainCtrl, MultiplayerLobbyCtrl multiplayerLobbyCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.mLobbyCtrl = multiplayerLobbyCtrl;
        this.serverUtils = serverUtils;

    }

    public void initialize() {
        username.setText(EnterMenuCtrl.getUsernameFromFile());
    }

    @FXML
    public void cleanText() {
        username.clear();
        server.clear();
    }

    public void enterButton() {
        String nickname = username.getText();
        String serverAddr = server.getText();
        if (nickname != null && nickname.length() > 0 && Utils.isAlphaNumeric(nickname)) { //Invalid nickname
            if (nickname.length() > 8) { //Invalid nickname length
                displayErrorText("Username is too long! (max 8 characters)");
                return;
            }
            if (serverAddr == null || serverAddr.length() == 0 || !Utils.validateServer(serverAddr)) { //Invalid serverAddr
                displayErrorText("Server address is invalid!");
                return;
            }

            if (!serverUtils.isUsernameValid(nickname)) {
                displayErrorText("This username is currently in use!");
                return;
            }



            EnterMenuCtrl.saveUsernameToFile(nickname);
            Session.setNickname(nickname);
            this.serverUtils.addPlayer(nickname);
            Session.setServerAddr(serverAddr);
            mainCtrl.showSplash();

            if (Utils.getCurrentSessionPlayers().size() <= 1) {
                mLobbyCtrl.setLeader(true);
            }
        }
        else {
            displayErrorText("Provided username is invalid!");
        }
    }

    /**
     * Gets username from file
     * @return String value of saved username ("" - empty string if empty file)
     */
    private static String getUsernameFromFile() {
        try {
            File myObj = new File(usernameFileName);
            Scanner myReader = new Scanner(myObj);
            String data = "";
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * Saves username into file for further usage
     * @param username - username to be saved
     * @return Boolean value whether operation was successful or not
     */
    private static boolean saveUsernameToFile(String username) {
        try {
            File usernameFile = new File(usernameFileName);
            usernameFile.createNewFile(); //Only creates new file if exists
            FileWriter myWriter = new FileWriter(usernameFileName);
            myWriter.write(username);
            myWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void displayErrorText(String text) {
        errorText.setText(text);
        errorText.setOpacity(1);
        FadeTransition errorFade = new FadeTransition(Duration.seconds(4), errorText);
        errorFade.setFromValue(1);
        errorFade.setToValue(0);
        errorFade.play();
    }
}
