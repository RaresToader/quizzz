package server.api;

import commons.Activity;
import commons.QuizzQuestion;
import org.junit.jupiter.api.Test;
import server.CorrectAnswer;

import static org.junit.jupiter.api.Assertions.*;

class CorrectAnswerTest {

    @Test
    void getMostExpensive() {
        Activity first = new Activity("Using an air conditioner for 1 hour during summer", "test", "https://americanhomewater.com/how-much-power-does-an-air-conditioner-use/", 3250l
                , "path");
        Activity second = new Activity("Flying for 5km in a Boeing 707", "test","https://en.wikipedia.org/wiki/Energy_efficiency_in_transport" ,1805l,
                 "path");
        Activity third = new Activity("Relaxing in the jacuzzi for 30 minutes", "test","https://www.directenergy.com/learning-center/how-much-electricity-does-a-hot-tub-use" ,1500l,
                 "path");
        QuizzQuestion test = new QuizzQuestion("Which is the most expensive?", first, second, third);

        assertEquals("Using an air conditioner for 1 hour during summer",
                new CorrectAnswer().getMostExpensive(test));
    }
}