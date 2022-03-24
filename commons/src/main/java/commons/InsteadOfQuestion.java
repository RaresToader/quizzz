package commons;

public class InsteadOfQuestion extends Question {

    private String question;
    private Activity promptActivity;
    private Activity firstChoice;
    private Activity secondChoice;
    private Activity thirdChoice;

    /**
     * Default constructor for objectMapping
     */
    public InsteadOfQuestion() {

    }

    /**
     * Constructor for InsteadOf type Question
     *
     * @param promptActivity the prompted activity
     * @param firstChoice    the first alternative activity
     * @param secondChoice   the second alternative activity
     * @param thirdChoice    the third alternative activity
     */
    public InsteadOfQuestion(Activity promptActivity, Activity firstChoice, Activity secondChoice, Activity thirdChoice) {
        super();
        this.question = "Instead of..., you could do instead...";
        this.promptActivity = promptActivity;
        this.firstChoice = firstChoice;
        this.secondChoice = secondChoice;
        this.thirdChoice = thirdChoice;
    }

    /**
     * Getter for the question
     *
     * @return the question, as a string
     */
    @Override
    public String getQuestion() {
        return question;
    }

    /**
     * Get the prompted activity
     *
     * @return the Activity that is prompted
     */
    public Activity getPromptActivity() {
        return promptActivity;
    }

    /**
     * Getter for the first choice activity
     *
     * @return the first choice Activity
     */
    public Activity getFirstChoice() {
        return firstChoice;
    }

    /**
     * Getter for the second choice activity
     *
     * @return the second choice Activity
     */
    public Activity getSecondChoice() {
        return secondChoice;
    }

    /**
     * Getter for the third choice activity
     *
     * @return the third choice Activity
     */
    public Activity getThirdChoice() {
        return thirdChoice;
    }

    /**
     * toString method for the InsteadOfQuestion class
     *
     * @return a string, containing the attributes from the instead of Question class
     */
    @Override
    public String toString() {
        return "InsteadOfQuestion{" +
                "question='" + question + '\'' +
                ", promptActivity=" + promptActivity +
                ", firstChoice=" + firstChoice +
                ", secondChoice=" + secondChoice +
                ", thirdChoice=" + thirdChoice +
                '}';
    }

    /**
     * Equals method for the InsteadOfQuestion class
     *
     * @param o an object, to be compared to
     * @return whether this object is equal to o
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InsteadOfQuestion that)) return false;
        return getQuestion().equals(that.getQuestion()) && getPromptActivity().equals(that.getPromptActivity()) && getFirstChoice().equals(that.getFirstChoice()) && getSecondChoice().equals(that.getSecondChoice()) && getThirdChoice().equals(that.getThirdChoice());
    }
}
