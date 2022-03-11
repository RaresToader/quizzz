package server;

import commons.QuizzQuestion;

public class CorrectAnswer {

    public String getMostExpensive(QuizzQuestion quizzQuestion) {
        long highest = quizzQuestion.getFirstChoice().getConsumption_in_wh();
        long second = quizzQuestion.getSecondChoice().getConsumption_in_wh();
        long third = quizzQuestion.getThirdChoice().getConsumption_in_wh();

        if(second > highest) {
            highest = second;
        }

        if(third > highest) {
            highest = third;
        }

        return Long.toString(highest);
    }
}
