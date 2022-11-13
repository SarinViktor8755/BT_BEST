package main.java.com.Units.ListPlayer;

public class PlayerStatic {
    String Tokken;

    String nik_name;

    int frag;
    int death;
    float time_in_game;
    int score;

    public PlayerStatic(String tokken) { // tokken - ключ
        Tokken = tokken;
    }



    public void getPlayer(){


    }

    @Override
    public String toString() {
        return
                "|T='" + Tokken + '\'' +
                "n='" + nik_name + '\'' +
                "f=" + frag +
                "d=" + death +
                "ti=" + time_in_game +
                "se=" + score +
                '|';
    }
}


