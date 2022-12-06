package com.mygdx.tanks2d.Locations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayDeque;
import java.util.ConcurrentModificationException;

public class Radspurens { //следы кратеры
    ArrayDeque<RadspurenTank> listRadspurens;
    ArrayDeque<Crater> listCrater;
    Texture sled;
    Texture crater;

    public Radspurens(Texture sled,Texture crater) {
        listRadspurens = new ArrayDeque<>();
        listCrater = new ArrayDeque<>();

        for (int i = 0; i < 2300; i++) {
            listRadspurens.addFirst(new RadspurenTank(0, 0, 0, false));
        }

        for (int i = 0; i < 35; i++) {
            listCrater.addFirst(new Crater(0, 0, 0, false));
        }
        this.sled = sled;


        this.crater = crater;

//        this.sled.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
//        this.crater.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
    }

    public void addRadspurenTank(float x, float y, float align) {
        try {
            RadspurenTank t = this.listRadspurens.pollLast();
            t.aligbDeg = align;
            t.xp = x;
            t.yp = y;
            t.life = true;
            t.timer = 0;
            listRadspurens.addFirst(t);
        }catch (NullPointerException e){}

    }

    public void addCrater(float x, float y, float align) {
        try {
            Crater t = this.listCrater.pollLast();
            t.aligbDeg = align;
            t.xp = x;
            t.yp = y;
            t.life = true;
            t.timer = 0;
            listCrater.addFirst(t);
        }catch (NullPointerException e){}
      //  System.out.println(listCrater);


    }

    public void randerRadspurens(SpriteBatch sb) {
        int size;
        float alpha;
        size = listRadspurens.size();
        for (RadspurenTank rt : listRadspurens) {
            if (!rt.life) {sb.setColor(1,1,1,1);return;}
            alpha = MathUtils.clamp(.0012f * (float) size,0,.6f);
            sb.setColor(1,1,1,alpha);
            sb.draw(sled,
                    rt.xp - 20, rt.yp - 20,
                    20, 20,
                    20, 40,
                    1, 1.095f,
                    rt.aligbDeg,
                    0, 0,
                    sled.getWidth(), sled.getHeight(),
                    false, false);
            size--;
        }

        sb.setColor(1,1,1,1);
    }

    public void clearRadspurens() {// оичстит следы на карте
        try {
            for (RadspurenTank rt : listRadspurens) {
                if(MathUtils.randomBoolean()) continue;
                rt.life = false;
                rt.timer = -1;
            }
        }catch (ConcurrentModificationException e){}

    }

    public void clearCrater() {// оичстит следы на карте
        for (RadspurenTank rt : listCrater) {
         //   if(MathUtils.randomBoolean()) continue;
            rt.life = false;
            rt.timer = -1;
        }
    }


    public void randerCrater(SpriteBatch sb) {
        int size;
        float alpha;
        size = listCrater.size();
        for (RadspurenTank rt : listCrater) {
            if (!rt.life) {sb.setColor(1,1,1,1);return;}
            size--;

            alpha = MathUtils.map(listCrater.size() - 10,0,1,0,size);
            sb.setColor(1,1,1,alpha);
            sb.draw(crater,
                    rt.xp - 64/2, rt.yp - 64/2,
                    64/2, 64/2,
                    64, 64,
                    1, 1,
                    rt.aligbDeg,
                    0, 0,
                    crater.getWidth(), crater.getHeight(),
                    false, false);
            size--;
        }



        sb.setColor(1,1,1,1);


    }


}
