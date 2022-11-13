package com.mygdx.tanks2d.ClientNetWork;

import com.mygdx.tanks2d.MainGame;


public class Client_new_Thred extends Thread{
    MainGame mainGame;

    public Client_new_Thred(MainGame mainGame) {

        this.mainGame = mainGame;
    }
}
