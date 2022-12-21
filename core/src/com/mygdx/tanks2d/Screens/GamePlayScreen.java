package com.mygdx.tanks2d.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.tanks2d.Assets.AssetsManagerGame;
import com.mygdx.tanks2d.AudioEngine.AudioEngine;
import com.mygdx.tanks2d.CameraGame;
import com.mygdx.tanks2d.ClientNetWork.Heading_type;
import com.mygdx.tanks2d.ClientNetWork.ServiceClient;
import com.mygdx.tanks2d.InputProcessor.InputProcessorDesktop;
import com.mygdx.tanks2d.Locations.GameSpace;
import com.mygdx.tanks2d.Locations.MapsList;
import com.mygdx.tanks2d.MainGame;
import com.mygdx.tanks2d.ParticleEffect.ParticleCustum;
import com.mygdx.tanks2d.Screens.Controll.Controller;
import com.mygdx.tanks2d.Units.Bullets;
import com.mygdx.tanks2d.Units.Player;
import com.mygdx.tanks2d.Units.Tanks.Tank;
import com.mygdx.tanks2d.Units.Tanks.TanksOther;

import java.io.IOException;
import java.util.ConcurrentModificationException;


public class GamePlayScreen implements Screen {
    private MainGame mainGame;
    private CameraGame cameraGame;
    private SpriteBatch batch;
    private float timeInGame = 0; // время в игре
    private GameSpace gameSpace; // карта_ локация
    private AudioEngine audioEngine;// игравой движок
    private InputProcessorDesktop inputProcessorPC;
    private Controller controller;
    private Tank tank;
    public Vector2 pos;
    private Bullets bullets;
    public ParticleCustum pc;

    private int score_blue_command;
    private int score_red_command;
    private int live_blue_command;
    private int live_red_command;

    private TanksOther tanksOther;

    ShaderProgram shader;

    public GamePlayScreen(MainGame mainGame) {
        this.batch = new SpriteBatch();
        score_blue_command = 0;
        score_red_command = 0;
        live_blue_command = 0;
        live_red_command = 0;
        this.mainGame = mainGame;

        this.timeInGame = 0;
        this.gameSpace = new GameSpace(this, mainGame);
        //this.audioEngine = new AudioEngine(this);
        this.audioEngine = mainGame.audioEngine;
        this.tanksOther = new TanksOther(this);

        this.inputProcessorPC = new InputProcessorDesktop(this);
        Gdx.input.setInputProcessor(inputProcessorPC);
        this.pos = new Vector2(150, 150);
        this.cameraGame = new CameraGame(MainGame.WIDTH_SCREEN * 1.2f, MainGame.HEIGHT_SCREEN * 1.2f, gameSpace.getSizeLocationPixel(), gameSpace.HEIHT_LOCATION, gameSpace.WITH_LOCATION, mainGame.hb);
        this.cameraGame.jampCameraToPoint(pos.x, pos.y);
//        this.controller = new Controller(this, mainGame.getAssetManager().get("flatDark26.png", Texture.class), mainGame.getAssetManager().get("flatDark261.png", Texture.class));
        this.controller = new Controller(this);
        tank = new Tank(this);
        bullets = new Bullets(this);
        pc = new ParticleCustum(this, mainGame.getAMG().get("particle1.png", Texture.class), mainGame.getAMG().get("fire.png", Texture.class), mainGame.getAMG().get("iron.png", Texture.class), mainGame.getAMG().get("de.pack", TextureAtlas.class), mainGame.getAMG().get("garnd.png", Texture.class));


//        if(!MainGame.ANDROID){
//            inputProcessorPC = new InputProcessorDesktop(this);
//
//        }
        //getMainGame().getMainClient().getNetworkPacketStock().toSendMyNik();
        //get

//        ShaderProgram.pedantic = false;
//        shader = new ShaderProgram(Gdx.files.internal("shaders/default.vert"),
//                (Gdx.files.internal("shaders/default.frag")));
//        if (!shader.isCompiled()) {
//            System.err.println(shader.getLog());
//            System.exit(0);
//        }
      //  tanksOther.createOponent(tank.getPosition().x, tank.getPosition().y + 20, 55, 15);

//        batch.setShader(shader);


    }

    public GamePlayScreen() {

    }

    public ParticleCustum getPc() {
        return pc;
    }

    @Override
    public void show() {
        controller.getDirectionMovement().set(0, 0);
    }

    public AudioEngine getAudioEngine() {
        return audioEngine;
    }

    public void update() {
//        if(MathUtils.randomBoolean(.005f)){
//            System.out.println("goMenuForPause");
//            mainGame.goMenuForPause();
//        }

//        if(MathUtils.randomBoolean(.005f)) {
//            //mainGame.getGamePlayScreen().getController().addFrag();
//          //  System.out.println("+++");
//        }

        //mainGame.getMainClient().
        //mainGame.getMainClient().checkConnect(Heading_type.STATUS_GAME);

        // disconect_protection();
        mainGame.getMainClient().checkConnect(Heading_type.IN_GAME); // проверяет на коннект переподключется
        getMainGame().updateClien();

        // кинуть на сервер мои координаты
        //if (!getTank().isLive())


        //////////    mainGame.getMainClient().getNetworkPacketStock();

        //if(MathUtils.randomBoolean(.0005f)) pc.addPasricalExplosionDeath(getTank().getPosition().x,getTank().getPosition().y);

//        if(MathUtils.randomBoolean(.5f)) mainGame.getMainClient().getNetworkPacketStock().toSendMyTokken();
//        if(MathUtils.randomBoolean(.5f)) mainGame.getMainClient().getNetworkPacketStock().toSendMyNik();


        //System.out.println(controller.isVoiceButton());

        if (controller.isVoiceButton()) {
//            System.out.println("-------------");
//            System.out.println(mainGame.getMainClient().getClient());
//            System.out.println(mainGame.getMainClient().getVoiceChatClient());
            mainGame.getMainClient().getVoiceChatClient().sendVoice(mainGame.getMainClient().getClient(), Gdx.graphics.getDeltaTime());
        }


        if (controller.isChance()) {
            controller.setChance(false);
            if (tank.isLive()) {
                tank.getTr().changeTarget();
            } else {
                getCameraGame().createNewTargetDeathRhim(getTanksOther().getRandomPlayer());
            }
        }


        controller.setContollerOn(tank.isLive());

        timeInGame = timeInGame + Gdx.graphics.getDeltaTime(); // игрвовое время
        if (controller.isInTuchMove()) audioEngine.pleySoundOfTracks();
        else audioEngine.stopSoundOfTracks();
        pos.add(controller.getDirectionMovement().cpy().scl(Gdx.graphics.getDeltaTime() * 1.5f)); /// движение танка Главного
    }

    private void disconect_protection() {
        if (!mainGame.getMainClient().getClient().isConnected()) {
            if (MathUtils.randomBoolean(.005f)) {
//                try {
//                    tanksOther.deathAllPlayers();
//                  //  mainGame.getMainClient().getClient().reconnect(5000);
//                 //   reconectClienNewThred();
//                } catch (IOException e) {
//                    //     e.printStackTrace();
//                }
            }
            //mainGame.transitionScreenGameToMenu();
        }
    }

    public float getTimeInGame() {
        return timeInGame;
    }

    public void playAnimation(Vector2 pos, Vector2 vel, int nom) { // добавляет анимацию выстрела
        mainGame.getGamePlayScreen().pc.addPasricalDeath_little(pos.x, pos.y, 4.7f);
        this.getBullets().addBullet(pos, vel, nom);
        //System.out.println("playAnimation");

        mainGame.getGamePlayScreen().getAudioEngine().pleySoundKickStick(cameraGame.getCamera().position.x, cameraGame.getCamera().position.y, pos.x, pos.y);
    }

    public void playExplosion(Vector2 pos, Vector2 vel) { // добавляет анимацию взрыва
        mainGame.getGamePlayScreen().pc.addPasricalDeath_little(pos.x, pos.y, 2.7f);
        mainGame.getGamePlayScreen().getAudioEngine().pleySoundKickStick(cameraGame.getCamera().position.x, cameraGame.getCamera().position.y, pos.x, pos.y);
    }

    @Override
    public void render(float delta) {

        update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

       // this.cameraGame.getCamera().update();
        this.batch.setProjectionMatrix(cameraGame.getCamera().combined);

        this.cameraGame.getCamera().update();
        this.batch.begin();
        try {
            //  System.out.println("rander");
            this.cameraGame.integrationCamera();
            this.gameSpace.renderSpace((OrthographicCamera) cameraGame.getCamera());                //рендер пространство
            this.cameraGame.getCamera().update();

            this.getGameSpace().getRadspurens().randerCrater(batch);// следы от Crater
            this.getGameSpace().getRadspurens().randerRadspurens(batch);// следы от танка

            this.pc.randerGarbage(batch);

            //this.tanksOther.updateOtherTank(mainGame.getMainClient().isOnLine()); /// обновление других танков с сервреа (позиция) или локальной зоны
            this.tanksOther.randerOtherTanks(getBatch());      // визуализация других танков
            this.tank.renderTank(controller.getDirectionMovement(), controller.isInTuchMove());     //рендер основного танка

/////////////стрельба

            this.bullets.randerBullets(delta);
            this.pc.render(getBatch());
            this.startFlashForMainTank();                                                  // вспышка из дула и вспышка вокруг танка

/////////////
//        Vector2 smooke = tank.getPosition().cpy().sub(tank.getDirection_tower().cpy().nor().scl(-20 ));
//        batch.setColor(1,1,1,.3f);
//        getBatch().draw(mainGame.getAssetManager().get("badlogic1B.png",Texture.class),smooke.x,smooke.y,45,45);
//        batch.setColor(1,1,1,1);
            //////////////////////////////////////

            this.batch.end();
            //  this.getGameSpace().getLighting().renderLights(cameraGame.getCamera()); временно
            this.controller.draw(this.getBatch());
            this.getBatch().setColor(1, 1, 1, 1);

        } catch (ConcurrentModificationException e) {
            this.batch.end();
        }

        mainGame.startPauseScreen();
       //  if(MathUtils.randomBoolean(.005f)) MainGame.setFlagChangeScreen((byte) MainGame.STATUS_GAME_PAUSE);
    }

    public AssetsManagerGame getAMG() {
        return mainGame.getAMG();
    }

    public TanksOther getTanksOther() {
        return tanksOther;
    }


    public Controller getController() {
        return controller;
    }

    @Override
    public void resize(int width, int height) {
        cameraGame.getViewport().update(width, height);
    }

    @Override
    public void pause() {

        getMainGame().getMainClient().getClient().stop();
        getMainGame().goMenuForPause();
       // AudioEngine.Mute(false);
    }

    public Tank getTank() {
        return tank;
    }

    @Override
    public void resume() {

    }

    public GameSpace getGameSpace() {
        return gameSpace;
    }


    @Override
    public void hide() {
        // getMainGame().getMainClient().getClient().stop();
        // getMainGame().goMenuForPause();
    //    AudioEngine.Mute(false);
    }

    public int getLive_blue_command() {
        return live_blue_command;
    }

    public void setLive_blue_command(int live_blue_command) {
        this.live_blue_command = live_blue_command;
    }

    public int getLive_red_command() {
        return live_red_command;
    }

    public void setLive_red_command(int live_red_command) {
        this.live_red_command = live_red_command;
    }

    public Bullets getBullets() {
        return bullets;
    }

    public CameraGame getCameraGame() {
        return cameraGame;
    }

    public void setCameraGame(CameraGame cameraGame) {
        this.cameraGame = cameraGame;
    }

    @Override
    public void dispose() {
        batch.dispose();
    }


    public void setPos(float x, float y) {
        this.pos.set(pos.x + x, pos.y + y);
    }

    public MainGame getMainGame() {
        return mainGame;
    }

    public void setMainGame(MainGame mainGame) {
        this.mainGame = mainGame;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    private void startFlashForMainTank() {
        Vector2 smooke = tank.getPosition().cpy().sub(tank.getDirection_tower().cpy().nor().scl(-30));
        if (controller.isAttackButon()) {
            if (!tank.redyToAttack()) return;
            //  System.out.println("startFlashForMainTank !! Generator new Buulet");
            this.getMainGame().getMainClient().getNetworkPacketStock().toSendMyShot(smooke.x, smooke.y, tank.getDirection_tower().angleDeg());
        }
    }

    public void sendMyCommand(int command) {
        this.getMainGame().getMainClient().getNetworkPacketStock().toSendMyCommand(command);
    }


//   // public AssetManager getAssetsManagerGame() {
//        return this.mainGame.getAssetManager();
//    }

    public int getScore_blue_command() {
        return score_blue_command;
    }

    public int getScore_red_command() {
        return score_red_command;
    }

    public void setScore_red_command(int score_red_command) {
        this.score_red_command = score_red_command;
    }

    public void setScore_blue_command(int score_blue_command) {
        this.score_blue_command = score_blue_command;
    }


}
