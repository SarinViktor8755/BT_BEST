package com.mygdx.tanks2d.Locations.Collision;

import com.badlogic.gdx.math.Vector2;

public class BoxCollision {
    Vector2 lb; // леывй нижний край
    Vector2 ru; // правый верхний
    Vector2 center;// центр прямаугольника

    public BoxCollision(Vector2 lb, Vector2 ru) {
        this.lb = lb;
        this.ru = ru;
        this.center = new Vector2();
        getCenter();
       // System.out.println(" Rectangle: position" + lb + "  WH" + ru + " center _ " + center);
    }

    public boolean isCollisionTank(Vector2 p) {
        if (p.x + 15 < this.lb.x) return true;
        if (p.x - 15 > this.ru.x) return true;
        if (p.y + 15 < this.lb.y) return true;
        if (p.y - 15 > this.ru.y) return true;
        return false;
    }

    private void getCenter() {
        float x = (lb.x + ru.x) / 2f;
        float y = (lb.y + ru.y) / 2f;
        this.center.set(x, y);
    }

    public Vector2 getLb() {
        return lb;
    }

    public Vector2 getRu() {
        return ru;
    }

    Vector2 get_vector2_from_center(int x, int y) {
        return center;
    }

    @Override
    public String toString() {
        return "Box{" +
                "lb=" + lb + ", ru=" + ru +
                '}';
    }
}
