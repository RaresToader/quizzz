package commons;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Those annotations help us distinguish between subtypes
 * source: https://www.baeldung.com/jackson-inheritance
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(QuizzQuestion.class),
        @JsonSubTypes.Type(ConsumpQuestion.class),
        @JsonSubTypes.Type(InsteadOfQuestion.class),
        @JsonSubTypes.Type(GuessQuestion.class)
})
public abstract class Question {

    private String question;

    public Question(String question) {
        this.question = question;
    }

    public Question() {

    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }
}
