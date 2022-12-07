package com.mygdx.tanks2d.Screens.Controll;

import static com.mygdx.tanks2d.MainGame.HEIGHT_SCREEN;
import static com.mygdx.tanks2d.MainGame.WIDTH_SCREEN;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.tanks2d.AudioEngine.AudioEngine;
import com.mygdx.tanks2d.MainGame;

import java.util.ArrayList;

public class Banner { // банер на гланом экране
    private float timeLife;
    private static final float defoult_time_life = 2f;
    private boolean flag_sound;

    private ArrayList<Integer> q = new ArrayList<>();
    private SpriteBatch batch;
    //  private boolean voise;


    private Texture feature;
    private Texture victory;
    private Texture failed;

    private Texture temp_texture;

    private Texture beck_graund;
    private AudioEngine ae;


    public Banner(SpriteBatch spriteBatch, AudioEngine audioEngine, Texture feature, Texture victory, Texture failed, Texture bg) {
        this.timeLife = 0;
        this.batch = spriteBatch;
        this.beck_graund = bg;
        this.feature = feature;
        this.victory = victory;
        this.failed = failed;
        // voise = false;
        ae = audioEngine;
        flag_sound = false;

    }

    public void update() {
//        System.out.println(q);
//        if (MathUtils.randomBoolean(.5f)) {
//            addBaner(MathUtils.random(1, 3));
//        }

        timeLife -= Gdx.graphics.getDeltaTime();
        ////////////////////////////////////
        if (!isWorking()) {
            timeLife = defoult_time_life;

            return;
        }
        timeLife -= Gdx.graphics.getDeltaTime();
        //if(voise && timeLife < 1.5){ae.pley_fight_ad_sound(); voise = false;}

        if (timeLife < 0) {
            delBanner();
            timeLife = defoult_time_life;
            flag_sound = true;
        }


        //   if (isWorking()) rander(batch);
    }

    private void delBanner() {
        if (isWorking()) q.remove(0);
    }


    public void rander(SpriteBatch batch) {
        temp_texture = this.victory;
        if (q.get(0) == 2) temp_texture = this.victory;
        if (q.get(0) == 3) temp_texture = this.failed;
        if (q.get(0) == 1) temp_texture = this.feature;

        if (timeLife < 1.3 && flag_sound) {
            flag_sound = false;
            if (q.get(0) == 2) ae.pley_win_ad_sound();
            if (q.get(0) == 3) ae.pley_lose_ad_sound();
            if (q.get(0) == 1) ae.pley_fight_ad_sound();

        }

        //if(q.get0)
        float scale = MathUtils.map(0, 1, .5f, 0, defoult_time_life - timeLife);
        float alpha_bg = 0;
        //    = MathUtils.map(0, 2f, 0, .3f, timeLife);

        if (timeLife < 1) alpha_bg = MathUtils.map(1, 0, .5f, 0, timeLife);
        else
            alpha_bg = MathUtils.map(1, 2, .5f, 0, timeLife);

        alpha_bg = MathUtils.clamp(Interpolation.exp10Out.apply(alpha_bg), 0, .5f);

        batch.setColor(0, 0, 0, alpha_bg);
        batch.draw(beck_graund, 0, 0, WIDTH_SCREEN, HEIGHT_SCREEN);
        for (int i = 6; i > 1; i--) {
            batch.setColor(1, 1, 1, MathUtils.map(1, 6, 1, 0, i));
            batch.draw(temp_texture,
                    WIDTH_SCREEN / 2 - (temp_texture.getWidth() / 2), // ширина экрана
                    120 - (Interpolation.swing.apply(scale) * (120 * i))// высота экрана
                    , temp_texture.getWidth(), 100);
        }


    }

    public void addBaner(int nom) {
        q.add(nom);
    }

    public boolean isWorking() {
        if (q.size() > 0) return true;
        else return false;
    }

    public void addBannerWiner() {
    }
}


