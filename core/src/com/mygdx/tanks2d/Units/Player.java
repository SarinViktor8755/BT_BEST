package com.mygdx.tanks2d.Units;

public class Player { /// все характеристики играков
    static final int STATUS_MENU = 1;
    static final int STATUS_IN_GAME = 2;

    static final int PLAYER_LIFE = 3;
    static final int PLAYER_DEATH = 4;

    // общие
    int status = STATUS_MENU;
    // игровые
    float xp, yp, r, rotTower; // коодинаты
    int hp, frags, death; //ХП

    String UserNick;
    int idUser_Connection;

    public static int getStatusMenu() {
        return STATUS_MENU;
    }

    public static int getStatusInGame() {
        return STATUS_IN_GAME;
    }

    public static int getPlayerLife() {
        return PLAYER_LIFE;
    }

    public static int getPlayerDeath() {
        return PLAYER_DEATH;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getXp() {
        return xp;
    }

    public void setXp(float xp) {
        this.xp = xp;
    }

    public float getYp() {
        return yp;
    }

    public void setYp(float yp) {
        this.yp = yp;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public float getRotTower() {
        return rotTower;
    }

    public void setRotTower(float rotTower) {
        this.rotTower = rotTower;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getFrags() {
        return frags;
    }

    public void setFrags(int frags) {
        this.frags = frags;
    }

    public int getDeath() {
        return death;
    }

    public void setDeath(int death) {
        this.death = death;
    }

    public String getUserNick() {
        return UserNick;
    }

    public void setUserNick(String userNick) {
        UserNick = userNick;
    }

    public int getIdUser_Connection() {
        return idUser_Connection;
    }

    public void setIdUser_Connection(int idUser_Connection) {
        this.idUser_Connection = idUser_Connection;
    }
}
