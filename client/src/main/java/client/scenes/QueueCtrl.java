package client.scenes;

import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.shape.*;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import org.glassfish.jersey.internal.PropertiesResolver;

import javax.inject.Inject;

public class QueueCtrl {
    private final MainCtrl mainCtrl;

    @FXML
    private Button goBack;

    @FXML
    public Circle loadingCircle1;
    @FXML
    public Circle loadingCircle2;
    @FXML
    public Circle loadingCircle3;
    @FXML
    public Circle loadingCircle4;
    @FXML
    public Circle pivot;

    @Inject
    public QueueCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }


    public void goBackToSplash() {
        mainCtrl.showSplash();
    }

    public void runLoadingAnimation() {
        RotateTransition rotationAnimation1 = createRotationAnimation(loadingCircle1);
        rotationAnimation1.play();

        RotateTransition rotationAnimation2 = createRotationAnimation(loadingCircle2);
        rotationAnimation2.play();

        RotateTransition rotationAnimation3 = createRotationAnimation(loadingCircle3);
        rotationAnimation3.play();

        RotateTransition rotationAnimation4 = createRotationAnimation(loadingCircle4);
        rotationAnimation4.play();
    }



    private RotateTransition createRotationAnimation(Node node) {
        //Create a pivot offset to allow the circles to rotate around the pivot position instead of themselves
        double x = pivot.getLayoutX()-node.getLayoutX();
        double y = pivot.getLayoutY()-node.getLayoutY();
        node.getTransforms().add(new Translate(-x,-y));
        node.setTranslateX(x); node.setTranslateY(y);

        //Create the animation for the given Node
        RotateTransition rotationAnimation = new RotateTransition(Duration.seconds(3), node);
        rotationAnimation.setToAngle(1080);
        rotationAnimation.setCycleCount(Timeline.INDEFINITE);

        return rotationAnimation;
    }

}

