package com.mygdx.tanks2d.ParticleEffect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PasricalExplosionBigParameter{
    private Vector2 position;
    ////////////////
    private float baseScale;
    private float time_life;
    private float rot;
    private float alpha;

    int par = 0;

    private final float baseTimeLife = .6f;

    public PasricalExplosionBigParameter() {
        position = new Vector2();
        rot = 0;
        time_life = baseTimeLife;;
        baseScale = 1;
        alpha = 0;
    }

    public void setParameters(float x, float y) {
        this.position.set(x, y);
        this.time_life = baseTimeLife;
        this.rot = MathUtils.random(0,360);
        par = 0;
    }

    public boolean isLife() {
        if (time_life > 0) return true;
        return false;
    }

    public void update(ParticleCustum particleCustum) {
        time_life = time_life - Gdx.graphics.getDeltaTime();
        if(alpha < 1) return;
//        if(MathUtils.randomBoolean(Gdx.graphics.getDeltaTime() * 30))
//            for (int i = 0; i < 2; i++) {
//                particleCustum.addGarbage(getPosition().x, getPosition().y);
//            }


    }

    public Vector2 getPosition() {
        return position;
    }

    public float getBaseScale() {
        return baseScale;
    }

    public float getScale(){
        float a = MathUtils.map(0,baseTimeLife,0,2,time_life);
        if(time_life > baseTimeLife * .5f) alpha = 1; else alpha = a;
        return Interpolation.swingIn.apply(MathUtils.sin(a)) * 8;
    }

    public float getAlpha(){
        return alpha;
    }

    public float getRot() {
        return rot;
    }
}
