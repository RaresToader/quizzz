package server.api;

import commons.Player;

import java.util.Comparator;

/**
 * Used to sort players in descending order
 */
public class PlayerComparator implements Comparator<Player> {
    /**
     * No args constructor for testing purposes
     */
    public PlayerComparator() {
    }

    /**
     * @param o1 the first player
     * @param o2 the second player
     * @return an int, being 1 if o1.score is less then o2.score, 0 if they are equal
     * and -1 if o1.score>o2.score
     */
    @Override
    public int compare(Player o1, Player o2) {
        return Long.compare(o2.getScore(), o1.getScore());
    }
}
