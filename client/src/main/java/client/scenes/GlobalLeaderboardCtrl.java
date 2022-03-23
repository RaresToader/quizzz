package client.scenes;

import client.utils.ServerUtils;
import commons.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.inject.Inject;

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
    @FXML
    private Button playAgain;
    @FXML
    private BarChart<String,Long> barChart;
    @FXML
    private CategoryAxis playerAxis;
    @FXML
    private NumberAxis scoreAxis;
    @Inject
    public GlobalLeaderboardCtrl(MainCtrl mainCtrl,ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }

    public void goBackToSplash() {
        mainCtrl.showSplash();
    }

    public void init() {
        nameColumn.setCellValueFactory(new PropertyValueFactory("username"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory("score"));
        ObservableList<Player> players = FXCollections.observableArrayList();
        players.addAll(serverUtils.getLeaderboardPlayers());
        tableView.setItems(players);
        barChart.
    }

    public void newGame(){
        mainCtrl.showSingleplayer();
    }
    public void buttonOnOrOff(boolean isFromSplash) {
        this.playAgain.setVisible(!isFromSplash);
        this.playAgain.setDisable(isFromSplash);
    }
}
