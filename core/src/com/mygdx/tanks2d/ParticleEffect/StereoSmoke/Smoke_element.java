package com.mygdx.tanks2d.ParticleEffect.StereoSmoke;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class Smoke_element extends Falling_element { /// это горит танк
    static final float MIN_H = 0;
    static final float MAX_H = 18;


    @Override
    protected void update(float dt, Camera camera) {
        this.dx = ((camera.position.x - position.x) / -10);
        this.dy = ((camera.position.y - position.y) / -10);
        this.wi = position.z + texture.getWidth() / 2;
        this.hi = position.z + texture.getHeight() / 2;

        this.position.z += dt * speed;
        this.color.a = MathUtils.map(this.MIN_H, this.MAX_H, .7f, -0.00f, position.z);
       // color.a = Interpolation.pow3OutInverse.apply(color.a);
       // System.out.println(this.color.a);
    }


}
