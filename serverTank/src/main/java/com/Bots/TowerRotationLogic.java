package main.java.com.Bots;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.tanks2d.Units.Tanks.OpponentsTanks;

import java.util.HashMap;

import main.java.com.Units.ListPlayer.ListPlayers;
import main.java.com.Units.ListPlayer.Player;

public class TowerRotationLogic { /// поворот любой башни ЛОГИКА - входит в класс танка одного
    private HashMap<Integer, OpponentsTanks> listOpponents;


    private boolean rotation; // вращение

    public final static float rast_to_target = 80_000; // растояние обноружения цели
    final static float speed_rotation_towr = 120; // скорость вращения башни


    public static void updateTowerRotation(float delta, DBBot dbBot, Player p, ListPlayers listPlayers) {
        making_Decision_Tower(dbBot, p, listPlayers); // принятие решение башня
        rotation_Tower(delta, dbBot, listPlayers, p);/// повернуть башню на градус
        // ..............

    }

    private static void making_Decision_Tower(DBBot dbBot, Player p, ListPlayers lp) { // принятие решение башня
        try {
            if (dbBot.getNomTarget() == null) { // если нет целей
                // dbBot.setTarget_tank(0);
                dbBot.setTarget_angle_rotation_tower(p.getBody_rotation().cpy().rotateDeg(180));
                dbBot.setNomTarget(scanning_the_terrain(dbBot, p, lp)); // поиск цели
            } else {
                capturing_target(dbBot, p, lp);

//            if(!lp.getPlayerForId(dbBot.getNomTarget()).isLive())dbBot.setNomTarget(null);
//            if(lp.getPlayerForId(dbBot.getNomTarget()).getPosi().dst2(p.getPosi()) > rast_to_target) dbBot.setNomTarget(null);
//
                if (MathUtils.randomBoolean(.08f)) ckeck_target(dbBot, p, lp);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            dbBot.setNomTarget(null);

        }

        // System.out.println(dbBot.getNomTarget() + "@@" + p.getId());

    }

    private static Integer scanning_the_terrain(DBBot dbBot, Player p, ListPlayers lp) { //найти цель
        Integer targetID = lp.targetTankForBotAttack(p.getPosi(), p);
        return targetID;
    }

    private static Integer scanning_the_terrain(DBBot dbBot, Player p, ListPlayers lp, Integer idTarget) { // перенайти цель
        Integer targetID = lp.targetTankForBotAttack(p.getPosi(), p);
        return targetID;
    }

    private static void rotation_Tower(float delta, DBBot dbBot, ListPlayers listPlayers, Player p) { /// повернуть башню на градус
        Vector2 tv = new Vector2(0, 1);
        tv.setAngleDeg(p.getRotTower());
        if (!MathUtils.isEqual(dbBot.getTarget_angle_rotation_tower().angleDeg(), tv.rotateDeg(180).angleDeg(), 2.2f)) { // сравнение угла цели , и угл аревльного
            if (tv.angleDeg(dbBot.getTarget_angle_rotation_tower()) > 180) {
                tv.setAngleDeg(p.getRotTower());
                tv.rotateDeg(-speed_rotation_towr * delta);
                p.setRotTower(tv.angleDeg());

            } else {
                tv.setAngleDeg(p.getRotTower());
                tv.rotateDeg(speed_rotation_towr * delta);
                p.setRotTower(tv.angleDeg());
                //System.out.println("RRRR");
            }
        }
    }

    private static boolean capturing_target(DBBot dbBot, Player p, ListPlayers lp) { //захват целт
        int idTarget = dbBot.getNomTarget();
        float align = returnAngle(lp.getPlayerForId(idTarget).getPosi(), p.getPosi());
        dbBot.setTarget_angle_rotation_tower(align);
        return false;
    }

    private static boolean ckeck_target(DBBot dbBot, Player p, ListPlayers lp) { // проверка цели
        try {
            if (lp.getPlayerForId(dbBot.getNomTarget()).getHp() < 1) dbBot.setNomTarget(null);
            if (!lp.getPlayerForId(dbBot.getNomTarget()).isLive()) dbBot.setNomTarget(null);
            if (lp.getPlayerForId(dbBot.getNomTarget()).getPosi().dst2(p.getPosi()) > rast_to_target)
                dbBot.setNomTarget(null);
            //   System.out.println("ckeck_target");
        } catch (NullPointerException e) {
            System.out.println("NullPointerException  ckeck_target");
            dbBot.setNomTarget(null);
            e.printStackTrace();
        }
        return true;
    }

    private static float returnAngle(Vector2 positionoOpponent, Vector2 positionMy) { /// определить угол поворота
        return positionoOpponent.cpy().sub(positionMy).angleDeg();
    }

//


}
