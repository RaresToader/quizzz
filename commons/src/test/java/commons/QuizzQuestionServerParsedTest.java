package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuizzQuestionServerParsedTest {
    @Test
    public void emptyQuestionTest() {
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed();
        assertNull(x.getQuestion());
        assertEquals(0,x.getStartTime());
        assertEquals(0,x.getQuestionNum());
    }

    @Test
    public void nonEmptyQuestionTest() {
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed(new QuizzQuestion("0", null, null, null, 0),123,-1);
        assertEquals(new QuizzQuestion("0", null, null, null, 0),x.getQuestion());
        assertEquals(123,x.getStartTime());
        assertEquals(-1,x.getQuestionNum());
    }

    @Test
    public void setQuestionTest() {
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed(null,0,-1);
        x.setQuestion(new QuizzQuestion("0", null, null, null, 1));
        assertEquals(new QuizzQuestion("0", null, null, null, 1),x.getQuestion());
    }

    @Test
    public void setTimeTest() {
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed(null,0,-1);
        x.setStartTime(123);
        assertEquals(123,x.getStartTime());
    }

    @Test
    public void setQuestionNumTest() {
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed(null,123,-1);
        x.setQuestionNum(12);
        assertEquals(12,x.getQuestionNum());
    }
}
