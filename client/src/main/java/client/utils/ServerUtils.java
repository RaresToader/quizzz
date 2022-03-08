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
     * @param username String, representing the username of the player to add
     * @return the created player, which is also added to the database
     */
    public Player addPlayer(String username){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("player/").request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(new Player(username), APPLICATION_JSON),Player.class);
    }

    /**
     * Retrieves a player by username(which acts in our case as an identifier)
     * @param username String representing the username of the player to be retrieved
     * @return the retrived player
     */
    public Player getPlayer(String username){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("player/"+username).request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<Player>(){});
    }

    /**
     * Retrieves the list of all players
     * @return a list of players, from the player repository
     */
    public List<Player> getAllPlayers(){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("player/").request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Player>>(){});
    }

    /**
     * Retrives a list of all activities
     * @return the list of all activities, from the activity repository
     */
    public List<Activity> getAllActivities(){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("activity/all").request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Activity>>(){});
    }

    /**
     * Retrieve an activity by id
     * @param id the id of the activity
     * @return the activity from the repository
     */
    public Activity getActivityById(long id){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("activity/"+id).request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<Activity>(){});
    }
    /**
     * Code from the example repository
    public void getQuotesTheHardWay() throws IOException {
        var url = new URL("http://localhost:8080/api/quotes");
        var is = url.openConnection().getInputStream();
        var br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public List<Quote> getQuotes() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {});
    }

    public Quote addQuote(Quote quote) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
    }

     */
}