package com.mygdx.tanks2d.Screens.Controll;

import static com.mygdx.tanks2d.MainGame.WIDTH_SCREEN;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

public class Banner { // банер на гланом экране
    private float timeLife;
    private static final float defoult_time_life = 2f;
    private ArrayList<Integer> q = new ArrayList<>();
    private SpriteBatch batch;


    private Texture feature;



    public Banner(SpriteBatch spriteBatch, Texture feature) {
        this.timeLife = 0;
        this.batch = spriteBatch;

        this.feature =  feature;
    }

    public void update() {

        if (MathUtils.randomBoolean(.5f)) {
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
        float scale = MathUtils.map(0,1,.5f,0,defoult_time_life - timeLife);
    //    float alpha = MathUtils.map(0,1,0,3,timeLife);

 //       System.out.println("!!!!!!!!!!!!!!!!!!!!" + timeLife);


        for (int i = 6; i > 1; i--) {
            batch.setColor(1,1,1,MathUtils.map(1,6,1,0,i));
            batch.draw(feature,
                    160, // ширина экрана
                    120  - (Interpolation.swing.apply(scale) * (120 * i))// высота экрана
                    , 200 , 100);
        }


    }

    public void addBaner(int bn) {
        q.add(bn);
    }

    public boolean isWorking() {
        if (q.size() > 0) return true;
        else return false;
    }
}


