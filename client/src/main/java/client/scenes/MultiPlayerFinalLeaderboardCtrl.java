package client.scenes;

import client.Session;
import client.utils.ServerUtils;
import client.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MultiPlayerFinalLeaderboardCtrl {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private List<Label> playerList;
    @FXML
    private Label p1;
    @FXML
    private Label p2;
    @FXML
    private Label p3;
    @FXML
    private Label p4;
    @FXML
    private Label p5;
    @FXML
    private Label p6;
    @FXML
    private Label p7;
    @FXML
    private Label p8;
    @FXML
    private Label p9;
    @FXML
    private Label p10;


    @Inject
    public MultiPlayerFinalLeaderboardCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
        playerList = new ArrayList<>();
        playerList.add(p1);
        playerList.add(p2);
        playerList.add(p3);
        playerList.add(p4);
        playerList.add(p5);
        playerList.add(p6);
        playerList.add(p7);
        playerList.add(p8);
        playerList.add(p9);
        playerList.add(p10);

    }

    public void init(){

        playerList = new ArrayList<>();
        playerList.add(p1);
        playerList.add(p2);
        playerList.add(p3);
        playerList.add(p4);
        playerList.add(p5);
        playerList.add(p6);
        playerList.add(p7);
        playerList.add(p8);
        playerList.add(p9);
        playerList.add(p10);
        List<Map.Entry<String,Integer>> players = Utils.getCurrentLeaderBoard(Session.getNickname());
        for(int i = 0;i< playerList.size();i++) {
            playerList.get(i).setText("");
        }
        for (int i = 0; i < players.size();i++) {
            playerList.get(i).setText(players.get(i).getKey() + ": " + players.get(i).getValue());
        }
        Utils.leaveSession();
    }

    /**
     * goes back to splash
     */
    public void goBack() {
        mainCtrl.showSplash();
        for(int i = 0;i< playerList.size();i++) {
            playerList.get(i).setText("");
        }
    }

    /**
     * puts the  player back into the multiplayer lobby
     */
    public void playAgain() {
        for(int i = 0;i< playerList.size();i++) {
            playerList.get(i).setText("");
        }
        mainCtrl.goToMultiplayerQueue();
    }
}
