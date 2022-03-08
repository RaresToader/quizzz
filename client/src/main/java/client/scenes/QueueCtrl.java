package client.scenes;

import javax.inject.Inject;

public class QueueCtrl {
    private final MainCtrl mainCtrl;
    @Inject
    public QueueCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }
    public void goBackToSplash(){
        mainCtrl.showSplash();
    }
}
