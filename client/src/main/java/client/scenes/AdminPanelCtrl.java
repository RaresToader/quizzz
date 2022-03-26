package client.scenes;

import client.utils.ServerUtils;
import commons.Activity;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

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
    @FXML
    private Button backButton;
    @FXML
    private TextField searchBar;

    @Inject
    public AdminPanelCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }

    public void backButton() {
        if (addAnchorPlane.isVisible()) {
            showButtonsAndTable();
            hideConfirmDelete();
        } else {
            hideConfirmDelete();
            showButtonsAndTable();
            refreshActivities.cancel();
            mainCtrl.showSplash();
        }
        activitiesTable.getSelectionModel().clearSelection();
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
        AtomicReference<ObservableList<Activity>> activities = new AtomicReference<>(FXCollections.observableArrayList());
        activities.get().addAll(serverUtils.getAllActivities());
        activitiesTable.setItems(activities.get());
        /**
         * Credits to this youtube video for explaining how to do it
         * https://www.youtube.com/watch?v=FeTrcNBVWtg
         */
        FilteredList<Activity> filteredData = new FilteredList<>(activities.get(), b -> true);
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(activity -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (activity.getId().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches activity id.
                } else if (activity.getTitle().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches the title of the activity.
                } else if (String.valueOf(activity.getConsumption_in_wh()).indexOf(lowerCaseFilter) != -1) {
                    return true; //Filter matches the consumption.
                } else if (activity.getSource().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; //Filter matches the source.
                } else if (activity.getImage_path().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; //Filter matches the image path.
                }
                return false; // Does not match.
            });
        });
        SortedList<Activity> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(activitiesTable.comparatorProperty());
        activitiesTable.setItems(sortedData);
        refreshActivities = new Timer();
        refreshActivities.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    ObservableList<Activity> activitiesCopy = FXCollections.observableArrayList(serverUtils.getAllActivities());
                    if (!activitiesCopy.equals(activities.get())) {  ///only refresh table if there are new activities
                        activitiesTable.setItems(activitiesCopy);
                        activities.set(activitiesCopy);
                    }

                });
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

    /**
     * Pops the add-button anchor-up
     */
    public void addButton() {
        hideButtonsAndTable();
        addAnchorPlane.setVisible(true);

    }

    /**
     * Adds or updates the activity with all the filled in elements after okButton is clicked.
     * Then sets the admin-panel screen back to normal state
     */
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
        activitiesTable.getSelectionModel().clearSelection();
    }

    /**
     * Method that loads again the activities from activities.json
     * They will be displayed again in the table due to constant polling
     */
    public void setLoadActivities() {
        activitiesTable.getSelectionModel().clearSelection();
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

    /**
     * Shows and enables every element of the confirmDelete-pop-up.
     */
    public void showConfirmDelete() {
        confirmDeleteAnchor.setDisable(false);
        confirmDeleteAnchor.setVisible(true);
        confirmDeleteButton.setDisable(false);
        confirmDeleteButton.setVisible(true);
        cancelDeleteButton.setDisable(false);
        confirmDeleteButton.setVisible(true);
    }

    /**
     * Hides and disables every element of the confirmDelete-pop-up.
     */
    public void hideConfirmDelete() {
        confirmDeleteAnchor.setDisable(true);
        confirmDeleteAnchor.setVisible(false);
        confirmDeleteButton.setDisable(true);
        confirmDeleteButton.setVisible(false);
        cancelDeleteButton.setDisable(true);
        confirmDeleteButton.setVisible(false);
    }

    /**
     * Checks if there is an Activity selected and if so pop's up the confirmDelete 'Pop-up'
     */
    public void delete() {
        Activity activity = activitiesTable.getSelectionModel().getSelectedItem();
        if (activity == null) {
            return;
        } else {
            showConfirmDelete();
            hideButtonsAndTable();
            backButton.setVisible(false);
            backButton.setDisable(true);
            String ID = activitiesTable.getSelectionModel().getSelectedItem().getId();
            deleteNameLabel.setText(ID);
        }
    }

    /**
     * Deletes Activity from Activity-Bank after the confirmDeleteButton is clicked.
     */
    public void confirmDelete() {
        Activity activity = activitiesTable.getSelectionModel().getSelectedItem();
        serverUtils.deleteActivity(activity.getId());
        hideConfirmDelete();
        showButtonsAndTable();
        backButton.setVisible(true);
        backButton.setDisable(false);
        activitiesTable.getSelectionModel().clearSelection();
    }

    /**
     * Cancels deleted and set admin screen back to normal state. After cancelDeleteButton is clicked.
     */
    public void cancelDelete() {
        activitiesTable.getSelectionModel().clearSelection();
        hideConfirmDelete();
        showButtonsAndTable();
        backButton.setVisible(true);
        backButton.setDisable(false);
    }


}
