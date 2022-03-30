package commons;

import java.util.Date;
import java.util.Objects;

public class Emoji {
    private String userApplying;
    private String emojiType;
    private long startTimeEmoji;

    /**
     * Constructor for Emoij
     * @param userApplying - The player that clicks the emoij
     * @param emoijType - The type of the emoij
     */
    public Emoji(String userApplying, String emoijType) {
        this.userApplying = userApplying;
        this.emojiType = emoijType;
        Date date = new Date();
        this.startTimeEmoji = date.getTime();
    }

    private Emoji() {
        //for Object Mapping
    }

    public String getUserApplying() {
        return this.userApplying;
    }

    public String getEmojiType() {
        return this.emojiType;
    }

    public long getStartTimeEmoji(){
        return this.startTimeEmoji;
    }

    /**
     * Set's the start time of the emoji (for test purposes)
     * @param startTimeEmoji - the start time of the emoji.
     */
    public void setStartTimeEmoji(long startTimeEmoji){
        this.startTimeEmoji = startTimeEmoji;
    }

    /**
     * checks if to emoji  are equal (same time/user/emojiType)
     * @param o - Object that might be equal to a specific emoji
     * @return boolean - that concludes if the emoji is equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Emoji emoji = (Emoji) o;
        return getStartTimeEmoji() == emoji.getStartTimeEmoji() && Objects.equals(getUserApplying(), emoji.getUserApplying()) && Objects.equals(getEmojiType(), emoji.getEmojiType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserApplying(), getEmojiType(), getStartTimeEmoji());
    }

    /**
     * Makes a string with all the information of the emoji
     * @return String with info about the an emoji
     */
    @Override
    public String toString() {
        return "Emoji{" +
                "userApplying='" + userApplying + '\'' +
                ", emojiType='" + emojiType + '\'' +
                ", startTimeEmoji=" + startTimeEmoji +
                '}';
    }
}
