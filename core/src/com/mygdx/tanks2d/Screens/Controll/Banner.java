package com.mygdx.tanks2d.Screens.Controll;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

public class Banner { // банер на гланом экране
    private float timeLife;
    private static final float defoult_time_life = 1;
    private ArrayList<Integer> q = new ArrayList<>();
    private SpriteBatch batch;

    public Banner(SpriteBatch spriteBatch) {
        this.timeLife = 0;
        this.batch = spriteBatch;
    }

    public void update() {

        if (MathUtils.randomBoolean(.005f)) {
            addBaner(MathUtils.random(500));
        }


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


        if (isWorking()) rander();
    }

    private void delBanner() {
        if (isWorking()) q.remove(0);
    }


    private void rander() {
        float scale = MathUtils.map(0,1,0,.5f,timeLife);
        float alpha = MathUtils.sinDeg(timeLife + .5f);
        System.out.println(scale);
        System.out.println(alpha);

        System.out.println(q);
        System.out.println(timeLife);
    }

    public void addBaner(int bn) {
        q.add(bn);
    }

    private boolean isWorking() {
        if (q.size() > 0) return true;
        else return false;
    }
}


