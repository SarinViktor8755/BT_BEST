package com.mygdx.tanks2d.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.tanks2d.AudioEngine.AudioEngine;
import com.mygdx.tanks2d.ClientNetWork.Heading_type;
import com.mygdx.tanks2d.ClientNetWork.MainClient;
import com.mygdx.tanks2d.ClientNetWork.RouterSM;
import com.mygdx.tanks2d.Locations.GameSpace;
import com.mygdx.tanks2d.MainGame;
import com.mygdx.tanks2d.Units.NikName;
import com.mygdx.tanks2d.Units.Tanks.Tank;

import java.io.IOException;


public class MenuScreen implements Screen {
    private MainGame mainGame;

    private SpriteBatch batch;
    private StretchViewport viewport;
    private OrthographicCamera camera;
    private MainClient mainClient;

    private Texture wallpaper;
    private Texture wallpaper1;
    private Texture logo;
    private Texture disconnect;

    private float timeInScreen;
    private float timerStartGame; // переменная для анимации

    private boolean startgameMP; // флаг старта мультиплеерной игры
    private boolean startgameSP; // флаг старта диночной игры

    public Label statusConnetct;
//    public Label singelGame;

    //////////////
    private Stage stageMenu;
    private Skin skinMenu;

    TextButton textButton;
    //  TextButton singelGame;

    private boolean button_start_click;


    FloatArray dummyArray = new FloatArray();
    String limit = "";


    public MenuScreen(final MainGame mainGame) {
        mainGame.getAMG().loadedAseets();
        mainGame.audioEngine = new AudioEngine(mainGame);

        button_start_click = false;

        this.mainGame = mainGame;
        timeInScreen = 0;
        timerStartGame = 0;
        startgameMP = false;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(MainGame.WIDTH_SCREEN, MainGame.HEIGHT_SCREEN, camera);

        viewport.apply();

       // viewport.apply(true);

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        mainClient = mainGame.getMainClient();
        stageMenu = new Stage(viewport);

        wallpaper = mainGame.getAMG().get("menuAsset/wallpaper.png", Texture.class);
        wallpaper1 = mainGame.getAMG().get("menuAsset/wallpaper1.png", Texture.class);
        logo = mainGame.getAMG().get("menuAsset/logo.png", Texture.class);
        disconnect = mainGame.getAMG().get("menuAsset/disconct.png", Texture.class);

        stageMenu = new Stage(viewport);

        skinMenu = mainGame.getAMG().get("skin/uiskin.json");

        final TextField textField = new TextField(limit, skinMenu);

        textField.setMaxLength(20);
        textField.setPosition(20, 250);
        textField.setText(NikName.getNikName());

        statusConnetct = new Label("", skinMenu);
        statusConnetct.setPosition(350, 170);
        // statusConnetct.setColor(Color.DARK_GRAY);
        statusConnetct.setFontScale(.6f);

//        singelGame = new Label("single player",skinMenu);
//        singelGame.setPosition(340,170);
//        singelGame.setColor(Color.DARK_GRAY);
//        singelGame.setFontScale(.6f);

        //  textField.setFillParent(true);


        //    System.out.println(viewport.getRightGutterX());

        textButton = new TextButton("Play Game", skinMenu);
        //  singelGame = new TextButton("Singel game", skinMenu);
        ///System.out.println(textField.getText());

        textButton.setX(270);
        textButton.setY(80);
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mainGame.getMainClient().getNetworkPacketStock().toSendButtonStartClick();
//                if(!mainGame.getMainClient().isConnect()) return false; else {
//                    try {
//                        mainGame.getMainClient().getClient().reconnect();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }

                if (!button_start_click) {
                    mainGame.audioEngine.pley_pip();
                    if (!mainClient.getClient().isConnected()) {
                        try {
                            mainClient.getClient().reconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            return false;
                        }
                    }
                    NikName.setNikName(textField.getText());
                    MainGame.nik_name = textField.getText();
                    mainGame.audioEngine.rady_for_action();


                    //   mainClient.getNetworkPacketStock().toSendButtonStartClick();
                    button_start_click = true;

                    //mainGame.getMainClient().getNetworkPacketStock().toSendMyNik();

                }
                startgameMP = true;

                //System.out.println(textField.getText());
                if (!mainClient.getClient().isConnected()) {
                    try {
                        mainClient.getClient().reconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
              //  mainGame.assetsManagerGame.loadAllAsseGame();


                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!mainGame.getMainClient().getClient().isConnected()) return;

                //System.out.println("StartGameDown");
                if (!mainClient.getClient().isConnected()) return;
                if (!button_start_click) {
                    //  mainClient.getNetworkPacketStock().toSendButtonStartClick();
                    button_start_click = true;

                }
                if (!mainClient.getClient().isConnected()) return;
                startgameMP = true;


            }
        });

//        singelGame.addListener(new InputListener() {
//            @Override
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                System.out.println("touchDown");
//
//                return true;
//            }
//
//            @Override
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                System.out.println("touchUp");
//                startgameSP = true;
//                mainGame.assetsManagerGame.loadAllAsseGame();
//                NikName.setNikName(textField.getText());
//
//            }
//        });


//        singelGame.setX(350);
//        singelGame.setY(120);

        stageMenu.addActor(textButton);
        stageMenu.addActor(textField);
        //  stageMenu.addActor(singelGame);
        stageMenu.addActor(statusConnetct);

        Gdx.input.setInputProcessor(stageMenu);
        mainGame.audioEngine.playMusicPaseMenu();
    }

    @Override
    public void show() {
        mainGame.audioEngine.stopSoundOfTracks();
        mainGame.audioEngine.playMusicPaseMenu();
    }

    @Override
    public void render(float delta) {
        //    System.out.println(RouterSM.map_math);
//        System.out.println(viewport.getWorldHeight());
//        System.out.println(viewport.getScreenHeight());
//        System.out.println();
     //   mainGame.audioEngine.stopSoundOfTracks();

        check_screen_flag();
        upDateScreen();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.camera.update();
        this.batch.setProjectionMatrix(camera.combined);
        this.batch.begin();

//        System.out.println(timeInScreen);
//        System.out.println(viewport.getScreenX());
//        System.out.println(viewport.getScreenY());
//        System.out.println(camera.position);
//       // System.out.println(viewport.getScreenY());
//        System.out.println(0 + ((MathUtils.sin(timeInScreen) + 1) / 2) * 20);
//        System.out.println();


        batch.setColor(1 - timerStartGame, 1 - timerStartGame, 1 - timerStartGame, 1);


        //batch.draw(wallpaper1,0,0,camera.viewportWidth, camera.viewportHeight,1,2,(int)camera.viewportWidth,(int) camera.viewportHeight,false,false);
        //System.out.println((MathUtils.sin(timeInScreen) + 1)/2);

        batch.setColor(1 - timerStartGame, 1 - timerStartGame, 1, 1);
        batch.draw(wallpaper1, viewport.getScreenX(), viewport.getScreenY() + ((MathUtils.sin(timeInScreen) + 1) / 2) * 20);
        batch.setColor(1 - timerStartGame, 1 - timerStartGame, 1, 1);

        batch.draw(wallpaper, viewport.getScreenX(), viewport.getScreenY() - ((Interpolation.bounce.apply((MathUtils.sin(timeInScreen) + 1) / 2) * 10)));
        batch.draw(logo, viewport.getScreenX(), viewport.getScreenY() + 14 + ((MathUtils.cos(timeInScreen * 3) + 1) / 2) * 20);

        if (!mainClient.getClient().isConnected()) {
            //batch.setColor(1 - timerStartGame, 1 - timerStartGame, 1,(MathUtils.sin(timeInScreen) +.5f));
            batch.draw(disconnect, viewport.getScreenX(), viewport.getScreenY() + ((MathUtils.sin(timeInScreen) + 1) / 2) * 20, 50, 50);
        }
        this.batch.end();
        stageMenu.draw();


    }

    private void check_screen_flag() {
        byte screen = MainGame.getFlagChangeScreen();
        if(screen == Heading_type.PAUSE_GAME) mainGame.goMenuForPause();

    }

    private void upDateScreen() {
        mainClient.checkConnect(Heading_type.IN_MENU); // проверяет на коннект переподключется

        // System.out.println("!!!" + mainClient.getClient().isConnected() + " ___ isOnLine" + mainGame.getMainClient().isOnLine());
        // mainGame.getMainClient().updateAlphaW();
        mainGame.updateClien();
        // statusConnetct.setText(mainClient.getClient().isConnected() +"");
        this.timeInScreen = Gdx.graphics.getDeltaTime() + this.timeInScreen;
        if (mainClient.getClient().isConnected()) {
            //   System.out.println(MathUtils.sin(Gdx.graphics.getDeltaTime()));
            // statusConnetct.setText("Ping :" + mainClient.getPing());
            statusConnetct.setColor(0, 0, 0, 1);
            textButton.setText("Play Game");

            MainGame.nik_name = NikName.getNikName();

            statusConnetct.setText("");
        } else {
            statusConnetct.setText("Server:disconnect");
            statusConnetct.setColor(Color.RED);
            // textButton.setText("Connect");
        }

        if (startgameMP || startgameSP) {
           // System.out.println(1-timerStartGame);
            mainGame.audioEngine.update_volme_pause(1-timerStartGame);
            timerStartGame += Gdx.graphics.getDeltaTime(); // задержка во воремени для анимации
        }

        if (timerStartGame > 1) {
            if (RouterSM.map_math == null) {
                mainGame.getMainClient().getNetworkPacketStock().toSendButtonStartClick();
                timerStartGame = 0;
                return;
            }

            mainGame.audioEngine.stopMusicPaseMenu();
            if (startgameMP) mainGame.startGameMPley();
            if (startgameSP) mainGame.startGameSPley();


        }
        // System.out.println(timerStartGame);
        // if (timeInScreen > 3.2f) mainGame.startGamePley();
        // kefMashtab = Interpolation.bounceOut.apply(MathUtils.sin(timeInScreen) + 1);
        //    System.out.println(kefMashtab);
        //System.out.println(mainClient.getClient().isConnected());
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height, true);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        try {
            batch.dispose();
            stageMenu.dispose();
            wallpaper.dispose();
            logo.dispose();
        }catch (IllegalArgumentException e){}
    }

    public void setStartgameMP(boolean startgame) {
        this.startgameMP = startgame;
    }

    public void setStartgameSP(boolean startgame) {
        this.startgameSP = startgame;
    }


}
