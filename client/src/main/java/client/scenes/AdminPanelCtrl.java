package client.scenes;

import client.utils.ServerUtils;
import commons.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javax.inject.Inject;

public class AdminPanelCtrl {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

    @Inject
    public AdminPanelCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }

    public void goBackToSplash() {
        mainCtrl.showSplash();
    }

}
