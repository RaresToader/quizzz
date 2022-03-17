package client.scenes;

import client.utils.ServerUtils;
import commons.Activity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.inject.Inject;

public class AdminPanelCtrl {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
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

    @Inject
    public AdminPanelCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }

    public void goBackToSplash() {
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
    }
}
