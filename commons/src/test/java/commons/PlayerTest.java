package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    @Test
    public void constructorTest1() {
        Player player = new Player("name");
        assertNotNull(player);
    }

    @Test
    public void constructorTest2() {
        Player player = new Player("name");
        assertTrue("name".equals(player.getUsername()));
    }

    @Test
    public void constructorTest3() {
        Player player = new Player("name");
        assertEquals(0, player.getScore());
    }

    @Test
    public void getUsernameTest() {
        Player player = new Player("name");
        assertEquals(player.getUsername(), "name");
    }

    @Test
    public void getScoreTest() {
        Player player = new Player("name");
        player.incrementScore(100);
        assertEquals(100, player.getScore());
    }

    @Test
    public void incrementScoreTest() {
        Player player = new Player("name");
        player.incrementScore(100);
        player.incrementScore(300);
        assertEquals(400, player.getScore());
    }

    @Test
    public void equalsTest1() {
        Player playerOne = new Player("name");
        Player playerTwo = new Player("name");
        assertEquals(playerTwo, playerTwo);
    }

    @Test
    public void equalsTest2() {
        Player playerOne = new Player("name");
        Player playerTwo = new Player("nameTwo");
        assertNotEquals(playerOne, playerTwo);
    }

    @Test
    public void equalsTest3() {
        Player playerOne = new Player("name");
        Player playerTwo = new Player("name");
        playerOne.incrementScore(100);
        assertNotEquals(playerOne, playerTwo);
    }

    @Test
    public void equalsTest4() {
        Player playerOne = new Player("name");
        assertNotEquals(playerOne, null);
    }

    @Test
    public void equalsTest5() {
        Player playerOne = new Player("name");
        assertEquals(playerOne, playerOne);
    }

    @Test
    public void toStringTest() {
        Player player = new Player("name");
        String toString = "Player{username=" + "'name', score=" + 0 + "}";
        assertEquals(toString, player.toString());
    }

}
