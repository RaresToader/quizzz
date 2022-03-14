package commons;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class QuizzQuestionServerParsedTest {
    @Test
    public void emptyQuestionTest() {
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed();
        assertNull(x.getQuestion());
        assertNull(x.getStartTime());
        assertEquals(0,x.getQuestionNum());
    }

    @Test
    public void nonEmptyQuestionTest() {
        LocalDate time = LocalDate.now();
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed(new QuizzQuestion("0", null, null, null),time,-1);
        assertEquals(new QuizzQuestion("0", null, null, null),x.getQuestion());
        assertEquals(time,x.getStartTime());
        assertEquals(-1,x.getQuestionNum());
    }

    @Test
    public void setQuestionTest() {
        LocalDate time = LocalDate.now();
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed(null,time,-1);
        x.setQuestion(new QuizzQuestion("0", null, null, null));
        assertEquals(new QuizzQuestion("0", null, null, null),x.getQuestion());
    }

    @Test
    public void setTimeTest() {
        LocalDate time = LocalDate.now();
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed(null,null,-1);
        x.setStartTime(time);
        assertEquals(time,x.getStartTime());
    }

    @Test
    public void setQuestioNumTest() {
        QuizzQuestionServerParsed x = new QuizzQuestionServerParsed(null,null,-1);
        x.setQuestionNum(12);
        assertEquals(12,x.getQuestionNum());
    }
}
