package com.mygdx.tanks2d.ParticleEffect.StereoSmoke;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class Flying_stereo_elements_base { // это используется!!!!!!!!!!!!
    Vector3 position;  // позиция
    Texture texture;   // текстура
    SpriteBatch spriteBatch;   // батч
    float speed;      // скорость полета
    static final float MIN_H = -1;   // минимальнаявысота
    static final float MAX_H = 5;     // максимальнаявысота
    float align; // угол текстуры
    Color color; // цвет ендера
    float dx, dy, wi, hi;// зачем не помню
    float scale = 1;  // размер

    private float deltaCamera = -1;

    public Flying_stereo_elements_base(SpriteBatch spriteBatch) {
        this.position = new Vector3(-1000, -1000, -100);
        this.align = 1;
        color = Color.WHITE;
        this.spriteBatch = spriteBatch;
        scale = 1;
        this.align = MathUtils.random(180);

    }

    public void add(float x, float y, float h, float scale, float speed, Texture tex, float r, float g, float b, float a) {
        this.position.set(x, y, h);
        this.speed = speed;
        this.texture = tex;
        this.color = new Color(r, g, b, a);
        this.scale = scale;
    }

    protected void update(float dt, Camera camera) {

        //deltaCamera = MathUtils.map(this.MAX_H, this.MIN_H, 300, 0, position.z);
        position.z += dt * speed;

        //    System.out.println(dx);
        dx = ((camera.position.x - position.x) * (position.z - 1) * -5);
        dy = ((camera.position.y - position.y) * (position.z - 1) * -5);

        dx = MathUtils.map(0, 2500, 0, 50, dx);
        dy = MathUtils.map(0, 2500, 0, 50, dy);

        this.color.a = MathUtils.map(this.MAX_H, this.MIN_H * 3, .000f, .7f, position.z);
    }



    protected boolean checkLimet() {
        if (position.z > this.MAX_H - .5f) return false;
        if (position.z < this.MIN_H) return false;
        return true;
    }

    public void rander(float dt, Camera camera) {
        if (! checkLimet()) return;
        update(dt, camera);
        spriteBatch.setColor(this.color);
        float scaleDel = scale + position.z * .001f;
//        spriteBatch.draw(
//                texture,
//                position.x - dx,
//                position.y - dy,
//                texture.getWidth() / 2,
//                texture.getHeight() / 2,
//                texture.getWidth(), texture.getHeight(),
//                scaleDel, scaleDel,
//                align, 0, 0
//                , texture.getWidth(), texture.getHeight(), false, false
//        );
        scaleDel = position.z * scale;

        spriteBatch.draw(
                texture,
                position.x - ((texture.getWidth() * scaleDel) / 2) + dx,
                position.y - ((texture.getHeight() * scaleDel) / 2) + dy,
                ((texture.getWidth() * scaleDel) / 2),
                ((texture.getHeight() * scaleDel) / 2),
                texture.getWidth() * scaleDel, texture.getHeight() * scaleDel,
                1, 1,
                align, 0, 0
                , texture.getWidth(), texture.getHeight(), false, false
        );
        spriteBatch.setColor(1, 1, 1, 1);
    }


}
