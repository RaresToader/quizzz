/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.Session;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;
    private EnterMenuCtrl enterMenuCtrl;
    private Scene enterMenu;
    private SplashCtrl splashCtrl;
    private Scene splashScreen;
    private GlobalLeaderboardCtrl globalLeaderboardCtrl;
    private Scene globalLeadScreen;
    private AdminPanelCtrl adminPanelCtrl;
    private Scene adminPanelScreen;
    private QueueCtrl queueCtrl;
    private Scene queueScreen;
    private SingleplayerLobbyCtrl sLobbyCtrl;
    private Scene sLobbyScreen;
    private QuestionScreenCtrl questionScreenCtrl;
    private Scene questionScreen;
    private static final String iconPath = "/photos/clientIcon.png";

    public RotateTransition rotationAnimation1;
    public RotateTransition rotationAnimation2;
    public RotateTransition rotationAnimation3;
    public RotateTransition rotationAnimation4;

    public void initialize(Stage primaryStage, Pair<EnterMenuCtrl, Parent> enterMenu,
                           Pair<SplashCtrl, Parent> splash, Pair<GlobalLeaderboardCtrl, Parent> globalLeaderboard, Pair<QuestionScreenCtrl, Parent> questionScreen, Pair<QueueCtrl, Parent> queue, Pair<SingleplayerLobbyCtrl, Parent> singleLobbyScreen, Pair<AdminPanelCtrl, Parent> admin) {

        this.primaryStage = primaryStage;
        this.enterMenuCtrl = enterMenu.getKey();
        this.enterMenu = new Scene(enterMenu.getValue());

        this.splashCtrl = splash.getKey();
        this.splashScreen = new Scene(splash.getValue());

        this.queueCtrl = queue.getKey();
        this.queueScreen = new Scene(queue.getValue());

        this.globalLeaderboardCtrl = globalLeaderboard.getKey();
        this.globalLeadScreen = new Scene(globalLeaderboard.getValue());

        this.adminPanelCtrl = admin.getKey();
        this.adminPanelScreen = new Scene(admin.getValue());

        this.sLobbyCtrl = singleLobbyScreen.getKey();
        this.sLobbyScreen = new Scene(singleLobbyScreen.getValue());

        this.questionScreenCtrl = questionScreen.getKey();
        this.questionScreen = new Scene(questionScreen.getValue());
        //Set program icon
        this.primaryStage.getIcons().add(new Image(MainCtrl.class.getResourceAsStream(iconPath)));
        showEnterMenu();
        primaryStage.show();

        // Create the animations for each loading icon element in the queue screen
        // This is placed here so that it is reset everytime the queue screen is opened
        this.rotationAnimation1 = createRotationAnimation(queueCtrl.loadingCircle1);
        this.rotationAnimation2 = createRotationAnimation(queueCtrl.loadingCircle2);
        this.rotationAnimation3 = createRotationAnimation(queueCtrl.loadingCircle3);
        this.rotationAnimation4 = createRotationAnimation(queueCtrl.loadingCircle4);

    }

    public void showEnterMenu() {
        primaryStage.setTitle("Enter Menu");
        primaryStage.setScene(enterMenu);
        enterMenu.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent k) {
                if (k.getCode().equals(KeyCode.ENTER)) {
                    enterMenuCtrl.enterButton();
                }
            }
        });
    }

    public void showSplash() {
        primaryStage.setTitle("Main Screen");
        primaryStage.setScene(splashScreen);
        splashCtrl.init();
    }

    public void closeSplash() {
        primaryStage.close();
    }

    public void showGlobalLeaderboard(boolean isFromSplash) {
        globalLeaderboardCtrl.init();
        globalLeaderboardCtrl.buttonOnOrOff(isFromSplash);
        primaryStage.setTitle("Global Leaderboard");
        primaryStage.setScene(this.globalLeadScreen);
    }

    public void showQueue() {
        primaryStage.setTitle("Multiplayer queue");
        primaryStage.setScene((this.queueScreen));
        queueCtrl.runLoadingAnimation();
    }

    /**
     * Generates the loading animation for the node provided
     * Should only be used for the 4 loading icon elements in the queue screen
     *
     * @param node provided will create a loading animation for the queue screen
     * @return rotationAnimation which is then applied to the node and is played once the queue screen is openened
     */
    public RotateTransition createRotationAnimation(Node node) {
        //Create a pivot offset to allow the circles to rotate around the pivot position instead of themselves
        double x = queueCtrl.pivot.getLayoutX() - node.getLayoutX();
        double y = queueCtrl.pivot.getLayoutY() - node.getLayoutY();
        node.getTransforms().add(new Translate(-x, -y));
        node.setTranslateX(x);
        node.setTranslateY(y);

        //Create the animation for the given Node
        RotateTransition rotationAnimation = new RotateTransition(Duration.seconds(3), node);
        rotationAnimation.setToAngle(720);
        rotationAnimation.setCycleCount(Timeline.INDEFINITE);

        return rotationAnimation;
    }

    public void showSingleplayerLobby() {
        sLobbyCtrl.playerNameLabel.setText(Session.getNickname());
        sLobbyCtrl.startGameButton.requestFocus();
        primaryStage.setTitle("Singleplayer Lobby");
        primaryStage.setScene(this.sLobbyScreen);
    }

    public void showSingleplayer() {
        primaryStage.setTitle("Singleplayer");
        primaryStage.setScene(this.questionScreen);
        questionScreenCtrl.init();
    }

    public void showAdminPanel() {
        primaryStage.setTitle("Admin Panel");
        primaryStage.setScene(this.adminPanelScreen);
    }
}