package client.scenes;

import client.utils.ServerUtils;
import commons.Player;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class GlobalLeaderboardCtrl {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    @FXML
    private Button goBack;
    @FXML
    private TableView<Player> tableView;
    @FXML
    private TableColumn<Player,String> nameColumn;
    @FXML
    private TableColumn<Player,Long> scoreColumn;

    @Inject
    public GlobalLeaderboardCtrl(MainCtrl mainCtrl,ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }

    public void goBackToSplash() {
        mainCtrl.showSplash();
    }

    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory("username"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory("score"));
        ObservableList<Player> players = FXCollections.observableArrayList();
        serverUtils.addPlayer("jon");
        serverUtils.addPlayer("Rares");
        System.out.println(serverUtils.getAllPlayers());
        players.addAll(serverUtils.getAllPlayers());
        tableView.setItems(players);
    }
}
