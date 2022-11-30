package main.java.com.Units.ListPlayer;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.tanks2d.ClientNetWork.Heading_type;
import com.mygdx.tanks2d.Utils.VectorUtils;

public class Player {


    public  static Vector2 DISCONECT_SYS_LAYER = new Vector2(-10000, -10000);
    int status;

    Vector2 pos;
    Vector2 body_rotation;

    float rotTower; // коодинаты
    int hp, command, id; //ХП

    String tokken, nikName;

    public Player(int id) {
        this.id = id;
        hp = 100;
        this.command = 0;
        nikName = Heading_type.DEFULT_NAME; // от куда он берется - это не понятно
        pos = new Vector2(StatusPlayer.IN_MENU, StatusPlayer.IN_MENU); // если -999 - знаит ненажал кнопчку старт 998 нажал -y это счетчик на время смерти ))
        body_rotation = new Vector2(1, 1);
        status = StatusPlayer.IN_MENU;
//        if (id < -99) {
//            System.out.println(
//                    "create " + id + "  " + getNikName());
//            new Exception().printStackTrace();
//        }
    }

    public Player(int id, int command,String tokken) {
        this.id = id;
        hp = 100;
        this.command = command;
        nikName = Heading_type.DEFULT_NAME; // от куда он берется - это не понятно
        pos = new Vector2(StatusPlayer.IN_MENU, StatusPlayer.IN_MENU); // если -999 - знаит ненажал кнопчку старт 998 нажал -y это счетчик на время смерти ))
        body_rotation = new Vector2(1, 1);
        status = StatusPlayer.IN_MENU;
        this.tokken = tokken;


//        if (id < -99) {
//            System.out.println(
//                    "create " + id + "  " + getNikName());
//            new Exception().printStackTrace();
//        }
    }

    public Player(int id, int command, boolean bot) {//bot
        this.id = id;
        hp = -1;
        this.command = command;
        nikName = Heading_type.DEFULT_NAME; // от куда он берется - это не понятно
        pos = new Vector2(StatusPlayer.IN_MENU, StatusPlayer.IN_MENU); // если -999 - знаит ненажал кнопчку старт 998 нажал -y это счетчик на время смерти ))
        body_rotation = new Vector2(1, 1);
        status = StatusPlayer.IN_MENU;
        tokken = id+":bot_";
//        if (id < -99) {
//            System.out.println(
//                    "create " + id + "  " + getNikName());
//            new Exception().printStackTrace();
//        }
    }

    public void setPosition(Vector2 p) {
        //  System.out.println(p);

        this.pos.set(p);
    }

    public void setPosition(float x, float y) {
        this.pos.set(x, y);
    }

    public Vector2 getPosi() {
        return pos;
    }

    public Vector2 getBody_rotation() {
        return body_rotation;
    }

    public void setBody_rotation(Vector2 body_rotation) {
        this.body_rotation = body_rotation;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public void setR(float r) {
        this.body_rotation.setAngleDeg(r);
//        if (this.r > 360) this.r = 0;
//        if (this.r < 0) this.r = 360;
    }

    public float getRotTower() {
        return rotTower;
    }

    public void setRotTower(float rotTower) {
        this.rotTower = rotTower;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTokken() {
        return tokken;
    }

    public void setTokken(String tokken) {
        this.tokken = tokken;
    }

    public String getNikName() {
        // System.out.println(this);
        return nikName;
    }

    public void setNikName(String nikName) {
        //   System.out.println(nikName + "   " + this.id + "!!!!!!!!!!!!!!! setNikName");
        this.nikName = nikName;

    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public int minusHP(int minus) {
        this.hp -= minus;
        MathUtils.clamp(hp, 0, 100);
        return this.hp;
    }

    @Override
    public String toString() {
        return
                "st=" + status +
                "p=" +  getPositionForPrint() +
                "br=" + body_rotation +
                "rt=" + rotTower +
                "hp=" + hp +
                "c" + command +
                "id=" + id +
                "to'" + tokken + '\'' +
                "nik" + nikName + '\'' +
                '|';
    }

    private String getPositionForPrint(){
        if(pos.x == -100000) return "DEATH"; else return pos.toString();
    }

    public boolean isCollisionsTanks(Vector2 posTank) {
        float len = VectorUtils.getLen2(posTank, this.pos);
        if (len > 5 && len < 1300) return true;
        return false;
    }

    public boolean isLive() {
        if (hp > 0) return true;
        return false;
    }

    public boolean isClickButtonStart() {
        if (getPosi().x == -999) return false;
     //   if (getStatus() == StatusPlayer.IN_MENU) return false;
        else return true;
    }

    public boolean in_game_player() {
        if (id > -99) return true;
        if (status == Heading_type.DISCONECT_PLAYER) return false;
        //  if (status == Heading_type.DISCONECT_PLAYER) return false;
        if (pos.x == StatusPlayer.IN_MENU) return false;
        //  if (pos.x == StatusPlayer.IN_MENU) return false;
        if (nikName.equals(Heading_type.DEFULT_NAME)) return false;

        return true;
    }
//    public boolean in_game_player() {
//        if (id < -99) return false;
//        // System.out.println("-");
//        if (status == Heading_type.DISCONECT_PLAYER) return false;
//        // System.out.println("--");
//        if (pos.x == StatusPlayer.IN_MENU) return false;
//        // System.out.println("-_-");
//        if (pos.y == StatusPlayer.IN_MENU) return false;
//        //  System.out.println("-_--");
//        if (nikName.equals("tokken_123")) return false;
//        //  System.out.println("-_-->>>");
//        return true;
//    }

}
