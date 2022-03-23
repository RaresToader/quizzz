package client.scenes;

import client.utils.ServerUtils;
import commons.Activity;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    @FXML
    private Button okButton;
    @FXML
    private Button confirmDeleteButton;
    @FXML
    private Button cancelDeleteButton;
    @FXML
    private AnchorPane confirmDeleteAnchor;
    @FXML
    private Label deleteNameLabel;

    @Inject
    public AdminPanelCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }

    public void backButton() {
        if (addAnchorPlane.isVisible()) {
            showButtonsAndTable();
        }
        else{
            showButtonsAndTable();
            refreshActivities.cancel();
            mainCtrl.showSplash();
        }
    }

    /**
     * This method initializes all the Activities in the table on the admin panel screen.
     */

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

    /**
     * This method hides the buttons and the table with all the activities
     * and shows the field in which you can add a new activity.
     */

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
        addAnchorPlane.setVisible(true);
        addAnchorPlane.setDisable(false);

    }

    /**
     * This method does the opposite of the method above:
     * It switches the 'Admin-Panel-screen' back to the beginning state.
     */

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
        addAnchorPlane.setVisible(false);
        addAnchorPlane.setDisable(true);

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
        if (ID.length() == 0) {
            id.clear();
            id.setPromptText("Please enter a valid id!");
            return;
        }
        if (serverUtils.doesActivityExist(ID) && !id.isDisabled()) {
            id.clear();
            id.setPromptText("ID already exists!");
            return;
        }
        if (activityTitle.length() == 0) {
            title.clear();
            title.setPromptText("Please enter a valid title!");
            return;
        }
        if (!NumberUtils.isParsable(consumption_in_wh) || StringUtils.contains(consumption_in_wh, ".")) {
            consumption.clear();
            consumption.setPromptText("Please enter a valid number!");
            return;
        }
        if (imagePathing.length() == 0) {
            imagePath.clear();
            imagePath.setPromptText("Please enter a valid image path!");
            return;
        }
        if (activitySource.length() == 0) {
            source.clear();
            source.setPromptText("Please enter a valid source!");
            return;
        }
        if (!id.isDisabled())
            serverUtils.addActivity(new Activity(ID, imagePathing, activityTitle, Long.parseLong(consumption_in_wh), activitySource));
        else {
            serverUtils.modifyActivity(new Activity(ID, imagePathing, activityTitle, Long.parseLong(consumption_in_wh), activitySource));
            id.setDisable(false);
        }
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
     * They will be displayed again in the table due to constant polling
     */
    public void setLoadActivities() {
        serverUtils.loadActivitiesInRepo();
    }


    /**
     * Modifies the selected activity
     */
    public void modifyActivity() {
        Activity activity = activitiesTable.getSelectionModel().getSelectedItem();
        if (activity == null)
            return;
        hideButtonsAndTable();
        addAnchorPlane.setVisible(true);
        id.setText(activity.getId());
        id.setDisable(true); // you cannot modify the id of the activity
        title.setText(activity.getTitle());
        consumption.setText("" + activity.getConsumption_in_wh());
        imagePath.setText(activity.getImage_path());
        source.setText(activity.getSource());
    }
    public void showConfirmDelete(){
        confirmDeleteAnchor.setDisable(false);
        confirmDeleteAnchor.setVisible(true);
        confirmDeleteButton.setDisable(false);
        confirmDeleteButton.setVisible(true);
        cancelDeleteButton.setDisable(false);
        confirmDeleteButton.setVisible(true);
    }
    public void hideConfirmDelete(){
        confirmDeleteAnchor.setDisable(true);
        confirmDeleteAnchor.setVisible(false);
        confirmDeleteButton.setDisable(true);
        confirmDeleteButton.setVisible(false);
        cancelDeleteButton.setDisable(true);
        confirmDeleteButton.setVisible(false);
    }

    public void delete(){
        Activity activity = activitiesTable.getSelectionModel().getSelectedItem();
        if(activity == null) {
           return;
        }
        else{
            showConfirmDelete();
            hideButtonsAndTable();
            String ID = activitiesTable.getSelectionModel().getSelectedItem().getId();
            deleteNameLabel.setText(ID);
        }
    }

    public void confirmDelete(){
        Activity activity = activitiesTable.getSelectionModel().getSelectedItem();
//        serverUtils.deleteActivity(activity.getId());
        hideConfirmDelete();
        showButtonsAndTable();

    }



}
