package client.utils;

import commons.Activity;
import commons.Player;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServerUtilsTest {

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

    @Test
    public void emptyConstructorTest() {
        ServerUtils x = new ServerUtils();
        assertNotNull(x);
    }

    @Test
    public void addPlayerTest() {
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("{\"username\":\"kuba\",\"score\":6975}")
                .addHeader("content-type: application/json; charset=utf-8"));

        ServerUtils server = new ServerUtils();
        assertEquals(new Player("kuba",Long.valueOf(6975)),server.addPlayer("kuba"));
    }

    @Test
    public void getPlayerTest() {
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("{\"username\":\"kuba\",\"score\":6975}")
                .addHeader("content-type: application/json; charset=utf-8"));

        ServerUtils server = new ServerUtils();
        assertEquals(new Player("kuba",Long.valueOf(6975)),server.getPlayer("kuba"));
    }

    @Test
    public void checkIfServerMatchesTest() {
        ServerUtils server = new ServerUtils();
        assertTrue(server.checkIfServerMatches("http://localhost:8080/"));
    }

    @Test
    public void checkIfServerMatchesFalseTest() {
        ServerUtils server = new ServerUtils();
        assertFalse(server.checkIfServerMatches("http://localhost:8081/"));
    }


    @Test
    public void getAllPlayersTest() {
        ServerUtils server = new ServerUtils();
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("[{\"username\":\"abcd\",\"score\":0},{\"username\":\"dab\",\"score\":0},{\"username\":\"kubapat\",\"score\":8425}]")
                .addHeader("content-type: application/json; charset=utf-8"));

        List<Player> expected = new ArrayList<Player>();
        expected.add(new Player("abcd",0));
        expected.add(new Player("dab",0));
        expected.add(new Player("kubapat",8425));

        assertEquals(expected,server.getAllPlayers());
    }


    @Test
    public void addActivityTest() {
        ServerUtils server = new ServerUtils();
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("{\"id\":\"02-electric_stove\",\"title\":\"Using an electric stove for 1 hour\",\"consumption_in_wh\":1200,\"source\":\"https://blog.arcadia.com/electricity-costs-10-key-household-products/\",\"image_path\":\"02/electric_stove.png\"}")
                .addHeader("content-type: application/json; charset=utf-8"));

        Activity x = new Activity("02-electric_stove","02/electric_stove.png","Using an electric stove for 1 hour",Long.valueOf(1200),"https://blog.arcadia.com/electricity-costs-10-key-household-products/");
        assertEquals(x,server.addActivity(x));
    }

    @Test
    public void deleteActivityTest() {
        ServerUtils server = new ServerUtils();
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("{\"id\":\"02-electric_stove\",\"title\":\"Using an electric stove for 1 hour\",\"consumption_in_wh\":1200,\"source\":\"https://blog.arcadia.com/electricity-costs-10-key-household-products/\",\"image_path\":\"02/electric_stove.png\"}")
                .addHeader("content-type: application/json; charset=utf-8"));

        Activity x = new Activity("02-electric_stove","02/electric_stove.png","Using an electric stove for 1 hour",Long.valueOf(1200),"https://blog.arcadia.com/electricity-costs-10-key-household-products/");
        assertEquals(x,server.deleteActivity("02-electric_stove"));
    }

    @Test
    public void modifyActivityTest() {
        ServerUtils server = new ServerUtils();
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("{\"id\":\"02-electric_stove\",\"title\":\"Using an electric stove for 1 hour\",\"consumption_in_wh\":1200,\"source\":\"https://blog.arcadia.com/electricity-costs-10-key-household-products/\",\"image_path\":\"02/electric_stove.png\"}")
                .addHeader("content-type: application/json; charset=utf-8"));

        Activity x = new Activity("02-electric_stove","02/electric_stove.png","Using an electric stove for 1 hour",Long.valueOf(1200),"https://blog.arcadia.com/electricity-costs-10-key-household-products/");
        assertEquals(x,server.modifyActivity(x));
    }

    @Test
    public void getActivityByIdTest() {
        ServerUtils server = new ServerUtils();
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("{\"id\":\"02-electric_stove\",\"title\":\"Using an electric stove for 1 hour\",\"consumption_in_wh\":1200,\"source\":\"https://blog.arcadia.com/electricity-costs-10-key-household-products/\",\"image_path\":\"02/electric_stove.png\"}")
                .addHeader("content-type: application/json; charset=utf-8"));

        Activity x = new Activity("02-electric_stove","02/electric_stove.png","Using an electric stove for 1 hour",Long.valueOf(1200),"https://blog.arcadia.com/electricity-costs-10-key-household-products/");
        assertEquals(x,server.getActivityById("02-electric_stove"));
    }

    @Test
    public void doesActivityExistTest() {
        ServerUtils server = new ServerUtils();
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("true")
                .addHeader("content-type: text/plain; charset=utf-8"));

        assertTrue(server.doesActivityExist("02-electric_stove"));
    }


    @Test
    public void get60RandomTest() {
        ServerUtils server = new ServerUtils();
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("[{\"id\":\"02-electric_stove\",\"title\":\"Using an electric stove for 1 hour\",\"consumption_in_wh\":1200,\"source\":\"https://blog.arcadia.com/electricity-costs-10-key-household-products/\",\"image_path\":\"02/electric_stove.png\"},{\"id\":\"02-electric_stove\",\"title\":\"Using an electric stove for 1 hour\",\"consumption_in_wh\":1200,\"source\":\"https://blog.arcadia.com/electricity-costs-10-key-household-products/\",\"image_path\":\"02/electric_stove.png\"}]")
                .addHeader("content-type: application/json; charset=utf-8"));

        List<Activity> activityList = new ArrayList<Activity>();
        Activity x = new Activity("02-electric_stove","02/electric_stove.png","Using an electric stove for 1 hour",Long.valueOf(1200),"https://blog.arcadia.com/electricity-costs-10-key-household-products/");
        activityList.add(x);
        activityList.add(x);
        assertEquals(activityList,server.get60RandomActivities());
    }

    @Test
    public void isUsernameValidTest() {
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("true")
                .addHeader("content-type: text/plain; charset=utf-8"));

        assertTrue(ServerUtils.isUsernameValid("kubapat"));
    }

    @Test
    public void getAllActivitiesTest() {
        ServerUtils server = new ServerUtils();
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("[{\"id\":\"02-electric_stove\",\"title\":\"Using an electric stove for 1 hour\",\"consumption_in_wh\":1200,\"source\":\"https://blog.arcadia.com/electricity-costs-10-key-household-products/\",\"image_path\":\"02/electric_stove.png\"},{\"id\":\"02-electric_stove\",\"title\":\"Using an electric stove for 1 hour\",\"consumption_in_wh\":1200,\"source\":\"https://blog.arcadia.com/electricity-costs-10-key-household-products/\",\"image_path\":\"02/electric_stove.png\"}]")
                .addHeader("content-type: application/json; charset=utf-8"));

        List<Activity> activityList = new ArrayList<Activity>();
        Activity x = new Activity("02-electric_stove","02/electric_stove.png","Using an electric stove for 1 hour",Long.valueOf(1200),"https://blog.arcadia.com/electricity-costs-10-key-household-products/");
        activityList.add(x);
        activityList.add(x);
        assertEquals(activityList,server.getAllActivities());
    }


    @Test
    public void loadActivitiesTest() {
        ServerUtils server = new ServerUtils();
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("[{\"id\":\"02-electric_stove\",\"title\":\"Using an electric stove for 1 hour\",\"consumption_in_wh\":1200,\"source\":\"https://blog.arcadia.com/electricity-costs-10-key-household-products/\",\"image_path\":\"02/electric_stove.png\"},{\"id\":\"02-electric_stove\",\"title\":\"Using an electric stove for 1 hour\",\"consumption_in_wh\":1200,\"source\":\"https://blog.arcadia.com/electricity-costs-10-key-household-products/\",\"image_path\":\"02/electric_stove.png\"}]")
                .addHeader("content-type: application/json; charset=utf-8"));

        List<Activity> activityList = new ArrayList<Activity>();
        Activity x = new Activity("02-electric_stove","02/electric_stove.png","Using an electric stove for 1 hour",Long.valueOf(1200),"https://blog.arcadia.com/electricity-costs-10-key-household-products/");
        activityList.add(x);
        activityList.add(x);
        assertEquals(activityList,server.loadActivitiesInRepo());
    }

    @Test
    public void getCorrectTest() {
        ServerUtils server = new ServerUtils();
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("test")
                .addHeader("content-type: text/plain; charset=utf-8"));

        assertEquals("test",server.getCorrect());
    }

    @Test
    public void getLeaderboardPlayersTest() {
        ServerUtils server = new ServerUtils();
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("[{\"username\":\"abcd\",\"score\":0},{\"username\":\"dab\",\"score\":0},{\"username\":\"kubapat\",\"score\":8425}]")
                .addHeader("content-type: application/json; charset=utf-8"));

        List<Player> expected = new ArrayList<Player>();
        expected.add(new Player("abcd",0));
        expected.add(new Player("dab",0));
        expected.add(new Player("kubapat",8425));

        assertEquals(expected,server.getLeaderboardPlayers());
    }

    @Test
    public void updatePlayerInRepoTest() {
        webClientMock.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200")
                .setBody("{\"username\":\"kuba\",\"score\":6975}")
                .addHeader("content-type: application/json; charset=utf-8"));

        ServerUtils server = new ServerUtils();
        assertEquals(new Player("kuba",Long.valueOf(6975)),server.updatePlayerInRepo("kuba",Long.valueOf(55)));
    }

    @Test
    public void setServerAddrTest() {
        ServerUtils.setServerAddr("abc");
        assertEquals("abc",ServerUtils.getServerAddr());
        ServerUtils.setServerAddr("http://localhost:8080/");
    }

}
