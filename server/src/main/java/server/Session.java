package server;

import commons.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Session {

    private static final int playerLimit = 20; //To be determined
    private List<Player> playerList;
    private boolean started;
    private Player gameAdmin;
    private boolean gameType; //0 for singleplayer || 1 for multiplayer

    public Session(boolean gameType) {
        this.playerList  = new ArrayList<Player>();
        this.started     = false;
        this.gameAdmin   = null;
        this.gameType    = gameType;
    }

    /**
     *
     * @param p - Player to be added
     * @return boolean value whether addition of player is possible
     */
    public boolean isAvailable(Player p) {
        //Game is full OR player is already in game OR has started
        if(this.playerList.size() >= Session.playerLimit ||
                this.playerList.contains(p) || this.started) return false;

        //Attempting to add another player to singleplayer
        if(!this.gameType && this.playerList.size() == 1) return false;

        return true;
    }

    /**
     * @param p - Player to be added to game
     * @return boolean value whether operation of addition was successful
     */
    public boolean addPlayer(Player p) {
        if(!this.isAvailable(p)) return false;

        //Setting up gameAdmin
        if(this.playerList.size() == 0) {
            this.gameAdmin = p;
        }

        this.playerList.add(p);
        return true;
    }

    /**
     *
     * @param p - Player to be removed from game
     * @return Boolean value depending on whether deletion operation was successful
     */
    public boolean removePlayer(Player p) {
        if(this.playerList.size() == 0 || !playerList.contains(p)) return false;

        playerList.remove(p); //Remove player from playerList

        if(gameAdmin.equals(p)) { //If player is the game's admin
            if(playerList.size() == 0) gameAdmin = null;
            else gameAdmin = playerList.get(0);
        }

        return true;
    }

    /**
     *
     * @param x - Player to find for
     * @return Boolean value determining whether player is inside this session
     */
    public boolean isPlayerInSession(Player x) {
        if(x == null) return false;

        return this.playerList.contains(x);
    }

    public int getPlayerNum() {
        return this.playerList.size();
    }

    public void startGame() {
        this.started = true;
    }

    public void endGame() {
        this.started = false;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public boolean isStarted() {
        return started;
    }

    public Player getGameAdmin() {
        return gameAdmin;
    }

    public boolean isGameType() {
        return gameType;
    }

    @Override
    public String toString() {
        return "Session{" +
                "playerList=" + playerList +
                ", started=" + started +
                ", gameAdmin=" + gameAdmin +
                ", gameType=" + gameType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return started == session.started && gameType == session.gameType && Objects.equals(playerList, session.playerList) && Objects.equals(gameAdmin, session.gameAdmin);
    }
}
