package com.mygdx.tanks2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.tanks2d.Assets.AssetsManagerGame;
import com.mygdx.tanks2d.AudioEngine.AudioEngine;
import com.mygdx.tanks2d.ClientNetWork.MainClient;
import com.mygdx.tanks2d.Screens.GamePlayScreen;
import com.mygdx.tanks2d.Screens.MenuScreen;
import com.mygdx.tanks2d.Screens.PauseScreen.PauseScreen;
import com.mygdx.tanks2d.adMod.AdAds;

public class MainGame extends Game {

    //  public AssetManager assetManager;
    public AudioEngine audioEngine;
    public AssetsManagerGame assetsManagerGame;
    private MainClient mainClient;
    private GamePlayScreen gamePlayScreen;
    private PauseScreen pauseScreen;

    private Screen mainMenu;


    private static byte flagChangeScreen = 0; // фоаг смены экрана - 0 не менять далее по показателям

    public static final int STATUS_GAME_MENU = 1;
    public static final int STATUS_GAME_GAMEPLAY = 2;
    public static final int STATUS_GAME_PAUSE = 3;


    public static String nik_name;


    ////////////////////////


    ////////////////////////
    //public AssetsManagerGame assetsManagerGame;

    static public boolean ANDROID;      // андроид
    private AdAds adMod;                // реклама
    public String tokken;

    static public final int WIDTH_SCREEN = 555; // шириа экрана Х
    static public final int HEIGHT_SCREEN = 315;  // высота экрана У

   // public float wb, wu; // шириа экрана Х
    public float hb, hu; // высота экрана У

    static public int status = STATUS_GAME_MENU;

    public MainGame(int tip) {


        mainClient = new MainClient(this);
        // assetManager = new AssetManager();
//Gdx.graphics.getWidth()
        assetsManagerGame = new AssetsManagerGame(new AssetManager());
        // assetsManagerGame.loadAllAssetMenu();
        assetsManagerGame.loadAllAsseGame();

        // audioEngine = new AudioEngine(this);

        if (tip == 1) ANDROID = false;
        else ANDROID = true;


    }


    @Override
    public void create() {

        count_the_edges_of_the_screen();
        //System.out.println("!!! " +  Gdx.graphics.getWidth());
        // tokken = NikName.getTokken();
        assetsManagerGame.loadAllAssetMenu();
        mainMenu = new MenuScreen(this);
        this.setScreen(mainMenu);





    }

    public void startGameMPley() {
        mainMenu.dispose();
        // getMainClient().setOnLine(true);
        //assetsManagerGame.loadAllAsseGame();
        this.gamePlayScreen = new GamePlayScreen(this);
        this.setScreen(this.gamePlayScreen);
    }

    public void startGameSPley() {
        mainMenu.dispose();
        //  getMainClient().setOnLine(false);
        //  assetsManagerGame.loadAllAsseGame();
        this.gamePlayScreen = new GamePlayScreen(this);
        this.setScreen(this.gamePlayScreen);
        MainGame.status = STATUS_GAME_GAMEPLAY;
        //     mainClient.getClient().dispose();
    }

    public void startPauseScreen() {
        startPauseScreen(15);
    }

    public void startPauseScreen(float time) {
        if (MainGame.flagChangeScreen != MainGame.STATUS_GAME_PAUSE) return;
        // assetsManagerGame.loadAllAsseGame();
        MainGame.flagChangeScreen = 0;
        this.screen.dispose();
        //  assetsManagerGame.loadAllAsseGame();
        // this.setScreen(null);
        // assetsManagerGame.loadAllAsseGame();
        this.pauseScreen = new PauseScreen(this, time);
        this.setScreen(pauseScreen);
        MainGame.status = STATUS_GAME_PAUSE;
    }

    public void goGameForPause() { // выход из паузы в игру
        //   System.out.println("goGameForPause");
        if (MainGame.flagChangeScreen != MainGame.STATUS_GAME_GAMEPLAY) return;
        MainGame.flagChangeScreen = 0;
        this.screen.dispose();


        // assetsManagerGame.loadAllAsseGame();
//        this.pauseScreen = new PauseScreen(this);
//        this.setScreen(pauseScreen);
//        MainGame.status = STATUS_GAME_PAUSE;

        this.gamePlayScreen = new GamePlayScreen(this);
        this.setScreen(this.gamePlayScreen);

    }

    public void goMenuForPause() { // выход из игры в меню
        this.screen.dispose();
        this.mainMenu.dispose();
        //this.gamePlayScreen.dispose();
        mainMenu = new MenuScreen(this);
        this.setScreen(mainMenu);

    }

    public AssetsManagerGame getAMG() {
        return assetsManagerGame;
    }

    public void transitionScreenGameToMenu() {
        this.mainMenu = new MenuScreen(this);
        this.setScreen(this.mainMenu);
        this.gamePlayScreen.dispose();
    }

    public static void setFlagChangeScreen(byte flagChangeScreen) { // флаг смены экрана
        MainGame.flagChangeScreen = flagChangeScreen;
    }

    public void switchingFromGameMenu() {
        //    playScreen.dispose();
        //assetsManagerGame.loadAllAsseGame();
        //this.gsp.dispose();
        mainMenu = new MenuScreen(this);
        this.setScreen(mainMenu);

    }

    public static boolean isANDROID() {
        return ANDROID;
    }

    public MainClient getMainClient() {
        return mainClient;
    }


//    public void getAllAssets() {
//        this.assetManager = new AssetManager();
//        this.assetManager.update();
//        this.assetManager.finishLoading();
//    }

    //  public AssetManager getAssetManager() {
//        return assetManager;
//    }

    public void updateClien() {
        this.getMainClient().upDateClient();
    }

    public GamePlayScreen getGamePlayScreen() {
        return gamePlayScreen;
    }

    public boolean isMainMenuScreen() {
        // System.out.println("---------  " + this.screen.equals(this.mainMenu));
        return this.screen.equals(this.mainMenu);
    }

    public boolean isPause() {
        //     System.out.println(this.screen);
//        System.out.println(flagChangeScreen + "  111111111");
        if (screen.getClass().equals(PauseScreen.class)) return true;
        //     if (MainGame.flagChangeScreen == MainGame.STATUS_GAME_PAUSE) return true;
        return false;
    }

    private void count_edges_screen() {

    }

    private static float get_percentage_of_proportions(float min, float max) { // на сколько процентов min больше max
        return (100 - (min / max * 100)) / 2;
    }

    private static float convert_percentage_pixels(float procent, boolean button) { // перевести процент в пиксели))
        return HEIGHT_SCREEN * (procent / 100);
        //		X = (600*12)/100
    }


    public void count_the_edges_of_the_screen(){
        float a = get_percentage_of_proportions(MainGame.WIDTH_SCREEN / (float) MainGame.HEIGHT_SCREEN, Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight());
        hb = convert_percentage_pixels(a, true);
        hu = HEIGHT_SCREEN - hb;
        if(a < 0) {hb  = 0; hu = HEIGHT_SCREEN;}
       //  System.out.println("!!!!!! a " + a + "  " + hb + "  " + hu);

    }

    public static byte getFlagChangeScreen() {
        return flagChangeScreen;
    }

    ///////////////////////////////////////////

//    public float getWb() {
//        return wb;
//    }
//
//    public float getWu() {
//        return wu;
//    }

    public float getHb() {
        return hb;
    }

    public float getHu() {
        return hu;
    }
}
