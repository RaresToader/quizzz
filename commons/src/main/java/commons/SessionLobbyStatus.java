package commons;

import java.util.List;
import java.util.Objects;

public class SessionLobbyStatus {
    private List<Emoji> emojiList;
    private boolean started;
    private String gameAdmin;


    public SessionLobbyStatus() {
        //Object mapping purposes
    }

    public SessionLobbyStatus(List<Emoji> emojiList, boolean started, String gameAdmin) {
        this.emojiList = emojiList;
        this.started = started;
        this.gameAdmin = gameAdmin;
    }

    public List<Emoji> getEmojiList() {
        return emojiList;
    }

    public void setEmojiList(List<Emoji> emojiList) {
        this.emojiList = emojiList;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public String getGameAdmin() {
        return gameAdmin;
    }

    public void setGameAdmin(String gameAdmin) {
        this.gameAdmin = gameAdmin;
    }

    @Override
    public String toString() {
        return "SessionLobbyStatus{" +
                "emojiList=" + emojiList +
                ", started=" + started +
                ", gameAdmin='" + gameAdmin + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionLobbyStatus that = (SessionLobbyStatus) o;
        return started == that.started && Objects.equals(emojiList, that.emojiList) && Objects.equals(gameAdmin, that.gameAdmin);
    }
}
