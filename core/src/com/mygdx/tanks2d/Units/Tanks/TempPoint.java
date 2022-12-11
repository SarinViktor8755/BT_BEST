package com.mygdx.tanks2d.Units.Tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Vector;

public class TempPoint {
    Vector position;
    float timeLife;

    public TempPoint(Vector position) {
        this.position = position;
        this.timeLife = 1f;

    }

    public void update(SpriteBatch spriteBatch, Texture texture){
        timeLife -= Gdx.graphics.getDeltaTime();




    }
}
