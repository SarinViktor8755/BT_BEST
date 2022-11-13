package main.java.com.Units.SpaceMap.Figure;

import com.badlogic.gdx.math.Vector2;

public class Rectangle implements Figure {

    private Vector2 position;  /// левая нижняя точка
    private Vector2 positionWH; /// верхняя правая точка
    Vector2 center;// центр прямаугольника

    public Rectangle(float x, float y, float width, float height, int hm) { /// конструктор прямоугольника
        //     System.out.println("---------!!  " + x + "  " + y + " -|||-  " + width + " -  " + height);

        this.position = new Vector2(x, y - height);
        this.positionWH = new Vector2(position.x + width, position.y + height);

        getCenter();
//
//        System.out.println(position);
//        System.out.println(positionWH);
//        System.out.println("--------------------------------");

        //   System.out.println(this);
    }

    @Override
    public boolean isPointCollision(float x, float y) {  /// это точка пересекается  с прямоугольником или нет
        if (
                (x > this.position.x - 15) && (x < this.positionWH.x + 15)
                        &&
                        (y > this.position.y - 15) && (y < this.positionWH.y + 15)
        ) return true;

        return false;
    }


    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getPositionWH() {
        return positionWH;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "nih lev =" + getPosition() +
                ", pravi verhn =" + getPositionWH() +
                '}';
    }

    private void getCenter() {
        center = new Vector2();
        float x = (position.x + positionWH.x) / 2f;
        float y = (position.y + positionWH.y) / 2f;
        this.center.set(x, y);
    }


    @Override
    public Vector2 get_vector2_from_center(float x, float y) {
        if (isPointCollision((int) x, (int) y)) {
            Vector2 pos = new Vector2(x, y);
            Vector2 tempVector = new Vector2();
            tempVector.set(pos.cpy().sub(center));
            if (Math.abs(tempVector.x) >= Math.abs(tempVector.y)) {
                if (tempVector.x > 0) return new Vector2(1, 0);
                else return new Vector2(-1, 0);
            } else {
                if (tempVector.y > 0) return new Vector2(0, 1);
                else return new Vector2(0, -1);
            }
        }


        return null;
    }

    public Vector2 get_vector2_from_center(final Vector2 pos) {
        if (isPointCollision(pos.x, pos.y)) {
            Vector2 tempVector = new Vector2();
            tempVector.set(pos.cpy().sub(center));
//            if (Math.abs(tempVector.x) >= Math.abs(tempVector.y)) {
//                if (tempVector.x > 0) return new Vector2(1, 0);
//                else return new Vector2(-1, 0);
//            } else {
//                if (tempVector.y > 0) return new Vector2(0, 1);
//                else return new Vector2(0, -1);
//            }


        }
        return null;
    }


}
