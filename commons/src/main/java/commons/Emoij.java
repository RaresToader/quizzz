package commons;

public class Emoij {
    private String userApplying;
    private String emoijType;

    /**
     * Constructor for Emoij
     * @param userApplying - The player that clicks the emoij
     * @param emoijType - The type of the emoij
     */
    public Emoij(String userApplying, String emoijType) {
        this.userApplying = userApplying;
        this.emoijType = emoijType;
    }

    public String getUserApplying() {
        return userApplying;
    }

    public String getEmoijType() {
        return emoijType;
    }

    public void setUserApplying(String userApplying) {
        this.userApplying = userApplying;
    }

    public void setEmoijType(String emoijType) {
        this.emoijType = emoijType;
    }
}
