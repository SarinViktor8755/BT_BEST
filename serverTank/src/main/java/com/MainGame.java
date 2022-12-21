package main.java.com;


import static com.mygdx.tanks2d.ClientNetWork.Network.register;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.tanks2d.ClientNetWork.Network;
import com.mygdx.tanks2d.Locations.MapsList;

import main.java.com.MatchOrganization.IndexMath;
import main.java.com.Units.Bullet.IndexBullets;
import main.java.com.Units.ListPlayer.StatisticMath;
import main.java.com.Units.SpaceMap.IndexMap;

public class MainGame {
    GameServer gameServer;
    IndexBullets bullets;
    IndexMap mapSpace;
    IndexMath indexMath;
    // IndexBot bot;
    private Thread thred25;
    private Thread thred50;
    private Thread thred600;

    private static boolean thred25_Live;
    private static boolean thred50_Live;


    private static final  float PAUSE_TIME = 15;
    private static float pause_math; // пауза матч - меняется от 15 до - бесконечности интервал (15 - 0)


    ///public boolean pause_game;


    public final long timer_tread_50 = 20; //ms поток таймер циклов , рассылвает координаты ботов ))
    public final long timer_tread_25 = 15; // таймер поведения ботов - 25

    public static int targetPlayer = 2;

    public static boolean isPause() {
        if (pause_math > 0) return true;
        return false;
    }




    public void check_pause_game(){
        /// System.out.println(" !indexMath.isPause()  "+ !indexMath.isPause()+ "   " + pause_math);
        if(!indexMath.isPause()) return;
        // gameServer.send_Chang_screen(true,PAUSE_TIME);

        pause_math = PAUSE_TIME;
        System.out.println("startPauseTimer");
        indexMath.setPause(false);


        // что то ещу нжно для рестарта матча
       // indexMath.set() = 0;


    }



    public MainGame(GameServer gameServer, int targetPlayer) {
        MainGame.pause_math = -1;

        MainGame.targetPlayer = targetPlayer;
        this.gameServer = gameServer;
        this.bullets = new IndexBullets(this.gameServer);
        this.mapSpace = new IndexMap(MapsList.getMapForServer()); // создание карты
        startSecondaryThread_50();
        startSecondaryThread_25();
        startSecondaryThread_600();
        //    pause_game = false;
        indexMath = new IndexMath();


    }


    public IndexMath getIndexMath() {
        return indexMath;
    }

    public float getTimeMath() {
        return getIndexMath().getTimeMath();
    }

    public IndexBullets getBullets() {
        return bullets;
    }

    private void startSecondaryThread_50() { // выполняется каждые 50 мс
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        thred50_Live = true;
                        //    if(MathUtils.randomBoolean(.05f)) System.out.println(":::: 50");
                        //  System.out.println(":::: 50");
                        if (gameServer.isServerLivePlayer()) Thread.sleep(timer_tread_50);
                        else Thread.sleep(450);
                        // System.out.println("50");

                        StatisticMath.key_recalculate_statistics = true;


                        //      System.out.println();

                        //StatisticMath.printSttisticMath();

                        // if(MathUtils.randomBoolean(.05f)) System.out.println(gameServer.lp.getPlayers());

//                        поток 50 можно остоновить при отсутвии игрков
//                                нужно будет обнулить игру результаты

                        //           System.out.println("is_end_math : " + is_end_math());
                        //   gameServer.lp.counting_games();

//                        if(MathUtils.randomBoolean(.001f)){
//                           GameServer.break_in_the_game = Service.invertBoolean(GameServer.break_in_the_game);
//                            gameServer.send_Chang_screen(GameServer.break_in_the_game);
//
//                            System.out.println(">>>>>>>>>_____________>>>>>>>>>");
/////////////////////////////////////////////////
//                            mapSpace = new IndexMap(MapsList.getMapForServer()); // создание карты
//                            gameServer.send_MAP_PARAMETOR();
/////////////////////////////////////////////////
//                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public IndexMap getMapSpace() {
        return mapSpace;
    }

    public void setMapSpace(IndexMap mapSpace) {
        this.mapSpace = mapSpace;
    }

    private void startSecondaryThread_25() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        thred25_Live = true;

                        if(MathUtils.randomBoolean(.005f)) System.out.println(":::: 25");

                        /// сон потока
                        if (gameServer.isServerLivePlayer()) Thread.sleep(timer_tread_25);
                        else Thread.sleep(timer_tread_50);

                       // System.out.println("--------------");

                        //  System.out.println("25");

                        long deltaTime = GameServer.getDeltaTime();
                        indexMath.updateMath(deltaTime, gameServer.lp,false); // время матча
                        pause_math-=(deltaTime * .001);
                        bullets.updateBulets(deltaTime);
                        gameServer.indexBot.updaeteBot((float) (deltaTime * .001));
                        check_pause_game();
                        //      gameServer.lp.respaunPlayer();

                        // gameServer.lp.re


                        // System.out.println("---");

//     не останавливать поток все функции должны быть конечными )))


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void startSecondaryThread_600() { // выполняется каждые 50 мс
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        //      System.out.println(":::: 600");
                        Thread.sleep(600);
                        // System.out.println("600");
                        System.out.println("50LIVE " + thred50_Live + "   " + "25LIVE " + thred25_Live);

                        thred25_Live = false;
                        thred50_Live = false;

                        gameServer.indexBot.updateCountBot(gameServer.countLivePlayer(), targetPlayer); // контроль количество ботов
                        gameServer.lp.getStatisticMath().counting_p(); /// пересчет игры

                        gameServer.server.getUpdateThread();


                        ///********   gameServer.lp.print_list_player();

                        //  if(MathUtils.randomBoolean(0.5f))gameServer.indexBot.clearAllBot();

                        //  System.out.println("Tokkens :: " + gameServer.lp.getPlayersTokken());
                        //   gameServer.server.

                        //   Log.set(LEVEL_INFO);
//                        if(MathUtils.randomBoolean(.005f)){
//                            gameServer.server.close();
//                            gameServer.server.start();
//                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    ///если конец матча

}
