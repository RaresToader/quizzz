package client.scenes;

import client.utils.ServerUtils;
import client.utils.Utils;
import commons.Activity;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
 import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

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
    private Button loadActivities;
    @FXML
    private AnchorPane addAnchorPlane;
    @FXML
    private TextField id;
    @FXML
    private TextField title;
    @FXML
    private TextField consumption;
    @FXML
    private TextField imagePath;
    @FXML
    private TextField source;

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

    public void hideButtonsAndTable() {
        addButton.setVisible(false);
        addButton.setDisable(true);
        editButton.setVisible(false);
        editButton.setDisable(true);
        loadActivities.setVisible(false);
        loadActivities.setDisable(true);
        deleteButton.setVisible(false);
        deleteButton.setDisable(true);
        activitiesTable.setVisible(false);
        activitiesTable.setDisable(true);

    }

    public void showButtonsAndTable() {
        addButton.setVisible(true);
        addButton.setDisable(false);
        editButton.setVisible(true);
        editButton.setDisable(false);
        loadActivities.setVisible(true);
        loadActivities.setDisable(false);
        deleteButton.setVisible(true);
        deleteButton.setDisable(false);
        activitiesTable.setVisible(true);
        activitiesTable.setDisable(false);

    }

    public void addButton() {
        hideButtonsAndTable();
        addAnchorPlane.setVisible(true);

    }

    public void okButton() {
        String ID = id.getText();
        String activityTitle = title.getText();
        String consumption_in_wh = consumption.getText();
        String imagePathing = imagePath.getText();
        String activitySource = source.getText();
        if (!NumberUtils.isParsable(consumption_in_wh) || StringUtils.contains(consumption_in_wh, ".")) {
            addButton();
            consumption.clear();
            consumption.setPromptText("Please enter a valid number!");
        }
        if (!Utils.isAlphaNumeric(ID) || ID.length()==0) {
            addButton();
            id.clear();
            id.setPromptText("Please enter a valid id!");
        }
        if (!Utils.isAlphaNumeric(activityTitle) ||activityTitle.length()==0) {
            addButton();
            title.clear();
            title.setPromptText("Please enter a valid title!");
        }
        if (!Utils.isAlphaNumeric(imagePathing) || imagePathing.length()==0) {
            addButton();
            imagePath.clear();
            imagePath.setPromptText("Please enter a valid image path!");
        }
        if (!Utils.isAlphaNumeric(activitySource)|| activitySource.length()==0) {
            addButton();
            source.clear();
            source.setPromptText("Please enter a valid source!");
        }
        if (serverUtils.doesActivityExist(ID)) {
            addButton();
            id.clear();
            id.setPromptText("ID already exists!");
        }
        serverUtils.addActivity(new Activity(ID, activityTitle, imagePathing, Long.parseLong(consumption_in_wh), activitySource));
        id.clear();
        title.clear();
        imagePath.clear();
        consumption.clear();
        source.clear();
        showButtonsAndTable();
        addAnchorPlane.setVisible(false);
    }

    /**
     * Method that loads again the activities from activities.json
     * TODO
     */
    public void setLoadActivities() {

    }

}
