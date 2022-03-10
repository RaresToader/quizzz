package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuizzQuestionTest {
    @Test
    public void checkConstructor() {
        var p = new QuizzQuestion("a", new Activity("a", 1, "v"), new Activity("a", 1, "v"), new Activity("a", 1, "v"));
        assertEquals("a", p.getQuestion());
        assertEquals(new Activity("a", 1, "v"), p.getFirstChoice());
        assertEquals(new Activity("a", 1, "v"), p.getSecondChoice());
        assertEquals(new Activity("a", 1, "v"), p.getThirdChoice());
    }

    @Test
    public void equalsHashCode() {
        var a = new QuizzQuestion("a", new Activity("a", 1, "v"), new Activity("a", 1, "v"), new Activity("a", 1, "v"));
        var b = new QuizzQuestion("a", new Activity("a", 1, "v"), new Activity("a", 1, "v"), new Activity("a", 1, "v"));
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var a = new QuizzQuestion("b", new Activity("a", 1, "v"), new Activity("a", 1, "v"), new Activity("a", 1, "v"));
        var b = new QuizzQuestion("a", new Activity("a", 1, "v"), new Activity("a", 1, "v"), new Activity("a", 1, "v"));
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        var actual = new QuizzQuestion("a", new Activity("a", 1, "v"), new Activity("a", 1, "v"), new Activity("a", 1, "v")).toString();
        assertTrue(actual.contains("a"));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("question"));
    }
}


