package com.mygdx.tanks2d.Screens.Controll;

import static com.mygdx.tanks2d.MainGame.WIDTH_SCREEN;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.tanks2d.ClientNetWork.Heading_type;
import com.mygdx.tanks2d.MainGame;
import com.mygdx.tanks2d.Screens.GamePlayScreen;
import com.mygdx.tanks2d.Units.Tanks.Tank;


/**
 * Created by brentaureli on 10/23/15.
 */
public class Controller {
    // Skin skinGame;

    private Banner banner;

    final private Viewport viewport;
    final private Stage stage;

    OrthographicCamera cam;
    OrthographicCamera cameraGUI;

    private boolean inTuchMove;
    private boolean attackButon;
    private boolean voiceButton;
    private boolean chance;


    final private Vector2 distance;
    private Vector2 temp_Point;
    private Image changingGoal;

    final Image stick;
    final Image attacButton;
    final Image voiceButtonImg;
    final private Image pointStick;

    private Label labelHP;
    /////////////
    private Label score_red;
    private Label score_blue;

    private Label live_score_red; //// количество живых
    private Label live_score_blue; /// количество живых


    private Label my_frag;

    private Label timer;
    /////////////
    private BitmapFont font;

    private GamePlayScreen gamePlayScreen;

    private Texture track;
    private Texture fith;
    private Texture feature;
    private Texture victory;
    private Texture failed;

    private static boolean finalAd = false; // люч вфинальных титров любявлени


    private boolean buttonChangingOpponent;

    private int frag = 0;

    private boolean contollerOn;

    private float fr; /// колчиество красных квадратиков
    private float fb; /// колчиество синих квадратиков
//
//
//    public float sw;
//    public float sh;


    Vector2 directionMovement; // Направление движения

    private float time_in_game; // время в игре

    public boolean isInTuchMove() {
        return inTuchMove;
    }

    public void setFr(float fr) {
        this.fr = fr;
    }

    public void setFb(float fb) {
        this.fb = fb;
    }

    public Controller(GamePlayScreen gsp) {

        // gsp.getAssetManager().get("de.pack", TextureAtlas.class);
        time_in_game = 0;
        this.gamePlayScreen = gsp;

        distance = new Vector2();
        inTuchMove = false;
        attackButon = false;
        voiceButton = false;
        chance = false;

        this.directionMovement = new Vector2(0, 0);
        cam = new OrthographicCamera();
//////////////////////////////////
        cam = new OrthographicCamera(WIDTH_SCREEN, MainGame.HEIGHT_SCREEN);
        cam.position.set(0, 0, 0);
        //  cam.setToOrtho(true); // flip y-axis
        cam.update();
        /////////
        viewport = new FillViewport(WIDTH_SCREEN, MainGame.HEIGHT_SCREEN, cam);
        contollerOn = false;


        stage = new Stage(viewport, gsp.getBatch());
        Gdx.input.setInputProcessor(stage);
        temp_Point = new Vector2(0, 0);
        buttonChangingOpponent = false;

        font = gsp.getAMG().get("fonts/font.fnt", BitmapFont.class);
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
        font.getData().setScale(.5f);
        font.getColor().set(.5f, .5f, .5f, 1);
//        System.out.println(font.getCapHeight() + "getCapHeight");

//        float procent = GamePlayScreen.get_percentage_of_proportions(MainGame.WIDTH_SCREEN/(float)MainGame.HEIGHT_SCREEN,Gdx.graphics.getWidth()/(float)Gdx.graphics.getHeight());
//
//        System.out.println(
//                procent
//
//        );
//        float a = ((float) MainGame.HEIGHT_SCREEN)/ 100 * procent ;
//
//
//        sh = a;
//        sw  = a;
//        System.out.println("!!!!!!!!!!!!!  " + sw);


        //System.out.println(Gdx.graphics.ge);

/////////////////
        stick = new Image(gsp.getAMG().get("button.pack", TextureAtlas.class).findRegion("b"));
        pointStick = new Image(gsp.getAMG().get("button.pack", TextureAtlas.class).findRegion("stick"));
////////////////
        // System.out.println(pointStick.getImageHeight()+ "  ==== ___ ");

        pointStick.setSize(90, 90);


        stick.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                inTuchMove = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                inTuchMove = false;
                resetPoint(pointStick);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                temp_Point.set(stick.getImageX() + stick.getImageWidth() / 2, stick.getImageY() + stick.getImageHeight() / 2);
                temp_Point.sub(x, y).scl(-1).clamp(0, 35);

                directionMovement.set(temp_Point);
                pointStick.setPosition(temp_Point.x, temp_Point.y);

            }
        });


        attacButton = new Image(gsp.getAMG().get("button.pack", TextureAtlas.class).findRegion("ba"));
        attacButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                attackButon = true;
                return true;
            }


            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                attackButon = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                attackButon = false;
            }
        });
////////////////////////////////////////// changingGoal
        changingGoal = new Image(gsp.getAMG().get("button.pack", TextureAtlas.class).findRegion("br"));
        changingGoal.setSize(90, 90);
        changingGoal.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                chance = true;
                setChance(true);
                //    System.out.println("changingGoal");
                return false;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                chance = false;

            }
        });

        voiceButtonImg = new Image((Texture) gsp.getMainGame().getAMG().get("microphone.png"));
        voiceButtonImg.setSize(90, 90);
        voiceButtonImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //   voiceButton = true;
                System.out.println();
                System.out.println(timer);
                System.out.println(time_in_game);


                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                voiceButton = false;

            }
        });

        /////////////////


        ////////////////////////////////////////// VOICE
//        voiceB = new Image((Texture) gsp.getMainGame().getAssetManager().get("microphone.png"));
//        voiceB.setSize(90, 90);
//        voiceB.addListener(new InputListener() {
//            @Override
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                voiceButton = true;
//                setVoiceButton(true);
//                // System.out.println("changingGoal");
//                return false;
//            }
//
//            @Override
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                setVoiceButton(false);
//                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//
//            }
//        });
        ////////////////////////////////////////////////////////////////////////////////////
        stick.setSize(90, 90);
//        stick.setPosition(0, 0);
//
        attacButton.setSize(55, 55);
        attacButton.setPosition(WIDTH_SCREEN - 90, 40);

        changingGoal.setSize(55, 55);
        changingGoal.setPosition(WIDTH_SCREEN - 90, 105);
//
        voiceButtonImg.setSize(55, 55);
        voiceButtonImg.setPosition(WIDTH_SCREEN - 90, 170);
//
        Group gropuButton = new Group();
        Group gropuStick = new Group();

        gropuStick.setPosition(35, 35);

        gropuStick.addActor(stick);
        gropuStick.addActor(pointStick);


        gropuButton.addActor(gropuStick);
        gropuButton.addActor(attacButton);
        gropuButton.addActor(voiceButtonImg);
        //   gropuButton.setPosition(50,50);

        gropuButton.addActor(changingGoal);


///////////////////
        // skinGame = gsp.getMainGame().assetManager.get("skin/metal-ui.json", Skin.class);
        labelHP = new Label("HP:", style);
        labelHP.setX(WIDTH_SCREEN / 2 - 180);
        labelHP.setY(gsp.getMainGame().hu - 25);
        stage.addActor(labelHP);

        ///////////////////////////////
        score_red = new Label("RED:", style);
        score_red.setColor(Color.RED);
        score_red.setX(1);
        score_red.setY(1 - 70);
        stage.addActor(score_red);
//      //  MathUtils.map(MainGame.HEIGHT_SCREEN,MainGame.WHIDE_SCREEN,Gdx.graphics.getHeight(),Gdx.graphics.getWidth(),MainGame.HEIGHT_SCREEN - 70);
        timer = new Label("", style);
        timer.setColor(Color.WHITE);
//        timer.setX(1);
//        timer.setY(gsp.getMainGame().hb);
        timer.setX(WIDTH_SCREEN / 2 - 35);
        timer.setY(gsp.getMainGame().hu - 25);

        stage.addActor(timer);
//
//
        my_frag = new Label("frags : " + frag, style);
        my_frag.setColor(Color.YELLOW);
        my_frag.setX(30);
        my_frag.setY(130);
        stage.addActor(my_frag);

        live_score_red = new Label("0", style);
        live_score_red.setColor(Color.RED);
        live_score_red.setX(WIDTH_SCREEN / 2 + 40);
        live_score_red.setY(gsp.getMainGame().hu - 35);
        stage.addActor(live_score_red);
/////////
        live_score_blue = new Label("0", style);
        live_score_blue.setColor(Color.BLUE);
        live_score_blue.setX(WIDTH_SCREEN / 2 - 60);
        live_score_blue.setY(gsp.getMainGame().hu - 35);
        stage.addActor(live_score_blue);


//////////////////////////////////
        //   stage.setDebugAll(true);
//        if(!MainGame.ANDROID)

        stage.addActor(gropuButton);

        resetPoint(stick);
        pointStick.setTouchable(Touchable.disabled);

        pointStick.setColor(1, 1, 1, .3f);

        stick.setColor(1, 1, 1, .3f);
        pointStick.setColor(1, 1, 1, .7f);

        attacButton.setColor(1, 1, 1, .3f);
        voiceButtonImg.setColor(1, 1, 1, .3f);

        changingGoal.setColor(1, 1, 1, .3f);

        // assets.put("fith.png", Texture.class);
        track = gamePlayScreen.getAMG().get("treck_bar.png", Texture.class);
        fith = gamePlayScreen.getAMG().get("Fith.png", Texture.class);
        failed = gamePlayScreen.getAMG().get("failed.png", Texture.class);
        victory = gamePlayScreen.getAMG().get("victory.png", Texture.class);

        feature = gamePlayScreen.getAMG().get("treck_bar1.png", Texture.class);


        banner = new Banner(gsp.getBatch(), gsp.getAudioEngine(), fith,victory,failed, track);
    }

    public boolean isButtonChangingOpponent() {
        return buttonChangingOpponent;
    }

    public boolean isChance() {
        return chance;
    }

    public void addFrag() {
        this.frag++;
        this.my_frag.setText("frags : " + frag);
    }

    public void setChance(boolean chance) {
        this.chance = chance;
    }

    private void resetPoint(Image stick) {
        stick.setPosition(
                stick.getImageX(),
                stick.getImageY()
        );

    }

    public void addBannerFeiath() {
        this.banner.addBaner(1);
    }

    public void addBannerWiner() {
        this.banner.addBaner(2);
    }

    public void addBannerLOUSER() {
        this.banner.addBaner(3);
    }

    public void randerGUI(SpriteBatch batch) {

        batch.begin();
        try {
            float n = WIDTH_SCREEN / 2 - 100;
            float tt = gamePlayScreen.getTank().getTime_Tackt();

            batch.setColor(1, 1, 1, 1);
            // fr = MathUtils.random(0,15);
            for (int i = 0; i < fr; i++) {
                batch.draw(track,
                        WIDTH_SCREEN / 2 + 60 + (i * 15), // ширина экрана
                        gamePlayScreen.getMainGame().hu - 35 // высота экрана
                        , 10, 10);
            }
            //batch.setColor(Color.BLUE);
            for (int i = 0; i < fb; i++) {
                batch.draw(feature,
                        WIDTH_SCREEN / 2 - 80 - (i * 15), // ширина экрана
                        gamePlayScreen.getMainGame().hu - 35 // высота экрана
                        , 10, 10);
            }

///////////////////////////////////////////////////////////////////

            if (tt < 1) {
                batch.setColor(1, 1, 1, 1);
                batch.draw(track,
                        n, // ширина экрана
                        gamePlayScreen.getMainGame().hu - 50 // высота экрана
                        , gamePlayScreen.getTank().getTime_Tackt() * 200, 6);
                batch.setColor(1, 1, 1, .3f);
                batch.draw(track,
                        n, // ширина экрана
                        gamePlayScreen.getMainGame().hu - 50 // высота экрана
                        , 200, 6);
                batch.setColor(1, 1, 1, 1);

            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (banner.isWorking()) banner.rander(batch);
            batch.end();
        }
//
//        live_score_red.setX(WIDTH_SCREEN / 2 + 40);
//        live_score_red.setY(gamePlayScreen.getMainGame().hu - 35);
/////////////////////////
    }

    public void draw(SpriteBatch batch) {

//        System.out.println(
//                MathUtils.map(Gdx.graphics.getHeight(),Gdx.graphics.getWidth(),MainGame.HIDE_SCREEN,MainGame.WHIDE_SCREEN,MainGame.HIDE_SCREEN - 70)
//
//        );
//        System.out.println(Gdx.graphics.getWidth()/(float)Gdx.graphics.getHeight());
//        System.out.println(MainGame.WHIDE_SCREEN/(float)MainGame.HIDE_SCREEN);
//        System.out.println(GamePlayScreen.get_percentage_of_proportions(MainGame.WHIDE_SCREEN/(float)MainGame.HIDE_SCREEN,Gdx.graphics.getWidth()/(float)Gdx.graphics.getHeight()));
//        //System.out.println(GamePlayScreen.get_percentage_of_proportions(110,100));
//        System.out.println("-------------------  " + (MainGame.WHIDE_SCREEN- sw));
//        System.out.println("-------------------  " + sw);
        this.update();

        stage.draw();
        //    System.out.println(this.inTuchMove);

        randerGUI(batch);


    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public Vector2 getDirectionMovement() {
        return directionMovement;
    }

    public boolean isAttackButon() {
        if (attackButon) {
            attackButon = false;
            return true;
        }
        return attackButon;
    }

    private void update() {

//        System.out.println();
//        System.out.println(cameraGUI.viewportHeight );
//        System.out.println(cameraGUI.viewportWidth );
//
//        System.out.println("Gdx.graphics.getWidth() ");
//        System.out.println(Gdx.graphics.getWidth() );
//        System.out.println(Gdx.graphics.getHeight());
//
//        System.out.println("Gdx.graphics.getDisplayMode().height");
//        System.out.println(Gdx.graphics.getDisplayMode().height );
//        System.out.println(Gdx.graphics.getDisplayMode().width );


        banner.update();

        // labelHP = MathUtils.random(5,50);
        //labelHP.setText("HP: " + hp);
        //System.out.println(buttonChangingOpponent);
        // changingGoal.setVisible(contollerOn);


        time_in_game += Gdx.graphics.getDeltaTime();
        // setBlueCommand(45 - (int) time_in_game);


        timer.setText(format_time((int) time_in_game));
        //     setBlueCommand((int) time_in_game);
        //    System.out.println((time_in_game) + "  --");


        pointStick.setVisible(contollerOn);
        attacButton.setVisible(contollerOn);
        stick.setVisible(contollerOn);


        if (buttonChangingOpponent) changingGoal.setColor(1, 1, 1, .3f);
        else changingGoal.setColor(1, 0, 0, .1f);
        if (!contollerOn) changingGoal.setColor(1, 1, 1, 1);

        updateCotrollerFinalAd(); // бновление финалльног ообьявления или титров
    }

    public void setContollerOn(boolean contollerOn) {
        this.contollerOn = contollerOn;
    }

    public void setButtonChangingOpponent(boolean buttonChangingOpponent) {
        this.buttonChangingOpponent = buttonChangingOpponent;
    }

    private String format_time(int time) {// орматировтаь время под часы игры
        int time_minus = 120 - time;
        int min = time_minus / 60 % 60,
                sec = time_minus / 1 % 60;
        return String.format("%02d:%02d", min, sec);

        //return result;

    }

    public float getTime_in_game() {
        return time_in_game;
    }

    public void setHPHeroTank(int hp) {
        this.labelHP.setText("HP: " + hp);
        if (hp < 30) labelHP.setColor(Color.FIREBRICK);
        else labelHP.setColor(Color.WHITE);
    }

    public void setBlueCommand(int score) {
        this.score_blue.setText(score);

        //if (hp < 30) labelHP.setColor(Color.FIREBRICK); else labelHP.setColor(Color.WHITE);
    }

    public void setTime_in_game(float time_in_game) {
        this.time_in_game = time_in_game;
    }

    public void setRedCommand(int score) {
        this.score_red.setText(score);
        // if (hp < 30) labelHP.setColor(Color.FIREBRICK); else labelHP.setColor(Color.WHITE);
    }

    public Label getLive_score_red() {
        return live_score_red;
    }


    public Label getLive_score_blue() {
        return live_score_blue;
    }

    public void setLive_score_red(int i) {
        this.live_score_red.setText(i);
    }

    public void setLive_score_blue(int i) {
        this.live_score_blue.setText(i);
    }

    public boolean isVoiceButton() {
        return voiceButton;
    }

    public void setVoiceButton(boolean voiceButton) {
        this.voiceButton = voiceButton;
    }

    public void updateCotrollerFinalAd() {
        ///// обеда
        if (Controller.finalAd) {
            if (fr < 1) {
                Controller.finalAd = false;

             //   System.out.println("BLUE WIN");
           //     gamePlayScreen.getController().addBannerFeiath();
                if(Tank.getMy_Command()== Heading_type.RED_COMMAND) gamePlayScreen.getController().addBannerLOUSER();else
                if(Tank.getMy_Command()== Heading_type.BLUE_COMMAND) gamePlayScreen.getController().addBannerWiner();
            }
            if (fb < 1) {
                Controller.finalAd = false;
             //   System.out.println("RED WIN");
                if(Tank.getMy_Command()== Heading_type.RED_COMMAND) gamePlayScreen.getController().addBannerWiner();else
                if(Tank.getMy_Command()== Heading_type.BLUE_COMMAND) gamePlayScreen.getController().addBannerLOUSER();
            }
        }

        //System.out.println(Controller.finalAd + "  Controller.finalAd " + fr + "   " + fb);
        /// начало матча
        if ((!Controller.finalAd) && (fr > 0) && (fb > 0) && (time_in_game > 8))
            Controller.finalAd = true;
        //else Controller.finalAd = false;

    }
}
