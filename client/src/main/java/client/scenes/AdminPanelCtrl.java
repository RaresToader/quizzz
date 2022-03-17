package client.scenes;

import client.utils.ServerUtils;
import commons.Activity;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;

public class AdminPanelCtrl {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private Timer refreshActivities;
    @FXML
    private TableView<Activity> activitiesTable;
    @FXML
    private TableColumn<Activity, String> idColumn;
    @FXML
    private TableColumn<Activity, Long> consumptionColumn;
    @FXML
    private TableColumn<Activity, String> imagePathColumn;
    @FXML
    private TableColumn<Activity, String> sourceColumn;
    @FXML
    private TableColumn<Activity, String> titleColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private AnchorPane addAnchorPlane;

    @Inject
    public AdminPanelCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }

    public void goBackToSplash() {
        refreshActivities.cancel();
        mainCtrl.showSplash();
    }

    public void initialise() {
        idColumn.setCellValueFactory(new PropertyValueFactory("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory("title"));
        consumptionColumn.setCellValueFactory(new PropertyValueFactory("consumption_in_wh"));
        imagePathColumn.setCellValueFactory(new PropertyValueFactory("image_path"));
        sourceColumn.setCellValueFactory(new PropertyValueFactory("source"));
        ObservableList<Activity> activities = FXCollections.observableArrayList();
        activities.addAll(serverUtils.getAllActivities());
        activitiesTable.setItems(activities);
        refreshActivities = new Timer();
        refreshActivities.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> activitiesTable.setItems(FXCollections.observableArrayList(serverUtils.getAllActivities())));
            }
        }, 0, 5 * 1000);

    }
    public void hideButtonsAndTable(){
        addButton.setVisible(false);
        addButton.setDisable(true);
        editButton.setVisible(false);
        editButton.setDisable(true);
        deleteButton.setVisible(false);
        deleteButton.setDisable(true);
        activitiesTable.setVisible(false);
        activitiesTable.setDisable(true);

    }
    public void showButtonsAndTable(){
        addButton.setVisible(true);
        addButton.setDisable(false);
        editButton.setVisible(true);
        editButton.setDisable(false);
        deleteButton.setVisible(true);
        deleteButton.setDisable(false);
        activitiesTable.setVisible(true);
        activitiesTable.setDisable(false);

    }

    public void addButton(){
        hideButtonsAndTable();
        addAnchorPlane.setVisible(true);

    }
    public void okButton(){
        showButtonsAndTable();
        addAnchorPlane.setVisible(false);
    }
}
