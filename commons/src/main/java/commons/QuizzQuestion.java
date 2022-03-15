

package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class QuizzQuestion {

    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    private String question;
    private Activity firstChoice;
    private Activity secondChoice;
    private Activity thirdChoice;

    public QuizzQuestion() {
        //Object mapping
    }

    public QuizzQuestion(String question, Activity firstChoice, Activity secondChoice, Activity thirdChoice) {
        this.question = question;
        this.firstChoice = firstChoice;
        this.secondChoice = secondChoice;
        this.thirdChoice = thirdChoice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
