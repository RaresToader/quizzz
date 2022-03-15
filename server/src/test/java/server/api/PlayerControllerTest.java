package server.api;

import commons.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerControllerTest {
    private TestPlayerRepository repo;
    private PlayerController systemUnderTest;

    @BeforeEach
    public void setup() {
        repo = new TestPlayerRepository();
        systemUnderTest = new PlayerController(repo);
    }

    @Test
    public void cannotAddNullPlayer() {
        var actual = systemUnderTest.addPlayer(null);
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void cannotAddPlayerWithNullUsername() {
        var actual = systemUnderTest.addPlayer(new Player(null));
        assertEquals(actual.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addPlayerTest() {
        systemUnderTest.addPlayer(new Player("test"));
        assertTrue(repo.players.contains(new Player("test")));
    }

    @Test
    public void addPlayerTest2() {
        systemUnderTest.addPlayer(new Player("test"));
        assertTrue(repo.calledMethods.contains("save"));
    }

    @Test
    public void getAllPlayersTest() {
        systemUnderTest.getAllPlayers();
        assertTrue(repo.calledMethods.contains("findAll"));
    }

    @Test
    public void getAllPlayersTest2() {
        systemUnderTest.addPlayer(new Player("1"));
        systemUnderTest.addPlayer(new Player("2"));
        List<Player> listTest = new ArrayList<>();
        listTest.add(new Player("1"));
        listTest.add(new Player("2"));
        assertEquals(listTest, repo.players);
    }

    @Test
    public void getPlayerByIdTest() {
        var actual = systemUnderTest.getPlayerById("test");
        assertEquals(actual.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void getPlayerByIdTest2() {
        systemUnderTest.addPlayer(new Player("test"));
        var actual = systemUnderTest.getPlayerById("test");
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getPlayerByIdTest3() {
        systemUnderTest.addPlayer(new Player("test"));
        var actual = systemUnderTest.getPlayerById("test");
        assertEquals(actual.getBody(), new Player("test"));
    }

    @Test
    public void getPlayerByIdTest4() {
        systemUnderTest.getPlayerById("test");
        assertTrue(repo.calledMethods.contains("findById"));
    }

    @Test
    public void cannotModifyNullPlayer() {
        var actual = systemUnderTest.modifyPlayer(null);
        assertEquals(actual.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void cannotModifyPlayerWithNullId() {
        var actual = systemUnderTest.modifyPlayer(new Player(null));
        assertEquals(actual.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void cannotModifyNonexistentPlayer() {
        var actual = systemUnderTest.modifyPlayer(new Player("test"));
        assertEquals(actual.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void modifyPlayerTest4() {
        systemUnderTest.modifyPlayer(new Player("test"));
        assertTrue(repo.calledMethods.contains("findById"));
    }

    @Test
    public void modifyPlayerTest5() {
        systemUnderTest.addPlayer(new Player("test"));
        var actual = systemUnderTest.modifyPlayer(new Player("test"));
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void modifyPlayerTest6() {
        systemUnderTest.addPlayer(new Player("test"));
        systemUnderTest.modifyPlayer(new Player("test"));
        assertTrue(repo.calledMethods.contains("getById"));
    }

    @Test
    public void modifyPlayerTest7() {
        systemUnderTest.addPlayer(new Player("test"));
        Player player = new Player("test");
        player.incrementScore(1000);
        var actual = systemUnderTest.modifyPlayer(player);
        assertEquals(Objects.requireNonNull(actual.getBody()).getScore(), player.getScore());
    }

    @Test
    public void modifyPlayerTest8() {
        systemUnderTest.addPlayer(new Player("test"));
        systemUnderTest.modifyPlayer(new Player("test"));
        assertTrue(repo.calledMethods.contains("save"));
    }

    @Test
    public void modifyPlayerByPathTest1() {
        var actual = systemUnderTest.modifyPlayerByPath(null, new Player("test"));
        assertEquals(actual.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    public void modifyPlayerByPathTest2() {
        var actual = systemUnderTest.modifyPlayerByPath("test", new Player("test"));
        assertEquals(actual.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    public void modifyPlayerByPathTest3() {
        systemUnderTest.addPlayer(new Player("test"));
        var actual = systemUnderTest.modifyPlayerByPath("test", null);
        assertEquals(actual.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    public void modifyPlayerByPathTest4() {
        systemUnderTest.addPlayer(new Player("test"));
        var actual = systemUnderTest.modifyPlayerByPath("test", new Player(null));
        assertEquals(actual.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    public void modifyPlayerByPathTest5() {
        systemUnderTest.addPlayer(new Player("test"));
        systemUnderTest.modifyPlayerByPath("test", new Player("mock"));
        boolean test = repo.calledMethods.contains("save") && repo.calledMethods.contains("getById");
        assertTrue(repo.calledMethods.contains("deleteById") && test);
    }

    @Test
    public void modifyPlayerByPathTest6() {
        systemUnderTest.addPlayer(new Player("test"));
        var actual = systemUnderTest.modifyPlayerByPath("test", new Player("mock"));
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void modifyPlayerByPathTest7() {
        systemUnderTest.addPlayer(new Player("test"));
        Player mock = new Player("mock");
        systemUnderTest.modifyPlayerByPath("test", mock);
        assertTrue(repo.players.contains(mock));
    }

    @Test
    public void deleteAllTest1() {
        var actual = systemUnderTest.deleteAllPlayers();
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteAllTest2() {
        systemUnderTest.deleteAllPlayers();
        assertTrue(repo.calledMethods.contains("deleteAll"));
    }

    @Test
    public void deleteAllTest3() {
        systemUnderTest.addPlayer(new Player("test"));
        systemUnderTest.deleteAllPlayers();
        assertEquals(repo.players.size(), 0);
    }

    @Test
    public void cannotDeletePlayerWithNullId() {
        var actual = systemUnderTest.deleteById(null);
        assertEquals(actual.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    public void cannotDeletePlayerWithNonexistentId() {
        var actual = systemUnderTest.deleteById("test");
        assertEquals(actual.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteByIdTest3() {
        systemUnderTest.deleteById("test");
        assertTrue(repo.calledMethods.contains("findById") && !repo.calledMethods.contains("deleteById"));

    }

    @Test
    public void deleteByIdTest4() {
        systemUnderTest.addPlayer(new Player("test"));
        var actual = systemUnderTest.deleteById("test");
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteByIdTest5() {
        systemUnderTest.addPlayer(new Player("test"));
        systemUnderTest.deleteById("test");
        assertEquals(repo.players.size(), 0);
    }

    @Test
    public void deleteTest1() {
        var actual = systemUnderTest.deleteByObject(null);
        assertEquals(actual.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteTest2() {
        var actual = systemUnderTest.deleteByObject(new Player(null));
        assertEquals(actual.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteTest3() {
        var actual = systemUnderTest.deleteByObject(new Player("test"));
        assertEquals(actual.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteTest4() {
        systemUnderTest.deleteByObject(new Player("test"));
        assertTrue(repo.calledMethods.contains("findById") && !repo.calledMethods.contains("deleteById"));
    }

    @Test
    public void deleteTest5() {
        systemUnderTest.addPlayer(new Player("test"));
        systemUnderTest.deleteByObject(new Player("test"));
        assertEquals(repo.players.size(), 0);
    }

    @Test
    public void deleteTest6() {
        systemUnderTest.addPlayer(new Player("test"));
        var actual = systemUnderTest.deleteByObject(new Player("test"));
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteTest7() {
        systemUnderTest.addPlayer(new Player("test"));
        systemUnderTest.deleteByObject(new Player("test"));
        assertTrue(repo.calledMethods.contains("deleteById"));
    }
    @Test
    public void comparatorTest(){
        Player p1 = new Player("a");
        p1.incrementScore(100);
        Player p2 = new Player("b");
        p2.incrementScore(200);
        Player p3 = new Player("c");
        p3.incrementScore(300);
        List<Player> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.sort(new PlayerComparator());
        assertEquals(p1,list.get(2));
    }
}
