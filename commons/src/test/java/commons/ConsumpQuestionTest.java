package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConsumpQuestionTest {

    ConsumpQuestion c = new ConsumpQuestion("How much does it take: Using an electric stove for 1 hour",
            new Activity("02-electric_stove", "02/electric_stove.png" ,"Using an electric stove for 1 hour", 1200L,
            "https://blog.arcadia.com/electricity-costs-10-key-household-products/"), 1200L, 2000L, 300L);

    @Test
    void getActivity() {
        Activity res = c.getActivity();
        Activity expected = new Activity("02-electric_stove", "02/electric_stove.png" ,"Using an electric stove for 1 hour",1200L,
                "https://blog.arcadia.com/electricity-costs-10-key-household-products/");
        assertEquals(expected, res);
    }

    @Test
    void getFirst() {
        assertEquals(1200L, c.getFirst());
    }

    @Test
    void getSecond() {
        assertEquals(2000L, c.getSecond());

    }

    @Test
    void getThird() {
        assertEquals(300L, c.getThird());
    }

    @Test
    void getConsump() {
        assertEquals("1200", c.getConsump());
    }
}