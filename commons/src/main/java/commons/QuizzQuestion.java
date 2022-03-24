package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class QuizzQuestion extends Question {
    private String question;
    private Activity firstChoice;
    private Activity secondChoice;
    private Activity thirdChoice;



    public QuizzQuestion(String question, Activity firstChoice, Activity secondChoice, Activity thirdChoice) {
        super(question);
        this.firstChoice = firstChoice;
        this.secondChoice = secondChoice;
        this.thirdChoice = thirdChoice;
    }
    public QuizzQuestion(){
        
    }
    public void setQuestion(String question) {
        this.question = question;
    }

    public Activity getFirstChoice() {
        return firstChoice;
    }

    public void setFirstChoice(Activity firstChoice) {
        this.firstChoice = firstChoice;
    }

    public Activity getSecondChoice() {
        return secondChoice;
    }

    public void setSecondChoice(Activity secondChoice) {
        this.secondChoice = secondChoice;
    }

    public Activity getThirdChoice() {
        return thirdChoice;
    }

    public void setThirdChoice(Activity thirdChoice) {
        this.thirdChoice = thirdChoice;
    }



    public String getMostExpensive() {
        Activity highest = this.firstChoice;
        Activity second = this.secondChoice;
        Activity third = this.thirdChoice;

        if (second.getConsumption_in_wh() > highest.getConsumption_in_wh()) {
            highest = second;
        }

        if (third.getConsumption_in_wh() > highest.getConsumption_in_wh()) {
            highest = third;
        }

        return highest.getTitle();
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }



}
