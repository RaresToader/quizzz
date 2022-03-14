package commons;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnswerTest {
    @Test
    public void emptyAnswerTest() {
        Answer x = new Answer();
        assertNull(x.getNickname());
        assertEquals(0,x.getAnswer());
        assertEquals(0,x.getQuestionNum());
    }

    @Test
    public void nonEmptyAnswerTest() {
        Answer x = new Answer("test",1,2);
        assertEquals("test",x.getNickname());
        assertEquals(1,x.getAnswer());
        assertEquals(2,x.getQuestionNum());
    }

    @Test
    public void setNicknameTest() {
        Answer x = new Answer("test",1,2);
        x.setNickname("test2");
        assertEquals("test2",x.getNickname());
    }

    @Test
    public void setAnswerTest() {
        Answer x = new Answer("test",1,2);
        x.setAnswer(3);
        assertEquals(3,x.getAnswer());
    }

    @Test
    public void setQuestionNumTest() {
        Answer x = new Answer("test",1,2);
        x.setQuestionNum(5);
        assertEquals(5,x.getQuestionNum());
    }

}
