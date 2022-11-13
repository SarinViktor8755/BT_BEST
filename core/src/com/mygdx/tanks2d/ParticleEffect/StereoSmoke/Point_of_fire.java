package com.mygdx.tanks2d.ParticleEffect.StereoSmoke;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.tanks2d.MainGame;
import com.mygdx.tanks2d.ParticleEffect.ParticleCustum;

public class Point_of_fire {// точка генерация поэара

    private float lifetime_initial = 0;
    private float time_life = 0;
    private float x, y;
    private Texture tex;

    private ParticleCustum pc;

    public Point_of_fire(float lifetime_initial, float xp, float yp, ParticleCustum particleCustum) {
        this.lifetime_initial = lifetime_initial;
        this.time_life = lifetime_initial;
        this.pc = particleCustum;
        this.x = xp;
        this.y = yp;
    }

    public Point_of_fire(ParticleCustum particleCustum, Texture tex) {
        this.lifetime_initial = -1;
        this.time_life = MathUtils.random(7, 12);
        this.pc = particleCustum;
        this.x = -1000;
        this.y = -1000;
        this.tex = tex;
    }

    public void update(float dt) {
        if (!isLive()) return;
        this.time_life = time_life - dt;
        //   System.out.println(time_life);

        generateSmoke();


    }

    public boolean isLive() {
        if (time_life < -.5f) return false;
        return true;
    }

    private void generateSmoke() {
        float shans = MathUtils.map(0, lifetime_initial, 0.0f, 0.4f, time_life);
        if (MathUtils.randomBoolean(shans)) {
            float delta = MathUtils.random(-15, 15);
            if (MathUtils.randomBoolean(.20f))
                pc.add_flying_stereo_elements_bases(
                        this.x + delta + tex.getWidth() / 2, this.y + delta + tex.getWidth() / 2,
                        0, 1,
                        1,
                        this.tex,
                        1, .3f, .1f, .3f
                );
            else {
                float black = MathUtils.random(0.05f, 0.15f);
                pc.add_flying_stereo_elements_bases(
                        this.x + delta + tex.getHeight() / 2, this.y + delta + tex.getWidth() / 2,
                        0, 1,
                        1,
                        this.tex,
                        black, black, black, .3f
                );

            }
        }

    }

    public void setParametors(float lifetime_initial, float x, float y) {
        time_life = lifetime_initial;
        this.lifetime_initial = lifetime_initial;
        this.x = x;
        this.y = y;
    }

}
