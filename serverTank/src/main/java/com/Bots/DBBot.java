package main.java.com.Bots;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class DBBot {

    private int id;

    private String nikName;

    private float timerShoot; // время с последней аттаки

    //////////////////////Target

    private Vector2 target_position;

    private Vector2 target_body_rotation_angle; // направление тела
    private Vector2 target_angle_rotation_tower; // направление башни

    public static Vector2 etalon_target = new Vector2(-1000, -1000);


    private Vector2 myPosition; // позиция

    private Integer nomTarget;
    private int target_tank;
    private float targetAlign; // целивой угол

    private float time_tackt_attack;
    //////////////////////////////
    private Vector2 globalTarget; // шлобальная цель
    private int nom_strategy;
    private float time_to_operation;

    public DBBot(int id) {
        time_to_operation = 0;
        this.id = id;
        target_position = new Vector2(0, 0);
        target_body_rotation_angle = new Vector2(1, 0);
        target_angle_rotation_tower = new Vector2(target_body_rotation_angle);

        targetAlign = target_angle_rotation_tower.angleDeg();
        nomTarget = null;
        target_tank = 0;

        time_tackt_attack = 0;
        /////////////////////////
        globalTarget = new Vector2(500, 500);
        nom_strategy = 0;


    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNikName() {
        return nikName;
    }

    public void setNikName(String nikName) {
        this.nikName = nikName;
    }

    public float getTimerShoot() {
        return timerShoot;
    }

    public void setTimerShoot(float timerShoot) {
        this.timerShoot = timerShoot;
    }

    public void setGlobalTarget(Vector2 globalTarget) {
        this.globalTarget = globalTarget;
    }

    public Vector2 getTarget_position() {
        return target_position;
    }

    public void setTarget_position(Vector2 target_position) {
        this.target_position = target_position;
    }

    public Vector2 getTarget_body_rotation_angle() {
        return target_body_rotation_angle;
    }

    public Vector2 getGlobalTarget() {
        return globalTarget;
    }

    public void setTarget_body_rotation_angle(Vector2 target_body_rotation_angle) {
        this.target_body_rotation_angle = target_body_rotation_angle;
    }

    public void setTarget_body_rotation_angle(float l) {

        this.target_body_rotation_angle = target_body_rotation_angle.setAngleDeg(l);
    }

    public boolean isRedyToAttac() {
      //  System.out.println(time_tackt_attack);
        if (this.time_tackt_attack >= 1f) {
            time_tackt_attack = 0;
            return true;
        } else return false;
    }

    public void updateTackAttack(float dt) {
        this.time_tackt_attack += dt;
        this.time_to_operation -= dt;
    }

    public Vector2 getTarget_angle_rotation_tower() {
        return target_angle_rotation_tower;
    }

    public void setTarget_angle_rotation_tower(Vector2 target_angle_rotation_tower) {
        this.target_angle_rotation_tower = target_angle_rotation_tower;
    }

    public void setTarget_angle_rotation_tower(float angel) {
        this.target_angle_rotation_tower.setAngleDeg(angel);
    }


    public Vector2 getMyPosition() {
        return myPosition;
    }

    public void setMyPosition(Vector2 myPosition) {
        this.myPosition = myPosition;
    }

    public Integer getNomTarget() {
        return nomTarget;
    }

    public void setNomTarget(Integer nomTarget) {
        this.nomTarget = nomTarget;
    }

    public int getTarget_tank() {
        return target_tank;
    }

    public void setTarget_tank(int target_tank) {
        this.target_tank = target_tank;
    }

    public float getTargetAlign() {
        targetAlign = MathUtils.random(0, 350);
        return targetAlign;
    }

    public void setTargetAlign(float targetAlign) {
        this.targetAlign = targetAlign;
    }

    public void uodate_time_tackt_attack(float dt) {
        this.time_tackt_attack = time_tackt_attack + dt;
    }

    public boolean is_redy_move() {
        // System.out.println(time_tackt_attack);
        if (this.time_tackt_attack > 0) return true;
        return false;
    }


    public float getTime_to_operation() {
        return time_to_operation;
    }

    public void setTime_to_operation(float time_to_operation) {
        this.time_to_operation = time_to_operation;
    }

    public boolean isFreeForOperation() {
        if (time_to_operation <= 0) {
            return true;
        }
        return false;
    }

    public void update_time_to_operation(float dt) {
        this.time_to_operation -= dt;
    }
}