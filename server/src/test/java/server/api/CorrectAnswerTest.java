package server.api;

import commons.Activity;
import commons.QuizzQuestion;
import org.junit.jupiter.api.Test;
import server.CorrectAnswer;

import static org.junit.jupiter.api.Assertions.*;

class CorrectAnswerTest {

    @Test
    public void getMostExpensive() {
        Activity first = new Activity("test", "test", "Using an air conditioner for 1 hour during summer", 3250l
                , "path");
        Activity second = new Activity("Flying for 5km in a Boeing 707", "test","https://en.wikipedia.org/wiki/Energy_efficiency_in_transport" ,1805l,
                 "path");
        Activity third = new Activity("Relaxing in the jacuzzi for 30 minutes", "test","https://www.directenergy.com/learning-center/how-much-electricity-does-a-hot-tub-use" ,1500l,
                 "path");
        QuizzQuestion test = new QuizzQuestion("Which is the most expensive?", first, second, third);

        assertEquals("Using an air conditioner for 1 hour during summer",
                new CorrectAnswer().getMostExpensive(test));
    }

    @Test
    public void getSecondExpensive() {
        Activity first = new Activity("test", "test", "Using an air conditioner for 1 hour during summer", 3250l
                , "path");
        Activity second = new Activity("Flying for 5km in a Boeing 707", "test","https://en.wikipedia.org/wiki/Energy_efficiency_in_transport" ,5000l,
                "path");
        Activity third = new Activity("Relaxing in the jacuzzi for 30 minutes", "test","https://www.directenergy.com/learning-center/how-much-electricity-does-a-hot-tub-use" ,1500l,
                "path");
        QuizzQuestion test = new QuizzQuestion("Which is the most expensive?", first, second, third);

        assertEquals("https://en.wikipedia.org/wiki/Energy_efficiency_in_transport",
                new CorrectAnswer().getMostExpensive(test));
    }

    @Test
    public void getThirdExpensive() {
        Activity first = new Activity("test", "test", "Using an air conditioner for 1 hour during summer", 3250l
                , "path");
        Activity second = new Activity("Flying for 5km in a Boeing 707", "test","https://en.wikipedia.org/wiki/Energy_efficiency_in_transport" ,5000l,
                "path");
        Activity third = new Activity("Relaxing in the jacuzzi for 30 minutes", "test","https://www.directenergy.com/learning-center/how-much-electricity-does-a-hot-tub-use" ,5500l,
                "path");
        QuizzQuestion test = new QuizzQuestion("Which is the most expensive?", first, second, third);

        assertEquals("https://www.directenergy.com/learning-center/how-much-electricity-does-a-hot-tub-use",
                new CorrectAnswer().getMostExpensive(test));
    }

}