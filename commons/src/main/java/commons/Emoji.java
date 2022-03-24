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
        return userApplying;
    }

    public String getEmojiType() {
        return emojiType;
    }

    public long getStartTimeEmoji(){
        return startTimeEmoji;
    }

}
