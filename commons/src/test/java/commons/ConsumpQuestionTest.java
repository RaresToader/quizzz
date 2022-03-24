package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConsumpQuestionTest {

    ConsumpQuestion c = new ConsumpQuestion("How much does it take: Using an electric stove for 1 hour",
            new Activity("02-electric_stove", "02/electric_stove.png" ,"Using an electric stove for 1 hour", 1200L,
            "https://blog.arcadia.com/electricity-costs-10-key-household-products/"), 1200L, 2000L, 300L);

    @Test
    public void getActivity() {
        Activity res = c.getActivity();
        Activity expected = new Activity("02-electric_stove", "02/electric_stove.png" ,"Using an electric stove for 1 hour",1200L,
                "https://blog.arcadia.com/electricity-costs-10-key-household-products/");
        assertEquals(expected, res);
    }

    @Test
    public void getFirst() {
        assertEquals(1200L, c.getFirst());
    }

    @Test
    public void getSecond() {
        assertEquals(2000L, c.getSecond());

    }

    @Test
    public void getThird() {
        assertEquals(300L, c.getThird());
    }

    @Test
    public void getConsump() {
        assertEquals("1200", c.getConsump());
    }

    @Test
    public void testEquals() {
        ConsumpQuestion q = new ConsumpQuestion("How much does it take: Using an electric stove for 1 hour",
                new Activity("02-electric_stove", "02/electric_stove.png" ,"Using an electric stove for 1 hour", 1200L,
                        "https://blog.arcadia.com/electricity-costs-10-key-household-products/"), 1200L, 2000L, 300L);
        assertTrue(c.equals(q));
    }

    @Test
    public void testNotEquals() {
        ConsumpQuestion q = new ConsumpQuestion("How much does it take: Using an electric stove for 1 hour",
                new Activity("02-electric_stove", "02/electric_stove.png", "Using an electric stove for 1 hour", 1500L,
                        "https://blog.arcadia.com/electricity-costs-10-key-household-products/"), 1200L, 2000L, 300L);
        assertFalse(c.equals(q));
    }

    @Test
    public void testToString() {
        String expected = "ConsumpQuestion{" +
                "question='" + "How much does it take: Using an electric stove for 1 hour" + '\'' +
                ", activity=" + "Activity{" +
                "id='" + "02-electric_stove" + '\'' +
                ", title='" + "Using an electric stove for 1 hour" + '\'' +
                ", consumption_in_wh=" + "1200" +
                ", source='" + "https://blog.arcadia.com/electricity-costs-10-key-household-products/" + '\'' +
                ", image_path='" + "02/electric_stove.png" + '\'' +
                '}' +
                ", first=" + "1200" +
                ", second=" + "2000" +
                ", third=" + "300" +
                '}';
        assertEquals(expected, c.toString());
    }
}