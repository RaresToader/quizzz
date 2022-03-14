package client.scenes;

import client.utils.ServerUtils;
import commons.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
        serverUtils.addPlayer("Rxares");
        serverUtils.addPlayer("Racres");
        serverUtils.addPlayer("Rarevs");
        serverUtils.addPlayer("Rareys");
        serverUtils.addPlayer("Raresy");
        serverUtils.addPlayer("Raresgg");
        serverUtils.addPlayer("Rarefddss");
        serverUtils.addPlayer("Rarsses");
        serverUtils.addPlayer("Rarswwes");
        serverUtils.addPlayer("Raryyes");
        serverUtils.addPlayer("Rarghjes");
        serverUtils.addPlayer("Ratrdres");
        serverUtils.addPlayer("Rarefs");
        serverUtils.addPlayer("Rares");
        serverUtils.addPlayer("Rarrhes");
        serverUtils.addPlayer("Rrares");
        serverUtils.addPlayer("Rares");
        serverUtils.addPlayer("Ragfsdrres");
        serverUtils.addPlayer("Rarefdsfds");
        serverUtils.addPlayer("Rarees");
        serverUtils.addPlayer("Rasdserres");
        serverUtils.addPlayer("Rarssses");
        serverUtils.addPlayer("Rarfghes");
        serverUtils.addPlayer("Rareius");
        serverUtils.addPlayer("Rareys");
        serverUtils.addPlayer("Raresu");
        players.addAll(serverUtils.getLeaderboardPlayers());
        tableView.setItems(players);
    }
}
