package com.mygdx.tanks2d.Locations.Collision;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.tanks2d.Utils.VectorUtils;


public class CircleCollision {
    Vector2 circule;
    float radius;
    float getRadiusKV;

    public CircleCollision(Vector2 circule, float radius) {
      //  System.out.println("CircleCollision");
        this.circule = circule;
        this.radius = radius;
        this.getRadiusKV = radius * radius;
       // System.out.println("createEllipse position x " +circule.x + " y"+circule.y +"   "+ this.circule+"  redius " + this.radius);

    }

    public CircleCollision(float x, float y, float radius) {
        this.circule = new Vector2(x, y);
        this.radius = radius;
    }

    public boolean isCollisionCircle(Vector2 pos) {
        if (VectorUtils.getLen2(pos, circule.x, circule.y) > getRadiusKV + 3500 ) return true;
        return false;
    }

    @Override
    public String toString() {
        return "CircleCollision{circule=" + circule + ", radius=" + radius + '}';
    }

    Vector2 get_vector2_from_center(int xp, int yp){
        return null;
    }
}
