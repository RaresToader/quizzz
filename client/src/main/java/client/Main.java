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
package client;

import static com.google.inject.Guice.createInjector;

import client.scenes.*;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {

        var enterMenu = FXML.load(EnterMenuCtrl.class, "client", "scenes", "EnterMenu.fxml");
        var multiPlayerFinalLeaderboard = FXML.load(MultiPlayerFinalLeaderboardCtrl.class, "client","scenes","FinalLeaderboard.fxml");
        var multiPlayerMiddleLeaderboard =  FXML.load(MultiplayerMiddleLeaderboardCtrl.class, "client","scenes","FinalLeaderboard.fxml");
        var splash = FXML.load(SplashCtrl.class, "client", "scenes", "SplashScreen.fxml");
        var queue = FXML.load(QueueCtrl.class, "client", "scenes", "QueueScreen.fxml");
        var multiLobby = FXML.load(MultiplayerLobbyCtrl.class, "client", "scenes", "MultiplayerLobbyScreen.fxml");
        var globalLeaderboard = FXML.load(GlobalLeaderboardCtrl.class, "client", "scenes", "GlobalLeaderboard.fxml");
        var adminPanel = FXML.load(AdminPanelCtrl.class, "client", "scenes", "AdminPanelScreen.fxml");
        var singleLobby = FXML.load(SingleplayerLobbyCtrl.class, "client", "scenes", "SingleplayerLobbyScreen.fxml");
        var questionScreen = FXML.load(QuestionScreenCtrl.class, "client", "scenes", "QuestionScreen.fxml");
        var questionScreenMultiplayer = FXML.load(QuestionScreenMultiplayerCtrl.class, "client", "scenes", "QuestionScreenMultiplayer.fxml");
        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, enterMenu, splash, globalLeaderboard, questionScreen, questionScreenMultiplayer, queue, singleLobby, adminPanel, multiLobby,multiPlayerFinalLeaderboard,multiPlayerMiddleLeaderboard);
    }
}