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
package client.utils;

import commons.Activity;
import commons.Player;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";

    /**
     * Adds a player by a username
     * see server/src/../PlayerController
     *
     * @param username String, representing the username of the player to add
     * @return the created player, which is also added to the database
     */
    public Player addPlayer(String username) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("player/add").request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(new Player(username), APPLICATION_JSON), Player.class);
    }

    /**
     * Retrieves a player by username(which acts in our case as an identifier)
     *
     * @param username String representing the username of the player to be retrieved
     * @return the retrived player
     */
    public Player getPlayer(String username) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("player/" + username).request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<Player>() {
                });
    }

    /**
     * Checks if the server matches with the host server
     *
     * @param otherServer another url
     * @return a boolean, representing whether the server matches.
     */
    public boolean checkIfServerMatches(String otherServer) {

        return SERVER.equals(otherServer);
    }

    /**
     * Retrieves the list of all players
     *
     * @return a list of players, from the player repository
     */
    public List<Player> getAllPlayers() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("player/").request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Player>>() {
                });
    }

    /**
     * Adds an activity to the database
     *
     * @param activity the activity to be added
     * @return the added activity
     */
    public Activity addActivity(Activity activity) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("activity/add").request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(activity, APPLICATION_JSON), Activity.class);
    }

    /**
     * Delete activity by its id
     *
     * @param id the id of an activity
     * @return the deleted activity
     */
    public Activity deleteActivity(String id) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("activity/delete/" + id).request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(Activity.class);
    }

    /**
     * Modifies an activity
     *
     * @param activity to be saved in the repository
     * @return the modified activity
     */
    public Activity modifyActivity(Activity activity) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("activity/modify").request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(activity, APPLICATION_JSON), Activity.class);
    }

    /**
     * Checks if an activity already exists in the repository (with the same id)
     *
     * @param id the activity to be checked
     * @return a boolean, true if an activity with the same id already exists in the repository,
     * false otherwise
     */
    public Boolean doesActivityExist(String id) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("activity/exists/" + id).request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<Boolean>() {
                });
    }

    /**
     * Retrieves a list of 60 Random Activities
     *
     * @return a list of 60 activities
     */
    public List<Activity> get60RandomActivities() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("activity/randomset")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Activity>>() {
                });
    }

    /**
     * checks if the chosen username is valid or not
     * @param username
     * @return
     */
    public static boolean isUsernameValid(String username) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("session/validusername/" + username)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<Boolean>() {
                });
    }

    /**
     * Retrives a list of all activities
     *
     * @return the list of all activities, from the activity repository
     */
    public List<Activity> getAllActivities() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("activity/all").request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Activity>>() {
                });
    }

    /**
     * Retrieve an activity by id
     *
     * @param id the id of the activity
     * @return the activity from the repository
     */
    public Activity getActivityById(String id) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("activity/" + id).request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<Activity>() {
                });
    }

    /**
     * Get the correct answer
     *
     * @return a string
     */
    public String getCorrect() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("/correct/mostExpensive").request(APPLICATION_JSON)
                .accept(APPLICATION_JSON) //
                .get(new GenericType<String>() {
                });
    }

    /**
     * Gets the top 20 players according to score, in descending order
     *
     * @return a list of the top 20 players
     */
    public List<Player> getLeaderboardPlayers() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/player/leaderboard").request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Player>>() {
                });
    }

    /**
     * Updates a player in the database if his score is higher than the score already stored.
     *
     * @param id     the id of the player whose score is going to be updated.
     * @param points the amount of points that are going to be added
     * @return the updated player
     */
    public Player updatePlayerInRepo(String id, long points) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/player/update/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(new Player(id, points), APPLICATION_JSON), Player.class);
    }

    /**
     * Loads all activities from activities.json file
     *
     * @return the list of activities loaded from the activities.json file
     */
    public List<Activity> loadActivitiesInRepo() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/activity/load")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Activity>>() {
                });
    }
}