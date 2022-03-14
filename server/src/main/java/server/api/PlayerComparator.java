package server.api;

import commons.Player;

import java.util.Comparator;

/**
 * Used to sort players in descending order
 */
public class PlayerComparator implements Comparator<Player> {
    @Override
    public int compare(Player o1, Player o2) {
        if(o1.getScore()>o2.getScore())
            return -1;
        else if(o1.getScore()<o2.getScore())
            return 1;
        else
            return 0;
    }
}
