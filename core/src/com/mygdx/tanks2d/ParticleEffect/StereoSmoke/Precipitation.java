package com.mygdx.tanks2d.ParticleEffect.StereoSmoke;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Precipitation extends  Flying_stereo_elements_base{
    public Precipitation(SpriteBatch spriteBatch) {
        super(spriteBatch);
    }

    @Override
    protected void update(float dt, Camera camera) {
        position.z -= dt * speed;

        //    System.out.println(dx);
        dx = ((camera.position.x - position.x) * (position.z - 1) * -5);
        dy = ((camera.position.y - position.y) * (position.z - 1) * -5);

        dx = MathUtils.map(0, 2500, 0, 50, dx);
        dy = MathUtils.map(0, 2500, 0, 50, dy);

        this.color.a = MathUtils.map(this.MAX_H, this.MIN_H * 3, .000f, .7f, position.z);
    }
}
