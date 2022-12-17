package main.java.com.MatchOrganization;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.tanks2d.ClientNetWork.Heading_type;

import java.util.Iterator;
import java.util.Map;

import main.java.com.MainGame;
import main.java.com.Units.ListPlayer.ListPlayers;
import main.java.com.Units.ListPlayer.Player;
import main.java.com.Units.ListPlayer.StatisticMath;

public class IndexMath {
    private static final float SECONDS_MATH = 120;
    private static final float MATH_LENGHT = 1000 * SECONDS_MATH; // время матча
    private static float realTimeMath; // время матча
    private ListPlayers listPlayers; // копия листа

    private static int red_team_score = 0;
    private static int blue_team_score = 0;

    private static int red_team_score_math = 0;  // очков в матче
    private static int blue_team_score_math = 0; // очков в матче


    private final static int DEFOULT_SCORE_RESPOWN = 150;
    private static int SCORE_RESPOWN = 80;

    private static int WINNING_NUMBER_OF_POINTS = 3;

    public void updateMath(float dt, ListPlayers listPlayers, boolean pause_game) {
        this.realTimeMath += dt;
        this.listPlayers = listPlayers;
        this.restartMath(this.realTimeMath);
        if(pause_game) System.out.println("PAUSE game");


    }

    public float getTimeMath() { // время оставшегося матча
        return realTimeMath;
    }

    private boolean is_end_math() {
        if (realTimeMath > MATH_LENGHT) return true;
        else return false;
    }


    public int getCommand() { // определить команду
        //System.out.println();
        //  System.out.print("vibor comdnd : red  " + listPlayers.getRed_size() + " blue :: " + listPlayers.getBlue_size() + "   --  ");
        // listPlayers.counting_games();


        if (StatisticMath.getRedSize() < StatisticMath.getBlueSize()) {
            // System.out.println("RED_COMMAND");
            return Heading_type.RED_COMMAND;
        }
        if (StatisticMath.getBlueSize() < StatisticMath.getRedSize()) {
            //System.out.println("BLUE COMAND");
            return Heading_type.BLUE_COMMAND;
        }


        // System.out.println("____________________________");
        if (MathUtils.randomBoolean()) return Heading_type.RED_COMMAND;
        else return Heading_type.BLUE_COMMAND;
    }

    //////////////////////////////////////////////////////
    public static int getRed_team_score() {
        return red_team_score;
    }

    public static void setRed_team_score(int red_team_score) {
        IndexMath.red_team_score = red_team_score;
    }

    public static int getBlue_team_score() {
        return blue_team_score;
    }

    public static void setBlue_team_score(int blue_team_score) {
        IndexMath.blue_team_score = blue_team_score;
    }

    public static void add_score_blue_team() {
        blue_team_score++;
    }

    public static void add_score_red_team() {
        red_team_score++;
    }

    public static int getRed_team_score_math() {
        return red_team_score_math;
    }

    public static int getBlue_team_score_math() {
        return blue_team_score_math;
    }

    public static void add_score_team(int team) {
        if (team == Heading_type.BLUE_COMMAND) add_score_blue_team();
        if (team == Heading_type.RED_COMMAND) add_score_red_team();
        // System.out.println("RED  " + red_team_score + "  BLUE  " + blue_team_score);
    }

    private void restartMath(float mathTime) { // рестарт матч или конец матча
        if (mathTime < 5000) return;
        if (mathTime > MATH_LENGHT) {

            respon_math();
        }/////////////
        if (red_team_score_math >= WINNING_NUMBER_OF_POINTS) {

            return;
        }
        if (blue_team_score_math >= WINNING_NUMBER_OF_POINTS) {

            return;
        }

        ///////////////////////
        if (StatisticMath.getLiveBlueSize() < 1) {

            respon_math(1);
        }
        if (StatisticMath.getLiveRedSize() < 1) {

            respon_math(2);
        }

    }

    public void respon_math() {
        SCORE_RESPOWN--;
        //System.out.println(SCORE_RESPOWN);
        if (SCORE_RESPOWN > 0) return;

        listPlayers.respownAllPlaers();
        realTimeMath = 0;
        //System.out.println("RESTART MATH");
        SCORE_RESPOWN = DEFOULT_SCORE_RESPOWN;
    }

    public void respon_math(int comand) {
        SCORE_RESPOWN--;
       // System.out.println(SCORE_RESPOWN);
        if (SCORE_RESPOWN > 0) return;

        if (comand == 1) red_team_score_math++;
        if (comand == 2) blue_team_score_math++;

        listPlayers.respownAllPlaers();
        realTimeMath = 0;
       // System.out.println("RESTART MATH " + comand);

        SCORE_RESPOWN = DEFOULT_SCORE_RESPOWN;
    }


    public static float getRealTimeMath() {
        return realTimeMath;
    }

    ///////////////////

}
