package client.scenes;

import client.utils.ServerUtils;
import commons.Player;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;

public class GlobalLeaderboardCtrl {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private Timer refreshLeaderboardAndBarChart;
    @FXML
    private Button goBack;
    @FXML
    private TableView<Player> tableView;
    @FXML
    private TableColumn<Player, String> nameColumn;
    @FXML
    private TableColumn<Player, Long> scoreColumn;
    @FXML
    private Button playAgain;
    @FXML
    private BarChart<String, Long> barChart;
    @FXML
    private CategoryAxis playerAxis;
    @FXML
    private NumberAxis scoreAxis;

    @Inject
    public GlobalLeaderboardCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
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
        barChart.setTitle("Score barchart");
        XYChart.Series series1 = new XYChart.Series();
        for (Player player : players) {
            series1.getData().add(new XYChart.Data(player.getUsername(), player.getScore()));
        }
        barChart.getData().addAll(series1);
        refreshLeaderboardAndBarChart = new Timer();
        refreshLeaderboardAndBarChart.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    tableView.setItems(FXCollections.observableArrayList(serverUtils.getLeaderboardPlayers()));
                });
            }
        }, 0, 5 * 1000);
    }

    public void newGame() {
        mainCtrl.showSingleplayer();
    }

    public void buttonOnOrOff(boolean isFromSplash) {
        this.playAgain.setVisible(!isFromSplash);
        this.playAgain.setDisable(isFromSplash);
    }
}
