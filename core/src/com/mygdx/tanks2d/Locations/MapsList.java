package com.mygdx.tanks2d.Locations;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

public class MapsList {
    ArrayList<String> mapsList;

    public MapsList() {
        this.mapsList = new ArrayList<>();
        mapsList.add("field");
        mapsList.add("desert");
        mapsList.add("wasteland");

    }

    public static String getMapForServer() {
        ArrayList<String> ml;
        ml = new ArrayList<String>();
        ml.add("field");
        ml.add("desert");
        ml.add("wasteland");
        return ml.get(MathUtils.random(ml.size() - 1));
    }


}
