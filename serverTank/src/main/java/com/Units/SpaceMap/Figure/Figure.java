package main.java.com.Units.SpaceMap.Figure;

import com.badlogic.gdx.math.Vector2;

public interface Figure {
    Vector2 position = new Vector2(Vector2.Zero);
    public boolean isPointCollision(float x, float y);
    Vector2 get_vector2_from_center(float x, float y);
}
