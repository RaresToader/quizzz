package commons;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class RandomSelectionTest {

    private Activity act1 = new Activity("d","lol","what",Long.valueOf(2214321),"what source");
    private Activity act2 = new Activity("r","lol","yo",Long.valueOf(2214321),"wahhg");

    @Test
    public void emptyConstructorTest() {
        RandomSelection x = new RandomSelection();
        List<QuizzQuestion> listOfQuestions = new ArrayList<>();
        listOfQuestions.add(new QuizzQuestion("What activity costs more?",act1,act2,act1));
        listOfQuestions.add(new QuizzQuestion("What activity costs more?",act2,act1,act2));
        listOfQuestions.add(new QuizzQuestion("What activity costs more?",act1,act2,act1));
        assertEquals(listOfQuestions,x.getListOfQuestions());
    }

    @Test
    public void nextTest() {
        RandomSelection x = new RandomSelection();
        assertEquals(new QuizzQuestion("What activity costs more?",act1,act2,act1),x.next());
    }

    @Test
    public void hasNextTrueTest() {
        RandomSelection x = new RandomSelection();
        assertTrue(x.hasNext());
    }

    @Test
    public void hasNextFalseTest() {
        RandomSelection x = new RandomSelection();
        x.next();
        x.next();
        x.next();
        x.next();
        assertFalse(x.hasNext());
    }

    @Test
    public void setListOfQuestionsTest() {
        RandomSelection x = new RandomSelection();
        x.setListOfQuestions(null);
        assertNull(x.getListOfQuestions());
    }

    @Test
    public void getIndexTest() {
        RandomSelection x = new RandomSelection();
        assertEquals(0,x.getIndex());
    }

}
