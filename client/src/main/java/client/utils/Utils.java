package client.utils;

import client.Session;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.QuizzQuestionServerParsed;
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
    public static QuizzQuestionServerParsed getCurrentQuestion(boolean sessionType) throws JsonProcessingException {
        String path = "session/question/" + Session.getNickname() + "/" + sessionType;
            String result =  ClientBuilder.newClient(new ClientConfig()) //
                    .target(SERVER).path(path) //
                    .request(APPLICATION_JSON) //
                    .accept(APPLICATION_JSON) //
                    .get(String.class);

            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //that allows to ignore getMostExpensive() method
            return objectMapper.readValue(result, QuizzQuestionServerParsed.class);
    }

    /**
     * Invokes to session/answer/{nickname}/{answer}/{currentQuestion}
     * @param answer - Answer <0;2> integer variable which tells what answer has user picked A-C
     * @return Boolean value whether operation was successful
     */
    public static boolean submitAnswer(int answer) {
        String path = "session/answer/" + Session.getNickname() + "/" + answer + "/" + Session.getQuestionNum();
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path(path) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(Boolean.class);
    }

    /**
     * Invokes to /verification path and check whether provided serverAddress is valid address of QuizzGame
     * @param serverAddr - provided serverAddr
     * @return Boolean value whether serverAddr is correct or not
     */
    public static boolean validateServer(String serverAddr) {
        String serverPath = "http://"+serverAddr;
        String path       = "/verification";
        int retNum = ClientBuilder.newClient(new ClientConfig()) //
                .target(serverPath).path(path) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(Integer.class);

        return retNum >= 100 && retNum <= 110;
    }
}
