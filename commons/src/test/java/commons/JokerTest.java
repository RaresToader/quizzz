package commons;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class JokerTest {

    @Test
    public void emptyJokerTest() {
        Joker x = new Joker();
        assertEquals(0,x.getJokerType());
        assertEquals(0,x.getQuestionNum());
        assertNull(x.getUsedBy());
    }

    @Test
    public void nonEmptyJokerTest() {
        Joker x = new Joker("testUser",1,2);
        assertEquals(1,x.getJokerType());
        assertEquals(2,x.getQuestionNum());
        assertEquals("testUser",x.getUsedBy());
    }

    @Test
    public void setJokerTypeTest() {
        Joker x = new Joker("testUser",1,2);
        x.setJokerType(3);
        assertEquals(3,x.getJokerType());
    }

    @Test
    public void setQuestionNumTest() {
        Joker x = new Joker("testUser",1,2);
        x.setQuestionNum(5);
        assertEquals(5,x.getQuestionNum());
    }

    @Test
    public void setUsedByTest() {
        Joker x = new Joker("testUser",1,2);
        x.setUsedBy("test123");
        assertEquals("test123",x.getUsedBy());
    }
}
