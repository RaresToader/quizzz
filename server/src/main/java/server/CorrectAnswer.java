package server;

import commons.QuizzQuestion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/correct")
public class CorrectAnswer {

    @GetMapping("/")
    @ResponseBody
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
