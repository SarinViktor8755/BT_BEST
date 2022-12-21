package com.mygdx.tanks2d.ClientNetWork;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.tanks2d.AudioEngine.AudioEngine;
import com.mygdx.tanks2d.Locations.GameSpace;
import com.mygdx.tanks2d.MainGame;
import com.mygdx.tanks2d.Units.Tanks.OpponentsTanks;
import com.mygdx.tanks2d.Units.Tanks.Tank;

public class RouterSM {

    MainGame mainGame;

    static Vector2 velocity;
    static Vector2 position;

    static Vector2 positionTemp;

    public static String map_math; /// карта в мате

    public RouterSM(MainGame mainGame) {
        velocity = new Vector2();
        position = new Vector2();
        positionTemp = new Vector2();
        this.velocity.set(45, 1);
        this.position.set(1, 1);

        this.mainGame = mainGame;
    }

    public void routeSM(Network.StockMessOut sm) throws NullPointerException {
         System.out.println("-->>> in :: " + sm);
        if (Heading_type.MY_SHOT == sm.tip) {
            try {
                //   System.out.println(mainGame.getGamePlayScreen() + "!!!!!!!!!!!!!!!!!!!!!");
                if (mainGame.getGamePlayScreen() == null) return;
                position.set(sm.p1, sm.p2);
                velocity.set(0, 400);
                velocity.setAngleDeg(sm.p3); /// навправление

                mainGame.getGamePlayScreen().playAnimation(position, velocity, (int) sm.p4);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return;
        }

        if (Heading_type.PARAMETERS_MATH == sm.tip) {
//            mainGame.getGamePlayScreen().setLive_red_command((int) sm.p1);
//            mainGame.getGamePlayScreen().setLive_score_blue((int) sm.p2);
            mainGame.getGamePlayScreen().getController().setLive_score_red((int) sm.p2);
            mainGame.getGamePlayScreen().getController().setLive_score_blue((int) sm.p1);

            System.out.println(sm.p1 + "            "+ sm.p2);

            mainGame.getGamePlayScreen().getController().setTime_in_game(sm.p3 / 1000);  // переделать на время матча
//            mainGame.getGamePlayScreen().setLive_red_command((int) sm.p4);
            return;
        }

        if (Heading_type.PARAMETERS_MAP == sm.tip) { // сервер прислал карту матча ))
            //    System.out.println("MAP_!!! " + sm.textM + "    pause_game ::" + sm.p1);
            // System.out.println("-------------@@@@@@@@@@");
            GameSpace.setMapDesetrt(sm.textM);
            RouterSM.map_math = sm.textM;
            mainGame.getGamePlayScreen().getGameSpace().loadMap();

            //mainMenuParametors(sm.p1);
            // MainGame.setFlagChangeScreen((byte) MainGame.STATUS_GAME_PAUSE);
            return;
        }

        if (Heading_type.CHANGE_THE_SCREEN == sm.tip) {
            //   System.out.println("=+++++++++++++  " + sm.p1);
            if (sm.p1 == Heading_type.PAUSE_GAME) MainGame.setFlagChangeScreen((byte) MainGame.STATUS_GAME_PAUSE);


             if(sm.p1 == Heading_type.PLAY_GAME) MainGame.setFlagChangeScreen((byte) MainGame.STATUS_GAME_GAMEPLAY);
        }


        if (Heading_type.RESPOWN_TANK_PLAYER == sm.tip) { // сервер прислал оманду на респун
//            System.out.println("RESPOWN_TANK_PLAYER!!! " + sm.p1);
//            System.out.println("RESPOWN_TANK_PLAYER!!! " + sm.p2);
//            System.out.println("RESPOWN_TANK_PLAYER!!! " + sm.p3);


            //mainGame.getGamePlayScreen().getTanksOther().clearListOponent();
            mainGame.getGamePlayScreen().getTanksOther().send_all_layer_live_100_hp();
            /// ока сделаем что бы клиент сам определял место респауна, если что - поменяем на то что бы сервер определял ,
            mainGame.getGamePlayScreen().getTank().setHp(100);
           // mainGame.getGamePlayScreen().getTank().respownTank((int) sm.p4);
            mainGame.getGamePlayScreen().getTank().respownTank();

            mainGame.getGamePlayScreen().getController().setTime_in_game(0);

            mainGame.getGamePlayScreen().getGameSpace().clear_map_particel();

            //mainGame.getGamePlayScreen().getPc().


            // mainGame.getGamePlayScreen().getTank().getPosition().set(sm.p1,sm.p2);


            return;
        }


        if (Heading_type.SHELL_RUPTURE == sm.tip) { // РАЗРЫВ СНАРЯДА
            try {
                Vector2 pp = new Vector2(sm.p1, sm.p2);

                // Vector2 v = mainGame.getGamePlayScreen().getBullets().getBullet((int) sm.p3).direction;
                mainGame.getGamePlayScreen().playExplosion(pp, velocity);
                //System.out.println(sm.p3 + "-------------");
                Vector2 v = mainGame.getGamePlayScreen().getBullets().removeBullet((int) sm.p3);
                v.rotateDeg(180);

                if (mainGame.getGamePlayScreen().getTank().isLive())
                    mainGame.getGamePlayScreen().getCameraGame().setTargetCamera(mainGame.getGamePlayScreen().getTanksOther().getTankForID((int) sm.p4));


                for (int i = 0; i < MathUtils.random(10, 30); i++) {
                    v.rotateDeg(MathUtils.random(-20, 20));
                    mainGame.getGamePlayScreen().getPc().addShares(sm.p1, sm.p2, v.x, v.y);
                }
            } catch (NullPointerException e) {
                //   e.printStackTrace();
                return;
            }
            return;
        }

        if (Heading_type.PARAMETERS_PLAYER == sm.tip) {
            try {
                int id = mainGame.getMainClient().getClient().getID();
                if (id == (int) sm.p1) {   // мой танк
                    //System.out.println(sm);
                    saveParametrsMyTank(sm);
                } else {   // чужие танки  - получают урон
                    // если игрока не будет создать нового
                    if(!mainGame.getGamePlayScreen().getTanksOther().getExists((int) sm.p1)) mainGame.getGamePlayScreen().getTanksOther().createOponent(-1000, -1000,(int) sm.p1,0);
                    OpponentsTanks ot = mainGame.getGamePlayScreen().getTanksOther().getTankForID((int) sm.p1);
                    //    System.out.println(opponentsTanks);
                    // opponentsTanks = new OpponentsTanks();


                    ot.hp = (int) sm.p3;
                    ot.command = (int) sm.p2;
                    ot.setNikPlayer(sm.textM);
                    if (!ot.isLive()) {
                        if (mainGame.getGamePlayScreen().getTimeInGame() < 1) return;

                    //    System.out.println("!!::  "+ot.hp + "  "+ ot.getNikPlayer());

                      //  System.out.println(sm + "   ---!!!!");
                       // if(sm.p1 == Tank.getMy_Command()) mainGame.getGamePlayScreen().getController().addFrag();
                        mainGame.getGamePlayScreen().getPc().addAnimationDeath(ot.getPosition().x, ot.getPosition().y);


//                        System.out.println(sm + " !!!!!!!!!!!!!!!!!!!!!!!");

                    }
                }

            } catch (NullPointerException e) {
                      e.printStackTrace();
                //  System.out.println("1");
//                Tank myTank = mainGame.getGamePlayScreen().getTank();
//                myTank.setHp((int) sm.p3);
//                myTank.setCommand((int) sm.p2);
                // System.out.println(sm.p1);
            }
            return;
        }

        if (Heading_type.DISCONECT_PLAYER == sm.tip) {
            System.out.println("del  DISCONECT_PLAYER" + ((int) sm.p1) + "       !!!!!!!!!!!! ПРОВЕРИТЬ ");
            mainGame.getGamePlayScreen().getTanksOther().getTankForID((int) sm.p1).setLive(-1000);
            mainGame.getGamePlayScreen().getTanksOther().getTankForID((int) sm.p1).getPosition().set(-10_000, -10_000);
            mainGame.getGamePlayScreen().getTanksOther().delPlayer((int) sm.p1);


            // mainGame.getGamePlayScreen().getTanksOther().delPlayer((int)sm.p1);
            return;
        }

    }

    private void mainMenuParametors(float p) {
        System.out.println("--@@@@------   " + p);
        // if(mainGame.isMainMenuScreen()) return;
        if (p == Heading_type.PAUSE_GAME) {
            MainGame.setFlagChangeScreen((byte) MainGame.STATUS_GAME_PAUSE);
            MainGame.status = MainGame.STATUS_GAME_PAUSE;
        } else {
            mainGame.startPauseScreen();
            mainGame.goGameForPause();
            MainGame.setFlagChangeScreen((byte) MainGame.STATUS_GAME_MENU);
        }

    }


    private void saveParametrsMyTank(Network.StockMessOut sm) {
        float stHp = mainGame.getGamePlayScreen().getTank().getHp() - sm.p3;
        if(stHp > 0) AudioEngine.Vibration(stHp * 4);
        mainGame.getGamePlayScreen().getTank().setHp((int) sm.p3);
        //mainGame.getGamePlayScreen().getTank().set((int) sm.p4);
        if (!mainGame.getGamePlayScreen().getTank().isLive()) {
            mainGame.getGamePlayScreen().getPc().addAnimationDeath(mainGame.getGamePlayScreen().getTank().getPosition().x, mainGame.getGamePlayScreen().getTank().getPosition().y);
        }
    }
}
