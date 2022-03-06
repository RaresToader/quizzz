package server;

import commons.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SessionContainer {
    private List<Session> sessionList;
    private static int maxSessions = 100;

    public SessionContainer(List<Session> sessionList) {
        this.sessionList = new ArrayList<Session>();
    }

    /**
     * @return number of all active players across all sessions
     */
    public int getNumOfActivePlayers() {
        int playerSum = 0;
        for(Session x : sessionList) {
            if(x == null) continue;

            playerSum += x.getPlayerNum();
        }

        return playerSum;
    }

    /**
     *
     * @param gameType - type of game (0 for singleplayer / 1 for multiplayer)
     * @param author - PLayer object of session initiator
     * @return Boolean value depending on whether session has been initiated
     */
    public boolean createSession(boolean gameType, Player author) {
        this.cleanup(); //Check for inactive sessions

        Session newSess = new Session(gameType);
        newSess.addPlayer(author);

        if(sessionList.size() >= SessionContainer.maxSessions) return false;

        //Attempt to add new session in place of some deleted one
        for(int i=0; i<sessionList.size(); i++) {
            if(sessionList.get(i) == null) {
                sessionList.set(i,newSess);
                return true;
            }
        }

        sessionList.add(newSess);
        return true;
    }

    /**
     *
     * @param x - Player to look for
     * @return Number of session player is inside (-1 if in none session)
     */
    public int findUserSession(Player x) {
        if(x == null) return -1;

        for(int i=0; i<sessionList.size(); i++) {
            if(sessionList.get(i) == null) continue;

            if(sessionList.get(i).isPlayerInSession(x)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Finds session for player
     * @param x - Player to be added
     * @return ID of session or -1 if no session was found
     */
    public int findAvailableSession(Player x) {
        this.cleanup(); //Perform cleanup
        if(this.findUserSession(x) != -1) return -1;

        for(int i=0; i<sessionList.size(); i++) {
            if(sessionList.get(i) == null) continue;

            Session curr = (Session) this.sessionList.get(i);
            if(!curr.isAvailable(x)) continue;

            curr.addPlayer(x);
            return i;
        }

        return -1;
    }

    /**
     * Deletes inactive sessions
     */
    public void cleanup() {
        if(sessionList == null) return;

        for(int i=0; i<this.sessionList.size(); i++) {
            if(this.sessionList.get(i).getPlayerNum() == 0) {
                this.sessionList.set(i,null);
            }
        }

        return;
    }

    public Session getSession(int id) {
        if(id >= sessionList.size()) return null;

        return sessionList.get(id);
    }

    public List<Session> getSessionList() {
        return sessionList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionContainer that = (SessionContainer) o;
        return Objects.equals(sessionList, that.sessionList);
    }

}
