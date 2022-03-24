package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class QuizzQuestionServerParsedTest {
    @Test
    public void emptyQuestionTest() {
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed();
        assertNull(x.getQuestion());
        assertEquals(0,x.getStartTime());
        assertEquals(0,x.getQuestionNum());
        assertNull(x.getJokerList());
    }

    @Test
    public void nonEmptyQuestionTest() {
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed(new QuizzQuestion("0", null, null, null),123,-1, new ArrayList<Joker>());
        assertEquals(new QuizzQuestion("0", null, null, null),x.getQuestion());
        assertEquals(123,x.getStartTime());
        assertEquals(-1,x.getQuestionNum());
        assertEquals(0,x.getJokerList().size());
    }

    @Test
    public void setQuestionTest() {
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed(null,0,-1,null);
        x.setQuestion(new QuizzQuestion("0", null, null, null));
        assertEquals(new QuizzQuestion("0", null, null, null),x.getQuestion());
    }

    @Test
    public void setTimeTest() {
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed(null,0,-1,null);
        x.setStartTime(123);
        assertEquals(123,x.getStartTime());
    }

    @Test
    public void setQuestionNumTest() {
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed(null,123,-1,null);
        x.setQuestionNum(12);
        assertEquals(12,x.getQuestionNum());
    }

    @Test
    public void setJokerListTest() {
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed(null,123,-1,null);
        x.setJokerList(new ArrayList<Joker>());
        assertEquals(0,x.getJokerList().size());
    }

    @Test
    public void toStringTest() {
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed(null,123,-1,null);
        String expected = "QuizzQuestionServerParsed{" +
                "question=" + x.getQuestion() +
                ", startTime=" + x.getStartTime() +
                ", questionNum=" + x.getQuestionNum() +
                ", jokerList=" + x.getJokerList() +
                '}';
        assertEquals(expected,x.toString());
    }

    @Test
    public void equalsNullTest() {
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed(null,123,-1,null);
        assertFalse(x.equals(null));
    }

    @Test
    public void sameInstanceEqualsTest() {
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed(null,123,-1,null);
        assertTrue(x.equals(x));
    }

    @Test
    public void diffEqualsTest() {
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed(null,123,-1,null);
        QuizzQuestionServerParsed y = new QuizzQuestionServerParsed(null,1253,-1,null);
        assertFalse(x.equals(y));
    }

    @Test
    public void sameEqualsTest() {
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed(null,123,-1,null);
        QuizzQuestionServerParsed y = new QuizzQuestionServerParsed(null,123,-1,null);
        assertTrue(x.equals(y));
    }
}
