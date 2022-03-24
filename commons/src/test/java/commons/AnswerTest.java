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

    @Test
    public void toStringTest() {
        Answer x = new Answer("test",1,2);
        String expected = "Answer{" +
                "nickname='" + x.getNickname() + '\'' +
                ", answer=" + x.getAnswer() +
                ", questionNum=" + x.getQuestionNum() +
                '}';

        assertEquals(expected,x.toString());
    }

    @Test
    public void equalsNullTest() {
        Answer x = new Answer("test",1,2);
        assertFalse(x.equals(null));
    }

    @Test
    public void sameInstanceEqualsTest() {
        Answer x = new Answer("test",1,2);
        assertTrue(x.equals(x));
    }

    @Test
    public void diffEqualsTest() {
        Answer x = new Answer("test",1,2);
        Answer y = new Answer("test3",1,2);
        assertFalse(x.equals(y));
    }

    @Test
    public void sameEqualsTest() {
        Answer x = new Answer("test",1,2);
        Answer y = new Answer("test",1,2);
        assertTrue(x.equals(y));
    }

}
