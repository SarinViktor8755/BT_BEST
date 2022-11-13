package com.mygdx.tanks2d.ParticleEffect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public class ParticalShootFlash {
    ParticleEffect peSmoke;
    SpriteBatch sb;

    public ParticalShootFlash(SpriteBatch sb) {
        this.sb = sb;
        peSmoke = new ParticleEffect();
        peSmoke.load(Gdx.files.internal("particle/smooke"), Gdx.files.internal("particle/"));
        peSmoke.getEmitters().first().setPosition(0, 0);
    }

    public void startSmooke(float x, float y) {
        peSmoke.start();
        peSmoke.getEmitters().first().setPosition(x, y);
    }

    public void startSmooke(Vector2 pos) {
        startSmooke(pos.x, pos.y);
    }



    public void renderSmooke(float x, float y) {
        peSmoke.setPosition(x, y);
        peSmoke.allowCompletion();
        peSmoke.update(Gdx.graphics.getDeltaTime());
        peSmoke.draw(sb);
    }

    public void renderSmooke(Vector2 p) {
        peSmoke.setPosition(p.x, p.y);
        peSmoke.allowCompletion();
        peSmoke.update(Gdx.graphics.getDeltaTime());
        peSmoke.draw(sb);
    }

}
