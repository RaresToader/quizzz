package client.utils;

import client.Session;
import commons.*;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class Utils {
    private static String SERVER = "http://localhost:8080/";

    /**
     * Setter for SERVER variable
     * @param serverAddr - String variable being serverAddr
     */
    public static void setServerAddr(String serverAddr) {
        Utils.SERVER = serverAddr;
    }

    /**
     * Getter for serverAddr from Utils class
     * @return String valuer being serverAddr of Utils class
     */
    public static String getServerAddr() {
        return SERVER;
    }

    /**
     * Determines whether given String is composed only from alphaNumeric chars
     *
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

    public static List<Map.Entry<String, Integer>> getCurrentLeaderBoard(String nickname) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("session/currentleaderboard/" + nickname) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Map.Entry<String, Integer>>>() {
                });
    }

    /**
     * invokes to session/question/{nickname}
     *
     * @return current question
     * "startTime":10,"questionNum":1}
     */
    public static QuizzQuestionServerParsed getCurrentQuestion(boolean sessionType) throws ParseException {
        String path = "session/question/" + Session.getNickname() + "/" + sessionType;
        String result = ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path(path) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(String.class);
        /**
         * Manually parse the result
         */
        JSONParser jsonParser = new JSONParser();
        JSONObject wholeServerQuizzQuestion = (JSONObject) jsonParser.parse(result);
        JSONObject question = (JSONObject) wholeServerQuizzQuestion.get("question");

        //Json parse
        JSONArray jokerList = (JSONArray) wholeServerQuizzQuestion.get("jokerList");
        List<Joker> finalJokers = new ArrayList<Joker>();
        if(jokerList != null) {
            for (Object o : jokerList) {
                JSONObject x = (JSONObject) o;
                long jokerType   = (long)x.get("jokerType");
                long questionNum = (long)x.get("questionNum");

                finalJokers.add(new Joker((String) x.get("usedBy"), Math.toIntExact(jokerType), Math.toIntExact(questionNum)));
            }
        }

        Question finalQuestion;
        String type = (String) question.get("type");
        if (type.equals("QuizzQuestion")) {
            finalQuestion = parseQuizzQuestion(question);
            return new QuizzQuestionServerParsed(finalQuestion, (long) wholeServerQuizzQuestion.get("startTime"), (Long) wholeServerQuizzQuestion.get("questionNum"), finalJokers);
        } else if (type.equals("ConsumpQuestion")) {
            finalQuestion = parseConsumpQuestion(question);
            return new QuizzQuestionServerParsed(finalQuestion, (long) wholeServerQuizzQuestion.get("startTime"), (Long) wholeServerQuizzQuestion.get("questionNum"), finalJokers);
        } else if (type.equals("GuessQuestion")) {
            finalQuestion = parseGuessQuestion(question);
            return new QuizzQuestionServerParsed(finalQuestion, (long) wholeServerQuizzQuestion.get("startTime"), (Long) wholeServerQuizzQuestion.get("questionNum"), finalJokers);
        } else if (type.equals("InsteadOfQuestion")) {
            finalQuestion = parseInsteadOfQuestion(question);
            return new QuizzQuestionServerParsed(finalQuestion, (long) wholeServerQuizzQuestion.get("startTime"), (Long) wholeServerQuizzQuestion.get("questionNum"), finalJokers);
        } else {
            throw new IllegalArgumentException("Wrong question");
        }
    }

    /**
     * Parses a QuizzzQuestion from a JSONObject
     *
     * @param question a jsonObject, representing the question
     * @return a quizzQuestion
     */
    public static QuizzQuestion parseQuizzQuestion(JSONObject question) {
        String questionPrompt = (String) question.get("question");
        Activity firstChoice = parseActivity((JSONObject) question.get("firstChoice"));
        Activity secondChoice = parseActivity((JSONObject) question.get("secondChoice"));
        Activity thirdChoice = parseActivity((JSONObject) question.get("thirdChoice"));
        return new QuizzQuestion(questionPrompt, firstChoice, secondChoice, thirdChoice);
    }

    /**
     * Parses a InsteadOfQuestion from a JSONObject
     *
     * @param question a jsonObject, representing the question
     * @return a insteadOfQuestion
     */
    public static InsteadOfQuestion parseInsteadOfQuestion(JSONObject question) {
        Activity prompt = parseActivity((JSONObject) question.get("promptActivity"));
        Activity firstChoice = parseActivity((JSONObject) question.get("firstChoice"));
        Activity secondChoice = parseActivity((JSONObject) question.get("secondChoice"));
        Activity thirdChoice = parseActivity((JSONObject) question.get("thirdChoice"));
        return new InsteadOfQuestion(prompt, firstChoice, secondChoice, thirdChoice);
    }

    /**
     * Parses an activity from a JSONObject
     *
     * @param helper a jsonObject
     * @return an activity
     */
    public static Activity parseActivity(JSONObject helper) {
        String id = (String) helper.get("id");
        String image_path = (String) helper.get("image_path");
        String title = (String) helper.get("title");
        Long cost = (Long) helper.get("consumption_in_wh");
        String source = (String) helper.get("source");
        Activity activity = new Activity(id, image_path, title, cost, source);
        return activity;
    }

    /**
     * Parses a ConsumpQuestion from a JSONObject
     *
     * @param question the question to be parsed
     * @return a ConsumpQuestion
     */
    public static ConsumpQuestion parseConsumpQuestion(JSONObject question) {
        String questionPrompt = (String) question.get("question");
        Activity activity = parseActivity((JSONObject) question.get("activity"));
        Long first = (Long) question.get("first");
        Long second = (Long) question.get("second");
        Long third = (Long) question.get("third");
        return new ConsumpQuestion(questionPrompt, activity, first, second, third);
    }

    /**
     * Parses a GuessQuestion from a JSONObject
     *
     * @param question the question to be parsed
     * @return a GuessQuestion
     */
    public static GuessQuestion parseGuessQuestion(JSONObject question) {
        String questionPrompt = (String) question.get("question");
        Activity activity = parseActivity((JSONObject) question.get("activity"));
        return new GuessQuestion(questionPrompt, activity);
    }

    /**
     * Invokes to session/answer/{nickname}/{answer}/{currentQuestion}
     *
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
     * Invokes to /session/addjoker/{nickname}/{jokertype}/{question} and submits joker for given session
     *
     * @param x - Joker to be sent
     * @return Boolean value whether operation of addition was successful
     */
    public static boolean addJoker(Joker x) {
        if (x == null) return false;
        String path = "/session/addjoker/" + Session.getNickname() + "/" + x.getJokerType() + "/" + x.getQuestionNum();
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path(path) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(Boolean.class);
    }

    /**
     * Invokes to /session/leavesession/{nickname} and informs server that player wants to leave given session
     *
     * @return Boolean value whether operation of removal was successful
     */
    public static boolean leaveSession() {
        String path = "session/leavesession/" + Session.getNickname();
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path(path) //
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Boolean.class);
    }

    /**
     * Invokes to /verification path and check whether provided serverAddress is valid address of QuizzGame
     *
     * @param serverAddr - provided serverAddr
     * @return Boolean value whether serverAddr is correct or not
     */
    public static boolean validateServer(String serverAddr) {
        String serverPath = "http://" + serverAddr;
        String path = "/verification";
        int retNum = ClientBuilder.newClient(new ClientConfig()) //
                .target(serverPath).path(path) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(Integer.class);

        return retNum >= 100 && retNum <= 110;
    }

    /**
     * Invokes to /session/playersinsession/{nickname} and get all the players in the session
     *
     * @return List<Players> that contains all the players in the session
     */
    public static List<String> getCurrentSessionPlayers() {
        String path = "session/playersinsession/" + Session.getNickname();
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(path).request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<String>>() {
                });
    }

    /**
     * Invokes to session/setEmoji/{nickname}/{emojiType} and adds the emoji of the user to the session
     *
     * @param nickname  - provided nickname of the requesting client
     * @param emojiType - the type of emoji that is clicked by the user
     * @return Boolean value whether operation was successful
     */
    public static boolean setEmoji(String nickname, String emojiType) {
        String path = "session/setEmoji/" + nickname + "/" + emojiType;
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path(path) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(Boolean.class);
    }

    /**
     * Invokes to session/getlobbystatus/{nickname} and gets a SessionLobbyStatus object
     *
     * @return SessionLobbyStatus object containing active emojis, gameStarted status and name of gameAdmin
     */
    public static SessionLobbyStatus getLobbyStatus() {
        String path = "session/getlobbystatus/" + Session.getNickname();
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(path)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<SessionLobbyStatus>() {
                });
    }

    /**
     * Invokes to session/joinsession/{nickname} and adds player to available (or creates new) multiplayer session
     * !!ONLY FOR MULTIPLAYER SESSION PURPOSES!!
     * @return Boolean value whether operation was successful
     */
    public static boolean joinSession() {
        String path = "session/joinsession/" + Session.getNickname();
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path(path) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(Boolean.class);
    }

    /**
     * Invokes to session/startsession/{nickname} and starts a game
     *
     * @return Boolean value whether operation was successful
     */
    public static boolean startSession() {
        String path = "session/startsession/" + Session.getNickname();
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(path)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Boolean.class);
    }
}
