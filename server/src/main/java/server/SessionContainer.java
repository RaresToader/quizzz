package server;

import java.util.ArrayList;
import java.util.List;

public class SessionContainer {
    private static List<Session> sessionList = new ArrayList<Session>();
    private static int maxSessions = 100;

    /**
     * @return number of all active players across all sessions
     */
    public static int getNumOfActivePlayers() {
        int playerSum = 0;
        for (Session x : SessionContainer.sessionList) {
            if (x == null) continue;

            playerSum += x.getPlayerNum();
        }

        return playerSum;
    }

    /**
     * @param gameType - type of game (0 for singleplayer / 1 for multiplayer)
     * @param author   - PLayer object of session initiator
     * @return Boolean value depending on whether session has been initiated
     */
    public static boolean createSession(boolean gameType, String author) {
        SessionContainer.cleanup(); //Check for inactive sessions

        Session newSess = new Session(gameType);
        newSess.addPlayer(author);

        if (sessionList.size() >= SessionContainer.maxSessions) return false;

        //Attempt to add new session in place of some deleted one
        for (int i = 0; i < sessionList.size(); i++) {
            if (sessionList.get(i) == null) {
                sessionList.set(i, newSess);
                return true;
            }
        }

        sessionList.add(newSess);
        return true;
    }

    /**
     * @param x - Player to look for
     * @return Number of session player is inside (-1 if in none session)
     */
    public static int findUserSession(String x) {
        if (x == null) return -1;

        for (int i = 0; i < sessionList.size(); i++) {
            if (sessionList.get(i) == null) continue;

            if (sessionList.get(i).isPlayerInSession(x)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Finds session for player
     *
     * @param x - Player to be added
     * @return ID of session or -1 if no session was found
     */
    public static int findAvailableSession(String x) {
        SessionContainer.cleanup(); //Perform cleanup
        if (SessionContainer.findUserSession(x) != -1) return -1;

        for (int i = 0; i < sessionList.size(); i++) {
            if (sessionList.get(i) == null) continue;

            Session curr = (Session) sessionList.get(i);
            if (!curr.isAvailable(x)) continue;

            curr.addPlayer(x);
            return i;
        }

        return -1;
    }

    /**
     * Deletes inactive sessions
     */
    public static void cleanup() {
        if (sessionList == null) return;

        for (int i = 0; i < sessionList.size(); i++) {
            if (sessionList.get(i).getPlayerNum() == 0) {
                sessionList.set(i, null);
            }
        }

        return;
    }

    public static Session getSession(int id) {
        if (id >= sessionList.size()) return null;

        return sessionList.get(id);
    }

    public static List<Session> getSessionList() {
        return sessionList;
    }


}
