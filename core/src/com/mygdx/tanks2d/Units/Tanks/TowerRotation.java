package com.mygdx.tanks2d.Units.Tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

public class TowerRotation { /// поворот любой башни ЛОГИКА - входит в класс танка одного


    private boolean pip_song = true;
    private HashMap<Float, Integer> targetTreet; // цели - угол до цели - номер цели )))
    private HashMap<Integer, OpponentsTanks> listOpponents;

    private float timerTackt;

    private int target_tank; // номер ид цели
    private Integer nomTarget; // номер ид цели

    private Vector2 direction;
    private Vector2 direction_tower;
    private Vector2 myPosition;

    private int myCommand;

    private boolean rotation;

    private boolean guidance = false; //наведеени на цель

    final float rast_to_target = 80_000; // растояние обноружения )

    public TowerRotation(Vector2 direction, Vector2 direction_tower, Vector2 pos, HashMap<Integer, OpponentsTanks> listOpponents, int command) {
        this.targetTreet = new HashMap<>();
        this.direction = direction;
        this.direction_tower = direction_tower;
        this.myPosition = pos;
        this.listOpponents = listOpponents;
        this.timerTackt = 0;
        this.rotation = false;
        this.myCommand = command;
        pip_song = true;
        // this.myCommand  =
    }

    public float getTimerTackt() {
        return timerTackt;
    }

    public void update(float delta) {
        makingDecisionTower(delta);
        this.timerTackt += delta;
    }

    private void makingDecisionTower(float delta) { // принятие решение башня
        //    System.out.println(timerTackt);

        if (nomTarget == null) { // если нет целей
            turningTower(this.direction.angleDeg(), delta);
            target_tank = 0;
            if (MathUtils.randomBoolean(.3f)) targetDetectionTower(this.myPosition); // ищем цели
            nomTarget = selectTarget();
        } else {    // если  цели есть
            try {
                if (!listOpponents.get(nomTarget).isLive()) { // сброс цели
                    target_tank = 0;
                    nomTarget = null;
                    return;
                }
            } catch (NullPointerException e) {
                nomTarget = null;
                return;
            }
            turningTower(returnAngle(listOpponents.get(nomTarget).getPosition(), myPosition), delta);
            if (MathUtils.randomBoolean(.05f)) targetDetectionTower(this.myPosition); // ищем цели
            if (checkLen()) nomTarget = null;
            if (changeCommand()) nomTarget = null;

        }
    }


    public boolean changeCommand() {// проверяет команду
        try {
            return myCommand == listOpponents.get(nomTarget).getCommand();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isGuidance() {
        return guidance;
    }

    private void turningTower(float targetAlign, float delta) { /// повернуть башню на градус
        //    System.out.println(guidance);
        float temp = direction.cpy().setAngleDeg(targetAlign).angleDeg(direction_tower);

        if (temp > 358 || temp < 2) guidance = true;
        else guidance = false;


        /// angle_difference = direction.cpy().setAngleDeg(targetAlign).angleDeg(direction_tower);
        if (!MathUtils.isEqual(direction_tower.angleDeg(), targetAlign, 1.2f)) {
            rotation = true;
//            System.out.println( "  ___ "+
//                    Math.abs(
//                            direction.cpy().setAngleDeg(targetAlign).angleDeg(direction_tower)
//                    )
//            );
            if ((direction.cpy().setAngleDeg(targetAlign).angleDeg(direction_tower) > 180))
                direction_tower.rotateDeg(-120 * delta);
            else
                direction_tower.rotateDeg(120 * delta);
        }
    }

    private void targetDetectionTower(Vector2 positionMy) { // обноружение цели для башни - добавляем в очередь
        targetTreet.clear();
        for (Map.Entry<Integer, OpponentsTanks> tank : listOpponents.entrySet()) {
            if (!tank.getValue().isLive()) continue;
            if (myCommand == tank.getValue().getCommand()) continue;
            float l = tank.getValue().getPosition().cpy().sub(positionMy).len2();
            // System.out.println("myCommand " + myCommand + "  " + tank.getValue().getCommand());

            if (l < rast_to_target) {
                this.targetTreet.put(returnAngle(tank.getValue().getPosition(), myPosition), tank.getKey());
            }
        }
        //  System.out.println(targetTreet.size());
    }

    private Integer selectTarget() { // выбираем цель из списка
        if (targetTreet.isEmpty()) return null;
        if (target_tank >= targetTreet.size()) target_tank = 0;
        return (Integer) (targetTreet.values().toArray())[target_tank];
    }

    public void changeTarget() { //сменить цель
        int a = 0;
        if (targetTreet.size() < 2) return;
        int tnt = Integer.MIN_VALUE;
        if (nomTarget != null) tnt = nomTarget;
        int temp_nom_t = tnt;
        this.target_tank++;
        try {
            while (!nomTarget.equals(tnt)) {
                if (target_tank >= targetTreet.size()) target_tank = 0;
            }
            nomTarget = selectTarget();
        } catch (NullPointerException e) {
            //   e.printStackTrace();
        }
        if (temp_nom_t == nomTarget) {
            changeTarget(a);
        }
        ;
    }

    public void changeTarget(int a) {
        //  System.out.println("changeTarget " + a);
        a++;
        if (a < 3) changeTarget(a);
    }

    private boolean checkLen() { // проерка длинны
        return listOpponents.get(nomTarget).getPosition().cpy().sub(myPosition).len2() > rast_to_target * 1.3f;
    }

    private float returnAngle(Vector2 positionoOpponent, Vector2 positionMy) {
        return positionoOpponent.cpy().sub(positionMy).angleDeg();
    }

    public boolean isRotation() {
        return rotation;
    }

    public void setRotation(boolean rotation) {
        this.rotation = rotation;
    }

    public int getTargetSize() {
        return targetTreet.size();
    }

    public int getTarget_tank() {
        return target_tank;
    }

    public Integer getNomTarget() {
        // System.out.println(nomTarget);
        return nomTarget;
    }

    public boolean isRedyToAttac() {
        if (this.timerTackt >= 1f) {
            timerTackt = 0;
            return true;
        } else {
            return false;
        }
    }

    private boolean checkVisebleRadar(float x, float y) {
//        if(!MathUtils.isEqual(getPosition().x,x,450)) return false;
//        if(!MathUtils.isEqual(.getPosition().y,y,450)) return false;
        return true;
    }

    public int getAnTower() {
        return (int) direction_tower.angleDeg();
    }

}
