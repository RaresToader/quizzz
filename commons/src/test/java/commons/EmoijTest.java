package commons;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class EmoijTest {
    @Test
    public void constructorTest1() {
        Emoji emoji = new Emoji("user","type");
        assertNotNull(emoji);
    }

    @Test
    public void constructorTest2() {
        Emoji emoji = new Emoji("user","type");
        assertTrue("user".equals(emoji.getUserApplying()));
    }

    @Test
    public void constructorTest3() {
        Emoji emoji = new Emoji("user","type");
        assertEquals("type", emoji.getEmojiType());
    }

    @Test
    public void getUserApplyingTest() {
        Emoji emoji = new Emoji("user","type");
        assertEquals("user",emoji.getUserApplying());
    }
    @Test
    public void getEmojiTypeTest(){
        Emoji emoji = new Emoji("user","type");
        assertEquals("type", emoji.getEmojiType());
    }

    @Test
    public void setStartTimeEmojiTest() {
        Emoji emoji = new Emoji("user","type");
        Date date = new Date();
        emoji.setStartTimeEmoji(date.getTime());
        assertTrue(date.getTime() + 50 > emoji.getStartTimeEmoji());
        assertTrue(date.getTime() - 50 < emoji.getStartTimeEmoji());
    }
    @Test
    public void getStartTimeEmojiTest(){
        Emoji emoji = new Emoji("user","type");
        assertNotNull(emoji.getStartTimeEmoji());
        Date date = new Date();
        assertTrue(date.getTime() + 50 > emoji.getStartTimeEmoji());
        assertTrue(date.getTime() - 50 < emoji.getStartTimeEmoji());
    }

    @Test
    public void equalsTest1() {
        Emoji emoji1 = new Emoji("user","type");
        Emoji emoji2 = new Emoji("user","type");
        assertEquals(emoji1, emoji2);
    }

    @Test
    public void equalsTest2() {
        Emoji emoji1 = new Emoji("user","type");
        Emoji emoji2 = new Emoji("user2","type");
        assertNotEquals(emoji1, emoji2);
    }

    @Test
    public void equalsTest3() {
        Emoji emoji1 = new Emoji("user","type");
        Emoji emoji2 = new Emoji("user","type2");
        assertNotEquals(emoji1, emoji2);
    }

    @Test
    public void equalsTest4() {
        Emoji emoji = new Emoji("user","type");
        assertNotEquals(emoji, null);
    }

    @Test
    public void equalsTest5() {
        Emoji emoji = new Emoji("user","type");
        assertEquals(emoji,emoji);
    }
    @Test
    public void equalsTest6() {
        Emoji emoji1 = new Emoji("user","type");
        Emoji emoji2 = new Emoji("user","type2");
        assertNotEquals(emoji1,emoji2);
    }

    @Test
    public void hashCodeTest() {
        Emoji emoji1 = new Emoji("user","type");
        Emoji emoji2 = new Emoji("user","type");
        assertTrue(emoji1.hashCode() == emoji2.hashCode());
    }

    @Test
    public void toStringTest() {
        Emoji emoji = new Emoji("user","type");
        String toString = "Emoji{" +
                "userApplying='" + emoji.getUserApplying() + '\'' +
                ", emojiType='" + emoji.getEmojiType() + '\'' +
                ", startTimeEmoji=" + emoji.getStartTimeEmoji() +
                '}';
        assertEquals(toString, emoji.toString());
    }

}
