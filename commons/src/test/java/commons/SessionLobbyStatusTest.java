package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SessionLobbyStatusTest {
    @Test
    public void emptyConstructorTest() {
        SessionLobbyStatus x = new SessionLobbyStatus();
        assertNotNull(x);
    }

    @Test
    public void nonEmptyConstructorTest() {
        SessionLobbyStatus x = new SessionLobbyStatus(new ArrayList<Emoji>(), false, "test");
        assertNotNull(x);
        assertEquals(new ArrayList<Emoji>(), x.getEmojiList());
        assertFalse(x.isStarted());
        assertEquals("test", x.getGameAdmin());
    }

    @Test
    public void setEmojiListTest() {
        SessionLobbyStatus x = new SessionLobbyStatus();
        x.setEmojiList(new ArrayList<Emoji>());
        assertEquals(new ArrayList<Emoji>(), x.getEmojiList());
    }

    @Test
    public void setStartedTest() {
        SessionLobbyStatus x = new SessionLobbyStatus();
        x.setStarted(true);
        assertTrue(x.isStarted());
    }

    @Test
    public void setGameAdminSet() {
        SessionLobbyStatus x = new SessionLobbyStatus();
        x.setGameAdmin("test");
        assertEquals("test",x.getGameAdmin());
    }

    @Test
    public void toStringTest() {
        SessionLobbyStatus x = new SessionLobbyStatus(new ArrayList<Emoji>(), false, "test");
        String expected = "SessionLobbyStatus{" +
                "emojiList=" + x.getEmojiList() +
                ", started=" + x.isStarted() +
                ", gameAdmin='" + x.getGameAdmin() + '\'' +
                '}';
        assertEquals(expected,x.toString());
    }

    @Test
    public void equalsWithNullTest() {
        SessionLobbyStatus x = new SessionLobbyStatus();
        assertFalse(x.equals(null));
    }

    @Test
    public void equalsWithSameInstance() {
        SessionLobbyStatus x = new SessionLobbyStatus();
        assertTrue(x.equals(x));
    }

    @Test
    public void equalsNotTest() {
        SessionLobbyStatus x = new SessionLobbyStatus();
        SessionLobbyStatus y = new SessionLobbyStatus(new ArrayList<Emoji>(), false, "test");
        assertFalse(x.equals(y));
    }

    @Test
    public void equalsTrueTest() {
        SessionLobbyStatus x = new SessionLobbyStatus(new ArrayList<Emoji>(), false, "test");
        SessionLobbyStatus y = new SessionLobbyStatus(new ArrayList<Emoji>(), false, "test");
        assertTrue(x.equals(y));
    }

}