package com.mygdx.tanks2d.ClientNetWork;

import java.util.HashMap;

public class Heading_type {
    public static final HashMap<Integer, String> domen = new HashMap<>();

    public static final int MY_TOKKEN = 1;
    public static final int MY_NIK = 2;

    public static final int STATUS_GAME = 3;
    public static final int MY_SHOT = 4;

    public static final int BUTTON_STARTGAME = 5;
    public static final int EXIT_IN_MATH_GAME_PLAYER = 6;

    public static final int PARAMETERS_PLAYER = 7;

    public static final int PARAMETERS_COMMAND = 8;
    public static final int PARAMETERS_MATH = 15;  // счета команд
    public static final int PARAMETERS_MAP = 16;  // счета команд

    public static final int MY_PARAMETERS = 9;
    public static final int MY_COMMAND = 14;

    public static final int HP_PLAYER = 10;

    public static final int DISCONECT_PLAYER = 11; //

    public static final int SHELL_RUPTURE = 12; // удаление пули - уничтожение ее

    public static final int BLUE_COMMAND = 44;
    public static final int RED_COMMAND = 45;

    public static final int LIFE_TANK = 47;
    public static final int DEATH_TANK = 48;

    public static final int RESPOWN_TANK_PLAYER = 49; // дать команду на респаун - в пакет нужно поместить номер команды ))

    ////
    public static final int IN_MENU = -1;
    public static final int IN_GAME = -2;

    public static final float SHOT_LIFETIME = 1.5f;
    public static final String DEFULT_NAME = "no_name3812";

    public static final int PAUSE_GAME = 1;
    public static final int PLAY_GAME = 2;

    public static final int CHANGE_THE_SCREEN = 49;


    static public String getDomenTip(int tip) {
        domen.put(1, "MY_TOKKEN");

        domen.put(2, "MY_NIK");
        domen.put(3, "STATUS_GAME");
        domen.put(4, "MY_SHOT");
        domen.put(5, "BUTTON_STARTGAME");
        domen.put(6, "EXIT_IN_MATH_GAME_PLAYER");
        domen.put(7, "PARAMETERS_PLAYER");
        domen.put(8, "PARAMETERS_COMMAND");
        domen.put(15, "PARAMETERS_MATH");
        domen.put(16, "PARAMETERS_MAP");
        domen.put(9, "MY_PARAMETERS");
        domen.put(14, "MY_COMMAND");
        domen.put(10, "HP_PLAYER");
        domen.put(12, "SHELL_RUPTURE");
        domen.put(44, "BLUE_COMMAND");
        domen.put(45, "RED_COMMAND");
        domen.put(11, "DISCONECT_PLAYER");
        domen.put(47, "LIFE_TANK");
        domen.put(48, "DEATH_TANK");

        domen.put(49, "RESPOWN_TANK_PLAYER");
        domen.put(11, "DISCONECT_PLAYER");

        domen.put(49, "CHANGE_THE_SCREEN");

        String r = domen.get(tip);
        if (r == null) return String.valueOf(tip);
        else return r;


    }
}
