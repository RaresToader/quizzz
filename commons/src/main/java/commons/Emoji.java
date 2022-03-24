package commons;

import java.util.Date;

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

}
