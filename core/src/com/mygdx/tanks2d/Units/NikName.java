package com.mygdx.tanks2d.Units;

import static com.badlogic.gdx.math.MathUtils.random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mygdx.tanks2d.MainGame;

public class NikName {
    public static String getNikName() {
        String nik = MainGame.nik_name;
        if (MainGame.nik_name == null) {
            Preferences prefs = Gdx.app.getPreferences("NikName");
            return prefs.getString("nik", "UserName_" + random(512));
        } else return MainGame.nik_name;

    }

    public static void setNikName(String nik) {
        Preferences prefs = Gdx.app.getPreferences("NikName");
        if (nik.length() < 1) nik = "Ananimus";
        prefs.putString("nik", nik);
        prefs.flush();
    }

    public static String getTokken() {
        String token;
        String tokken = "tokken";
        Preferences prefs = Gdx.app.getPreferences("Tanks2dPreferences");
        if (!prefs.contains(tokken)) token = genirateTokk();
        else token = prefs.getString(tokken);
        prefs.putString(tokken, token);
        prefs.flush();
        //System.out.println(prefs.getString(tokken));
        return token;
    }


    private static String genirateTokk() {
        long longToken = Math.abs(random.nextLong());
        return Long.toString(longToken, 32);
    }
}
