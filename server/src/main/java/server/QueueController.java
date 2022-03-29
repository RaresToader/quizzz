package server;

import commons.Activity;
import commons.QuizzQuestion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/correct")
public class QueueController {

    @GetMapping("/mostExpensive")
    @ResponseBody
    public String getMostExpensive(QuizzQuestion quizzQuestion) {
        Activity highest = quizzQuestion.getFirstChoice();
        Activity second = quizzQuestion.getSecondChoice();
        Activity third = quizzQuestion.getThirdChoice();

        if (second.getConsumption_in_wh() > highest.getConsumption_in_wh()) {
            highest = second;
        }

        if (third.getConsumption_in_wh() > highest.getConsumption_in_wh()) {
            highest = third;
        }

        return highest.getTitle();
    }

}
