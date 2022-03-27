package client.utils;

import client.Session;
import commons.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {

    public static MockWebServer webClientMock;

    @BeforeAll
    public static void setUp() throws IOException {
        webClientMock = new MockWebServer();
        webClientMock.start(8080);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        webClientMock.shutdown();
    }

    /* TESTS */

    @Test
    public void emptyConstructorTest() {
        Utils x = new Utils();
        assertNotNull(x);
    }

    @Test
    public void isAlphaNumericNullTest() {
        assertFalse(Utils.isAlphaNumeric(null));
    }

    @Test
    public void isAlphaNumericFalseTest() {
        assertFalse(Utils.isAlphaNumeric("$$$$###33"));
    }

    @Test
    public void isAlphaNumericTrueTest() {
        assertTrue(Utils.isAlphaNumeric("Ab3d9"));
    }

    @Test
    public void getActivePlayersTest() {
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("5")
                .addHeader("content-type: text/plain; charset=utf-8"));
        assertEquals("5",Utils.getActivePlayers());
    }

    @Test
    public void validServerTest() {
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("105")
                .addHeader("content-type: text/plain; charset=utf-8"));
        assertTrue(Utils.validateServer("localhost:8080"));
    }

    @Test
    public void parseActivityTest() {
        Activity x = new Activity("5","https://google.com","activity",Long.valueOf(0),"google");
        JSONObject js = new JSONObject();
        js.put("id",x.getId());
        js.put("image_path",x.getImage_path());
        js.put("title",x.getTitle());
        js.put("consumption_in_wh",x.getConsumption_in_wh());
        js.put("source",x.getSource());
        assertEquals(x,Utils.parseActivity(js));
    }

    @Test
    public void parseQuizzQuestionTest() {
        Activity x = new Activity("5","https://google.com","activity",Long.valueOf(0),"google");
        QuizzQuestion q = new QuizzQuestion("test",x,x,x);
        JSONObject activity = new JSONObject();
        activity.put("id",x.getId());
        activity.put("image_path",x.getImage_path());
        activity.put("title",x.getTitle());
        activity.put("consumption_in_wh",x.getConsumption_in_wh());
        activity.put("source",x.getSource());
        JSONObject js = new JSONObject();
        js.put("question",q.getQuestion());
        js.put("firstChoice",activity);
        js.put("secondChoice",activity);
        js.put("thirdChoice",activity);
        assertEquals(q,Utils.parseQuizzQuestion(js));
    }

    @Test
    public void getCurrentQuestionForQuizzQuestion() throws ParseException {
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("{\"question\":{\"type\":\"QuizzQuestion\",\"question\":\"What activity costs more?\",\"firstChoice\":{\"id\":\"75-phone\",\"title\":\"Using your smartphone for a week\",\"consumption_in_wh\":915,\"source\":\"https://www.forbes.com/sites/christopherhelman/2013/09/07/how-much-energy-does-your-iphone-and-other-devices-use-and-what-to-do-about-it/?sh=45530aaa2f70\",\"image_path\":\"75/phone.jpg\"},\"secondChoice\":{\"id\":\"76-biking\",\"title\":\"Biking for 1 hour\",\"consumption_in_wh\":698,\"source\":\"https://runrepeat.com/calories-burned-cycling-biking\",\"image_path\":\"76/biking.png\"},\"thirdChoice\":{\"id\":\"52-wind_turbine\",\"title\":\"Energy produced by a wind turbine at optimal efficiency\",\"consumption_in_wh\":1500000,\"source\":\"https://www.energuide.be/en/questions-answers/how-much-power-does-an-electric-car-use/212/\",\"image_path\":\"52/wind_turbine.png\"},\"mostExpensive\":\"Energy produced by a wind turbine at optimal efficiency\"},\"startTime\":6,\"questionNum\":0,\"jokerList\":[]}")
                .addHeader("content-type: application/json; charset=utf-8"));

        Session.setNickname("test");
        assertEquals(new QuizzQuestionServerParsed(
                new QuizzQuestion(
                      "What activity costs more?",
                      new Activity("75-phone","75/phone.jpg","Using your smartphone for a week",Long.valueOf(915),"https://www.forbes.com/sites/christopherhelman/2013/09/07/how-much-energy-does-your-iphone-and-other-devices-use-and-what-to-do-about-it/?sh=45530aaa2f70"),
                      new Activity("76-biking","76/biking.png","Biking for 1 hour",Long.valueOf(698),"https://runrepeat.com/calories-burned-cycling-biking"),
                      new Activity("52-wind_turbine","52/wind_turbine.png","Energy produced by a wind turbine at optimal efficiency",Long.valueOf(1500000),"https://www.energuide.be/en/questions-answers/how-much-power-does-an-electric-car-use/212/")
                ),6,0,new ArrayList<Joker>()
        ), Utils.getCurrentQuestion(false));
    }


    @Test
    public void getCurrentQuestionForConsump() throws ParseException {
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("{\"question\":{\"type\":\"ConsumpQuestion\",\"question\":null,\"activity\":{\"id\":\"43-printer\",\"title\":\"Using a Printer for an hour\",\"consumption_in_wh\":40,\"source\":\"https://energyusecalculator.com/electricity_printer.htm\",\"image_path\":\"43/printer.jpeg\"},\"first\":40,\"second\":3,\"third\":3,\"consump\":\"40\"},\"startTime\":5,\"questionNum\":0,\"jokerList\":[]}")
                .addHeader("content-type: application/json; charset=utf-8"));

        Session.setNickname("test");
        assertEquals(new QuizzQuestionServerParsed(
                            new ConsumpQuestion(
                                    null,
                                    new Activity("43-printer","43/printer.jpeg","Using a Printer for an hour",Long.valueOf(40),"https://energyusecalculator.com/electricity_printer.htm"),
                                    Long.valueOf(40),Long.valueOf(3),Long.valueOf(3))

                            ,5,0,new ArrayList<Joker>()),Utils.getCurrentQuestion(false));
    }

    @Test
    public void getCurrentQuestionForIllegal() throws ParseException {
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("{\"question\":{\"type\":\"wrongTypeOfQuestion\",\"question\":null,\"activity\":{\"id\":\"43-printer\",\"title\":\"Using a Printer for an hour\",\"consumption_in_wh\":40,\"source\":\"https://energyusecalculator.com/electricity_printer.htm\",\"image_path\":\"43/printer.jpeg\"},\"first\":40,\"second\":3,\"third\":3,\"consump\":\"40\"},\"startTime\":5,\"questionNum\":0,\"jokerList\":[]}")
                .addHeader("content-type: application/json; charset=utf-8"));

        Session.setNickname("test");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> { Utils.getCurrentQuestion(false);});
        assertEquals("Wrong question",exception.getMessage());
    }

    @Test
    public void validNotServerTest() {
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("115")
                .addHeader("content-type: text/plain; charset=utf-8"));
        assertFalse(Utils.validateServer("localhost:8080"));
    }

    @Test
    public void leaveSessionTest() {
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("true")
                .addHeader("content-type: text/plain; charset=utf-8"));

        Session.setNickname("test");
        assertTrue(Utils.leaveSession());
    }

    @Test
    public void submitAnswerTest() {
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("true")
                .addHeader("content-type: text/plain; charset=utf-8"));

        Session.setNickname("test");
        Session.setQuestionNum(5);
        assertTrue(Utils.submitAnswer(2));
    }

    @Test
    public void addNullJokerTest() {
        assertFalse(Utils.addJoker(null));
    }

    @Test
    public void addJokerTest() {
        Session.setNickname("test");
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("true")
                .addHeader("content-type: text/plain; charset=utf-8"));

        assertTrue(Utils.addJoker(new Joker("test",1,2)));
    }

    @Test
    public void getCurrentSessionPlayersTest() {
        List<String> x = new ArrayList<>();
        x.add("test");

        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("[\"test\"]")
                .addHeader("content-type: application/json; charset=utf-8"));

        Session.setNickname("test");
        assertEquals(x,Utils.getCurrentSessionPlayers());
    }
}
