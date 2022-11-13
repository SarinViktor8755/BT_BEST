package com.mygdx.tanks2d.ParticleEffect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PasricalExplosion {
    private Vector2 position;

    private float startTimeMax;
    ////////////////
    private float baseScale;
    private float time_life;
    private float rot;
    /////////////////////////////////////////

    public PasricalExplosion() {
        position = new Vector2();
        rot = 0;
        time_life = 0;
        startTimeMax = 0;
        baseScale = 1;
    }

    public void setParameters(float time_life, float x, float y) {
        this.position.set(x, y);
        this.time_life = time_life;
        this.startTimeMax = time_life;
        this.rot = MathUtils.random(0,360);
    }

    public void setTime_life(float time_life) {
        this.time_life = time_life;
    }

    /////////////////////////////////////////

    public Vector2 getPosition() {
        return position;
    }

    public float getRotation() {
        return rot;
    }



    public boolean isLife() {
        if (time_life > 0) return true;
        return false;
    }

    public void update() {
        time_life = time_life - Gdx.graphics.getDeltaTime();
    }

    public float getScale() {
        return (Interpolation.bounceOut.apply(MathUtils.sin(MathUtils.map(0,startTimeMax,0,2,time_life/2)))) * 2;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setStartTimeMax(float startTimeMax) {
        this.startTimeMax = startTimeMax;
    }

    public void setBaseScale(float baseScale) {
        this.baseScale = baseScale;
    }

    public void setRot(float rot) {
        this.rot = rot;
    }

    public float getTime_life() {
        return time_life;
    }
}
