package client.utils;

import client.Session;
import jakarta.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.client.ClientConfig;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class Utils {
    //Temporary variable of server address will be changed for Session.serverAddr after tests
    private static String SERVER = "http://localhost:8080/";

    /**
     * Determines whether given String is composed only from alphaNumeric chars
     * @param s - String to be checked
     * @return Boolean value whether s is only alphaNumeric composed
     */
    public static boolean isAlphaNumeric(String s) {
        return s != null && s.matches("^[a-zA-Z0-9]*$");
    }

    /**
     * @return number of active players across the server
     */
    public static String getActivePlayers() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("session/activeplayers") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(String.class);
    }

    /**
     * invokes to session/question/{nickname}
     * @return current question
     */
    public static String getCurrentQuestion() {
            String path = "session/question/" + Session.getNickname();
            return ClientBuilder.newClient(new ClientConfig()) //
                    .target(SERVER).path(path) //
                    .request(APPLICATION_JSON) //
                    .accept(APPLICATION_JSON) //
                    .get(String.class);
    }
}
