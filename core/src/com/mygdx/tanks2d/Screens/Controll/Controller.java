package com.mygdx.tanks2d.Screens.Controll;

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
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.tanks2d.MainGame;
import com.mygdx.tanks2d.Screens.GamePlayScreen;


/**
 * Created by brentaureli on 10/23/15.
 */
public class Controller {
    // Skin skinGame;

    private Banner banner;

    final private Viewport viewport;
    final private Stage stage;

    OrthographicCamera cam;

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

    private Label live_score_red;
    private Label live_score_blue;
    private Label my_frag;
    /////////////
    private BitmapFont font;

    private GamePlayScreen gamePlayScreen;

    private boolean buttonChangingOpponent;

    private int frag = 0;

    private boolean contollerOn;


    Vector2 directionMovement; // Направление движения

    private float time_in_game; // время в игре

    public boolean isInTuchMove() {
        return inTuchMove;
    }

    public Controller(GamePlayScreen gsp) {

        // gsp.getAssetManager().get("de.pack", TextureAtlas.class);
        time_in_game = 0;

        distance = new Vector2();
        inTuchMove = false;
        attackButon = false;
        voiceButton = false;
        chance = false;

        this.directionMovement = new Vector2(0, 0);
        cam = new OrthographicCamera();

        viewport = new FitViewport(MainGame.WHIDE_SCREEN, MainGame.HIDE_SCREEN, cam);
        contollerOn = false;


        stage = new Stage(viewport, gsp.getBatch());
        Gdx.input.setInputProcessor(stage);
        temp_Point = new Vector2(0, 0);
        buttonChangingOpponent = false;

        font = gsp.getAMG().get("fonts/font.fnt", BitmapFont.class);
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
        font.getData().setScale(.8f);
        font.getColor().set(.5f, .5f, .5f, 1);


        final float sw = MainGame.WHIDE_SCREEN;
        final float sh = MainGame.HIDE_SCREEN;

        banner = new Banner(gsp.getBatch());

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

        voiceButtonImg =  new Image((Texture) gsp.getMainGame().getAMG().get("microphone.png"));
        voiceButtonImg.setSize(90, 90);
        voiceButtonImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                voiceButton = true;
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
        stick.setPosition(0, 0);

        attacButton.setSize(55, 55);
        attacButton.setPosition(sw - 100, 40);

        voiceButtonImg.setSize(55, 55);
        voiceButtonImg.setPosition(sw - 100, 170);

        changingGoal.setSize(55, 55);
        changingGoal.setPosition(sw - 100, 105);




        Group gropuButton = new Group();
        Group gropuStick = new Group();

        gropuStick.setPosition(35, 35);

        gropuStick.addActor(stick);
        gropuStick.addActor(pointStick);


        gropuButton.addActor(gropuStick);
        gropuButton.addActor(attacButton);
        gropuButton.addActor(voiceButtonImg);

        gropuButton.addActor(changingGoal);


///////////////////
        // skinGame = gsp.getMainGame().assetManager.get("skin/metal-ui.json", Skin.class);
        labelHP = new Label("HP:", style);
        labelHP.setX(30);
        labelHP.setY(sh - 40);
        stage.addActor(labelHP);

        ///////////////////////////////
        score_red = new Label("RED:", style);
        score_red.setColor(Color.RED);
        score_red.setX(30);
        score_red.setY(sh - 70);
        stage.addActor(score_red);

        score_blue = new Label("BLUE:", style);
        score_blue.setColor(Color.BLUE);
        score_blue.setX(30);
        score_blue.setY(sh - 100);
        stage.addActor(score_blue);


        my_frag = new Label("frags : " + frag, style);
        my_frag.setColor(Color.YELLOW);
        my_frag.setX(30);
        my_frag.setY(sh - 130);
        stage.addActor(my_frag);

        live_score_red = new Label("RED_LIVE:", style);
        live_score_red.setColor(Color.RED);
        live_score_red.setX(30);
        live_score_red.setY(sh - 160);
        stage.addActor(live_score_red);

        live_score_blue = new Label("BLUE_LIVE:", style);
        live_score_blue.setColor(Color.BLUE);
        live_score_blue.setX(30);
        live_score_blue.setY(sh - 190);
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



    }

    public boolean isButtonChangingOpponent() {
        return buttonChangingOpponent;
    }

    public boolean isChance() {
        return chance;
    }

    public void addFrag() {
        this.frag++;
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

    public void draw() {
        this.update();
        stage.draw();
        //    System.out.println(this.inTuchMove);
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

        banner.update();

        // labelHP = MathUtils.random(5,50);
        //labelHP.setText("HP: " + hp);
        //System.out.println(buttonChangingOpponent);
       // changingGoal.setVisible(contollerOn);


        time_in_game+=Gdx.graphics.getDeltaTime();
       // setBlueCommand(45 - (int) time_in_game);
        score_blue.setText("TIME_MATH "+(45 - (int) time_in_game));
   //     setBlueCommand((int) time_in_game);
    //    System.out.println((time_in_game) + "  --");


        pointStick.setVisible(contollerOn);
        attacButton.setVisible(contollerOn);
        stick.setVisible(contollerOn);


        if (buttonChangingOpponent) changingGoal.setColor(1, 1, 1, .3f);
        else changingGoal.setColor(1, 0, 0, .1f);
        if(!contollerOn) changingGoal.setColor(1,1,1,1);
    }

    public void setContollerOn(boolean contollerOn) {
        this.contollerOn = contollerOn;
    }

    public void setButtonChangingOpponent(boolean buttonChangingOpponent) {
        this.buttonChangingOpponent = buttonChangingOpponent;
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
}
