package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuizzQuestionTest {
    @Test
    public void checkConstructor() {
        var p = new QuizzQuestion("a", new Activity("a", "a", 1, "a", "a"), new Activity("a", "a", 1, "a", "a"), new Activity("a", "a", 1, "a", "a"));
        assertEquals("a", p.getQuestion());
        assertEquals(new Activity("a", "a", 1, "a", "a"), p.getFirstChoice());
        assertEquals(new Activity("a", "a", 1, "a", "a"), p.getSecondChoice());
        assertEquals(new Activity("a", "a", 1, "a", "a"), p.getThirdChoice());
    }

    @Test
    public void equalsHashCode() {
        var a = new QuizzQuestion("a", new Activity("a","a" ,1,"a", "a"), new Activity("a","a" ,1,"a", "a"),  new Activity("a","a" ,1,"a", "a"));
        var b = new QuizzQuestion("a", new Activity("a","a" ,1,"a", "a"), new Activity("a","a" ,1,"a", "a"),  new Activity("a","a" ,1,"a", "a"));
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var a = new QuizzQuestion("a", new Activity("a","a" ,1,"a", "a"), new Activity("a","a" ,1,"a", "a"),  new Activity("a","a" ,1,"a", "a"));
        var b = new QuizzQuestion("b", new Activity("a","a" ,1,"a", "a"), new Activity("a","a" ,1,"a", "a"),  new Activity("a","a" ,1,"a", "a"));
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        var a = new QuizzQuestion("a", new Activity("a","a" ,1,"a", "a"), new Activity("a","a" ,1,"a", "a"),  new Activity("a","a" ,1,"a", "a")).toString();
        assertTrue(a.contains("a"));
        assertTrue(a.contains("\n"));
        assertTrue(a.contains("question"));
    }
}


