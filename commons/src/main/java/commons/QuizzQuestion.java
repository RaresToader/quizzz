package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class QuizzQuestion {
    private String question;
    private Activity firstChoice;
    private Activity secondChoice;
    private Activity thirdChoice;
    private int questionType;

    public QuizzQuestion() {
        //Object mapping
    }

    public QuizzQuestion(String question, Activity firstChoice, Activity secondChoice, Activity thirdChoice, int questionType) {
        this.question = question;
        this.firstChoice = firstChoice;
        this.secondChoice = secondChoice;
        this.thirdChoice = thirdChoice;
        this.questionType = questionType;
    }

    public String getQuestion() {
        return question;
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

    public int getQuestionType() {
        return questionType;
    }

    public String getCorrectAnswer() {
        String correct = "";
        if(questionType == 0) {
            correct = getMostExpensive();
        }

        if(questionType == 1) {
            correct = getConsump();
        }

        return correct;
    }

    private String getConsump() {
        String[] parts = this.question.split(": ");
        String title = parts[1];

        if(this.firstChoice.getTitle().equals(title)) {
            return Long.toString(firstChoice.getConsumption_in_wh());
        }

        if(this.secondChoice.getTitle().equals(title)) {
            return Long.toString(secondChoice.getConsumption_in_wh());
        }

        else return Long.toString(thirdChoice.getConsumption_in_wh());
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
