package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuizzQuestionTest {
    @Test
    public void checkConstructor() {
        var p = new QuizzQuestion("a", new Activity("a", "a", "1", 10l, "a"), new Activity("a", "a", "1", 10l, "a"), new Activity("a", "a", "1", 10l, "a"));
        assertEquals("a", p.getQuestion());
        assertEquals(new Activity("a", "a", "1", 10l, "a"), p.getFirstChoice());
        assertEquals(new Activity("a", "a", "1", 10l, "a"), p.getSecondChoice());
        assertEquals(new Activity("a", "a", "1", 10l, "a"), p.getThirdChoice());
    }

    @Test
    public void emptyConstructorTest() {
        QuizzQuestion x = new QuizzQuestion();
        assertNotNull(x);
    }

    @Test
    public void setQuestionTest() {
        QuizzQuestion x = new QuizzQuestion("a", new Activity("a", "a", "1", 10l, "a"), new Activity("a", "a", "1", 10l, "a"), new Activity("a", "a", "1", 10l, "a"));
        x.setQuestion("abc");
        assertEquals("abc",x.getQuestion());
    }

    @Test
    public void setFirstChoiceTest() {
        QuizzQuestion x = new QuizzQuestion();
        x.setFirstChoice(new Activity("","","",Long.valueOf(0),""));
        assertEquals(new Activity("","","",Long.valueOf(0),""),x.getFirstChoice());
    }

    @Test
    public void setSecondChoiceTest() {
        QuizzQuestion x = new QuizzQuestion();
        x.setSecondChoice(new Activity("","","",Long.valueOf(0),""));
        assertEquals(new Activity("","","",Long.valueOf(0),""),x.getSecondChoice());
    }

    @Test
    public void setThirdChoiceTest() {
        QuizzQuestion x = new QuizzQuestion();
        x.setThirdChoice(new Activity("","","",Long.valueOf(0),""));
        assertEquals(new Activity("","","",Long.valueOf(0),""),x.getThirdChoice());
    }

    @Test
    public void getMostExpensiveTest() {
        QuizzQuestion x = new QuizzQuestion("abc",new Activity("","","1",Long.valueOf(0),""),new Activity("","","2",Long.valueOf(1),""),new Activity("","","3",Long.valueOf(2),""));
        assertEquals("3",x.getMostExpensive());
    }

    @Test
    public void equalsHashCode() {
        var a = new QuizzQuestion("a", new Activity("a", "a", "1", 10l, "a"), new Activity("a", "a", "1", 10l, "a"), new Activity("a", "a", "1", 10l, "a"));
        var b = new QuizzQuestion("a", new Activity("a", "a", "1", 10l, "a"), new Activity("a", "a", "1", 10l, "a"), new Activity("a", "a", "1", 10l, "a"));
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var a = new QuizzQuestion("a", new Activity("a", "a", "1", 10l, "a"), new Activity("a", "a", "1", 10l, "a"), new Activity("a", "a", "1", 10l, "a"));
        var b = new QuizzQuestion("b", new Activity("a", "a", "1", 10l, "a"), new Activity("a", "a", "1", 10l, "a"), new Activity("a", "a", "1", 10l, "a"));
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        var a = new QuizzQuestion("a", new Activity("a", "a", "1", 10l, "a"), new Activity("a", "a", "1", 10l, "a"), new Activity("a", "a", "1", 10l, "a")).toString();
        assertTrue(a.contains("a"));
        assertTrue(a.contains("\n"));
        assertTrue(a.contains("question"));
    }
}


