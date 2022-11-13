package com.mygdx.tanks2d.ParticleEffect.StereoSmoke;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Element_up_down extends Flying_stereo_elements_base{ // частицы котрые летят вверх вниз в стерео
    public Element_up_down(SpriteBatch spriteBatch) {
        super(spriteBatch);
    }

    @Override
    protected void update(float dt, Camera camera) {
        super.update(dt, camera);
    }
}
