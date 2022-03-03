package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.inject.Inject;

public class SplashCtrl {
    private MainCtrl mainCtrl;

    @Inject
    public SplashCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    @FXML
    private Button quit;
    @FXML
    private Button single;
    @FXML
    private Button multi;
    @FXML
    private Button leadeboard;

    public void quitButton(){
        mainCtrl.closeSplash();
    }
}
