package com.mygdx.tanks2d.ParticleEffect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;



public class ParticleSmoke {
    private Vector2 position;
    private float rotation;
    private float scale;
    ////////////////
    private Vector2 velocity;
    private float rotate;
    private float time_life;
    private float scalingKef;

    private Color color;

    private boolean live;

    public ParticleSmoke() {
        position = new Vector2();
        rotation = 0;
        scale = 1;

        velocity = new Vector2();
        rotate = 0;
        time_life = -1;
        scalingKef = 0;
        live = false;
        this.color = new Color();
    }

    /////////////////////////////////////////
    public void setParameters(float time_life, float x, float y, float rotate, float rotationAdd, float scale, float scalingKef, float vx, float vy, float r ,float g, float b, float a) {
        position.set(x, y);
        rotation = 0;
        this.rotate = rotate;
        this.rotation = rotationAdd;
        this.scale = scale;
        this.velocity.set(vx, vy);
        this.time_life = time_life;
        this.scalingKef = scalingKef;
        this.live = true;
        this.color.set(r,g,b,a);
    }
    /////////////////////////////////////////

    public void setAlpha(float alpha){
        this.color.set(color.r,color.g,color.b,alpha);
    }

    public Color getColor() {
        return color;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getRotation() {
        return rotate;
    }

    public float getScale() {
        return scale;
    }

    public boolean isLife() {
//        if (!live) return false;
        if (time_life > 0) return true;
//        live = false;
        return false;
    }


    public void update() {
        time_life = time_life - Gdx.graphics.getDeltaTime();
        position.add(velocity.cpy().scl(Gdx.graphics.getDeltaTime()));
        rotate += + rotation * Gdx.graphics.getDeltaTime();
        scale += + scalingKef * Gdx.graphics.getDeltaTime();
    }

    public float getTime_life() {
        return time_life;
    }



    public void setTime_life(int i) {
        this.time_life = i;
    }
}
