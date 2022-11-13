package com.mygdx.tanks2d.ParticleEffect.StereoSmoke;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class Falling_element { // базовый класс
    Vector3 position;
    Texture texture;
    SpriteBatch spriteBatch;
    float speed;
    static final float MIN_H = 0;
    static final float MAX_H = 1;
    float align;
    Color color;
    float dx, dy, wi, hi;


    public Falling_element() {
        this.position = new Vector3(-1000, -1000, -1);
        this.align = MathUtils.random(-360, 360);
        color = Color.WHITE;
    }

//    public void add(float x, float y, float h, float speed, Texture tex) {
//        this.position.set(x, y, h);
//        this.speed = speed;
//        this.texture = tex;
//        this.color.a = .7f;
//    }

    public void add(float x, float y, float h, float speed, Texture tex, float r, float g, float b, float a) {
        this.position.set(x, y, h);
        this.speed = speed;
        this.texture = tex;
        this.color = new Color(r, g, b, a);
    }

    protected void update(float dt, Camera camera) {
        position.z -= dt * speed;
        dx = ((camera.position.x - position.x) / -300);
        dy = ((camera.position.y - position.y) / -300);
        wi = position.z + texture.getWidth();
        hi = position.z + texture.getHeight();

    }

    protected boolean checkLimet(){
        if (position.z > this.MAX_H) return false;
        if (position.z < this.MIN_H) return false;
        return true;
    }

    public void rander(float dt, Camera camera, SpriteBatch spriteBatch) {
        if(!checkLimet()) return;
        update(dt, camera);
        spriteBatch.setColor(this.color);
        spriteBatch.draw(
                texture, position.x + dx, position.y + dy,
                wi / 2,
                hi / 2,
                texture.getWidth(), texture.getHeight(),
                position.z+.4f, position.z +.4f, align, 0, 0
                , texture.getWidth(), texture.getHeight(), false, false

        );
        spriteBatch.setColor(1, 1, 1, 1);


    }
}
