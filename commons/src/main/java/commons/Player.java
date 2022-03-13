package commons;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Player {

    @Id
    private String username;

    private long score;

    private Player() {
        //for object mapper
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public Player(String username) {
        this.username = username;
        this.score = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return getScore() == player.getScore() && getUsername().equals(player.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getScore());
    }

    @Override
    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", score=" + score +
                '}';
    }

    public void incrementScore(long amount) {
        this.score += amount;
    }
}
