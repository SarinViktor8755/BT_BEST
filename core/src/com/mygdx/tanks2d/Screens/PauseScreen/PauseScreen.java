package com.mygdx.tanks2d.Screens.PauseScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.tanks2d.AudioEngine.AudioEngine;
import com.mygdx.tanks2d.ClientNetWork.MainClient;
import com.mygdx.tanks2d.MainGame;

public class PauseScreen implements Screen {
    MainGame mainGame;

    private SpriteBatch batch;

    private StretchViewport viewport;
    private OrthographicCamera camera;
    private MainClient mainClient;

    private AudioEngine audioEngine;// игравой движок

    /////////////////////////////////////
    private float timeInScreen;
    private float final_time;
    Texture f;
    Texture f_bw;
    Texture tb;


    public PauseScreen(MainGame mainGame) {
        System.out.println("PAUSE ");
        audioEngine = mainGame.audioEngine;
        this.batch = new SpriteBatch();
        this.mainGame = mainGame;

        viewport = new StretchViewport(MainGame.WIDTH_SCREEN, MainGame.HEIGHT_SCREEN, camera);
        //viewport.apply();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(MainGame.WIDTH_SCREEN / 2, MainGame.HEIGHT_SCREEN / 2, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        this.mainClient = mainGame.getMainClient();
        f = mainGame.getAMG().get("pause_screen/bg.png", Texture.class);
        f_bw = mainGame.getAMG().get("pause_screen/bg_bw.png", Texture.class);
        tb = mainGame.getAMG().get("treck_bar.png", Texture.class);
        timeInScreen = 15;
        final_time = 15;

        audioEngine.playMusicPaseMenu();
//
//        BitmapFont font = mainGame.getAssetManager().get("fonts/font.fnt", BitmapFont.class);
//        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
//        puseTextLabel = new Label("PAUSE_GAME",style);
//        font = new BitmapFont(); //or use alex answer to use custom font
        ///////////////////////////////
    }

    public PauseScreen(MainGame mainGame, float timeInScreen) {
        System.out.println("PAUSE ");
        audioEngine = mainGame.audioEngine;
        this.batch = new SpriteBatch();
        this.mainGame = mainGame;
        final_time = timeInScreen;

        viewport = new StretchViewport(MainGame.WIDTH_SCREEN, MainGame.HEIGHT_SCREEN, camera);
        //viewport.apply();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(MainGame.WIDTH_SCREEN / 2, MainGame.HEIGHT_SCREEN / 2, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        this.mainClient = mainGame.getMainClient();
        f = mainGame.getAMG().get("pause_screen/bg.png", Texture.class);
        f_bw = mainGame.getAMG().get("pause_screen/bg_bw.png", Texture.class);
        tb = mainGame.getAMG().get("treck_bar.png", Texture.class);
        this.timeInScreen = timeInScreen;
        audioEngine.playMusicPaseMenu();
//
//        BitmapFont font = mainGame.getAssetManager().get("fonts/font.fnt", BitmapFont.class);
//        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
//        puseTextLabel = new Label("PAUSE_GAME",style);
//        font = new BitmapFont(); //or use alex answer to use custom font
        ///////////////////////////////
    }



    @Override
    public void show() {
        mainGame.audioEngine.playMusicPaseMenu();

    }

    @Override
    public void render(float delta) {
        update();
        //mainGame.goGameForPause();

        //if(MathUtils.randomBoolean(.005f)) MainGame.setFlagChangeScreen((byte) MainGame.STATUS_GAME_GAMEPLAY);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.setColor(1, 1, 1, getAlpha());

      //  System.out.println(mainGame.getScreen());
        batch.draw(f, viewport.getScreenX(), viewport.getScreenY(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(1, 1, 1, 1 - getWith());
        batch.draw(f_bw, viewport.getScreenX(), viewport.getScreenY(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(1, 1, 1, 1);
        batch.draw(tb, viewport.getScreenX(), viewport.getScreenY(), Gdx.graphics.getWidth() * getWith(), Gdx.graphics.getHeight() / 25);
        batch.end();
       // System.out.println(timeInScreen);
        //if(timeInScreen < 0) mainGame.goGameForPause();
    }

    private void update() {
        this.timeInScreen -= Gdx.graphics.getDeltaTime();
        if(timeInScreen < 0 ) MainGame.setFlagChangeScreen((byte) MainGame.STATUS_GAME_GAMEPLAY);
        mainGame.goGameForPause();
    }

    private float getAlpha() {
        float result = 1;
        if (timeInScreen > final_time - 2) {
            result = Interpolation.exp10Out.apply(final_time - 1 - timeInScreen);
        } else if (timeInScreen < 1.3f) {
            result = Interpolation.exp10Out.apply(timeInScreen - 1);
            audioEngine.stopMusicPaseMenu();
        }

        return result;

    }

    private float getWith() {
        float result = final_time - timeInScreen;
        result = result / final_time;
        return Interpolation.fade.apply(result);

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
        batch.dispose();
        mainGame.audioEngine.stopMusicPaseMenu();

    }
}
