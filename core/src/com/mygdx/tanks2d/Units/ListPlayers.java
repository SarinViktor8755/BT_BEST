package com.mygdx.tanks2d.Units;

import java.util.concurrent.ConcurrentHashMap;

public class ListPlayers {
    ConcurrentHashMap<Integer,Player> players;

    public ListPlayers(ConcurrentHashMap<Integer, Player> players) {
        this.players = players;
    }

    public Player getPlayerForId(int id){
        return players.get(id);
    }

    public void clearList(){
        this.players.clear();
    }

    public ConcurrentHashMap<Integer, Player> getPlayers() {
        return players;
    }
}
