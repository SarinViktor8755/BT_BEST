package com.mygdx.tanks2d.Screens.Controll;

import static com.mygdx.tanks2d.MainGame.WIDTH_SCREEN;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

public class Banner { // банер на гланом экране
    private float timeLife;
    private static final float defoult_time_life = 1;
    private ArrayList<Integer> q = new ArrayList<>();
    private SpriteBatch batch;


    private Texture feature;



    public Banner(SpriteBatch spriteBatch, Texture feature) {
        this.timeLife = 0;
        this.batch = spriteBatch;

        this.feature =  feature;
    }

    public void update() {

        if (MathUtils.randomBoolean(.005f)) {
            addBaner(MathUtils.random(500));
        }

        timeLife-=Gdx.graphics.getDeltaTime();
        ////////////////////////////////////
        if (!isWorking()) {
            timeLife = defoult_time_life;
            return;
        }
        timeLife -= Gdx.graphics.getDeltaTime();
        if (timeLife < 0) {
            delBanner();
            timeLife = defoult_time_life;
        }


     //   if (isWorking()) rander(batch);
    }

    private void delBanner() {
        if (isWorking()) q.remove(0);
    }


    public void rander(SpriteBatch batch) {
        float scale = MathUtils.map(0,1,0,.5f,timeLife);
        float alpha = MathUtils.sinDeg(timeLife + .5f);
      //  System.out.println("!!!!!!!!!!!!!!!!!!!!");
        batch.setColor(alpha,1,1,scale);
        batch.draw(feature,
                160 , // ширина экрана
                150 // высота экрана
                , 200 * scale, 100 * scale);

//        System.out.println(scale);
//        System.out.println(alpha);
//
//        System.out.println(q);
//        System.out.println(timeLife);
    }

    public void addBaner(int bn) {
        q.add(bn);
    }

    public boolean isWorking() {
        if (q.size() > 0) return true;
        else return false;
    }
}


