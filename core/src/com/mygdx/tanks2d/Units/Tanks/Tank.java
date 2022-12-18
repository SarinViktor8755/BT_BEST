package com.mygdx.tanks2d.Units.Tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.tanks2d.ClientNetWork.Heading_type;
import com.mygdx.tanks2d.ClientNetWork.ServiceClient;
import com.mygdx.tanks2d.Screens.Controll.Controller;
import com.mygdx.tanks2d.Screens.GamePlayScreen;

public class Tank {
    static final Vector2 DEATH_VECTOR = new Vector2(-1111, -1111);


    GamePlayScreen gsp;

    //Корпус
    Vector2 position; // позиция
    Vector2 direction; //направление корпуса
    Vector2 targetCoordinat; // цель коодинаты для прицела

    //Башня - Tower
    Vector2 direction_tower;//направление корпуса башни
    //////////
    SpriteBatch sb;
    Controller controller;

    Texture img, img_1, img2, imgB, imgB2, img_1B, imgr, body, towers;

    Texture target;
    Texture target_guidance;

    float raz;
    boolean rot;

    float deltaSled;
    Vector2 deltaSledVec;
    static Integer my_Command = generateCommand(); // по умолчанию 1 красня команда временно

    final float SPEED = 120f;
    final float SPEED_ROTATION = 180f;

    private TowerRotation tr;

    private int hp;

    private Vector2 point_respown_blue = new Vector2();
    private Vector2 point_respown_red = new Vector2();

    private float time_life = 0;
    private float g = 1;

    private boolean banner_feith;


//    private HashMap<Float, Integer> targetTreet; // цели - угол до цели - номер цели )))
//    private int target_tank;
//    private Integer nomTarget;

    public Tank(GamePlayScreen gsp) {
        banner_feith = true;
        time_life = 0;
        g = 1;
        deltaSledVec = new Vector2();
        this.gsp = gsp;
        position = new Vector2(MathUtils.random(0, gsp.getGameSpace().WITH_LOCATION), MathUtils.random(0, gsp.getGameSpace().HEIHT_LOCATION));
        direction = new Vector2(0, 1);
        direction.setToRandomDirection();
        hp = 100;

        direction_tower = new Vector2(0, 1);
        direction_tower.setAngleDeg(direction.angleDeg());
        //targetCoordinat = new Vector2()
        this.sb = gsp.getBatch();

///////////////////////////////////////


        img = gsp.getAMG().get("trb1.png");
        img2 = gsp.getAMG().get("trb2.png");

        img_1 = gsp.getAMG().get("tr.png");
//////////////
        imgB = gsp.getAMG().get("tbb1.png");
        imgB2 = gsp.getAMG().get("tbb2.png");

        img_1B = gsp.getAMG().get("tb.png");

        //target = new Texture(Gdx.files.internal("target.png"));
        target = gsp.getAMG().get("target.png", Texture.class);
        target_guidance = gsp.getAMG().get("target_guidance.png", Texture.class);
        //////////////////
        replace_textures();


        target.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);

        rot = true;
        deltaSled = 0;
        tr = new TowerRotation(direction, direction_tower, position, gsp.getTanksOther().listOpponents, getMy_Command());
        targetCoordinat = new Vector2(0, 0);


        //  gsp.getCameraGame().createNewTargetDeathRhim(gsp.getTanksOther().getRandomPlayer());

        this.point_respown_blue.set(gsp.getGameSpace().getRasp1());
        this.point_respown_red.set(gsp.getGameSpace().getRasp2());


        if (my_Command == Heading_type.RED_COMMAND) position.set(point_respown_red);
        if (my_Command == Heading_type.BLUE_COMMAND) position.set(point_respown_blue);
        position.x += MathUtils.random(-100, 100);
        position.y += MathUtils.random(-100, 100);
        deltaSledVec.set(this.getPosition());

    }

    public void respownTank(int command) {
        my_Command = command;
        hp = 100;
        time_life = 0;
        if (my_Command == Heading_type.BLUE_COMMAND) position.set(point_respown_blue);
        if (my_Command == Heading_type.RED_COMMAND) position.set(point_respown_red);
        position.set(position.x, position.y);
        position.x += MathUtils.random(-100, 100);
        position.y += MathUtils.random(-100, 100);

        // gsp.getMainGame().getGamePlayScreen().getAudioEngine().pley_fight_ad_sound();
    }

    public void respownTank() {
        hp = 100;
        time_life = 0;
        if (my_Command == Heading_type.BLUE_COMMAND) position.set(point_respown_blue);
        if (my_Command == Heading_type.RED_COMMAND) position.set(point_respown_red);
        position.set(position.x, position.y);
        position.x += MathUtils.random(-100, 100);
        position.y += MathUtils.random(-100, 100);
        banner_feith = true;


        //   controller.addBannerFeiath();
        //  gsp.getMainGame().getGamePlayScreen().getAudioEngine().pley_fight_ad_sound();
    }

    public static Integer getMy_Command() {
        return my_Command;
    }

    public static void setMy_Command(Integer my_Command) {
        Tank.my_Command = my_Command;
    }

    public int getHp() {
        return hp;
    }

    public TowerRotation getTr() {
        return tr;
    }

    private void flashing_tank() {
        time_life += Gdx.graphics.getDeltaTime();
        if (time_life < 3) g = MathUtils.sin(time_life * 28);
        else g = 1;


//        try {
//         if(time_life > 2)   controller.addBannerFeiath();
//        }catch (NullPointerException e){}

    }

    public void update(Vector2 directionMovementControll, boolean inTuch) {
//       if(MathUtils.randomBoolean(.005f)) {
//           System.out.println("cleeeeeeeeeeeeeeeeer");
//           gsp.getTanksOther().listOpponents.clear();
//       }
        upDateHpHud();
        tr.update(Gdx.graphics.getDeltaTime());
        if (this.tr.getTargetSize() > 1) gsp.getController().setButtonChangingOpponent(true);
        else gsp.getController().setButtonChangingOpponent(false);
        //if (MathUtils.randomBoolean(.05f)) hp--;


        getTargetCamera();
        getTargetCamera(directionMovementControll);
        banner_feith();

        //  if (!inTuch) return;

        if (isLive()) {
            //System.out.println(direction.clamp(SPEED,SPEED).len());
            moveMainTank(directionMovementControll, inTuch);
            generatorSled();
            tower_sec(); // работа башни
            send_my_coordinat(); // передачакоординат
            flashing_tank();
            generatorSmoke();
        } else this.position.set(DEATH_VECTOR);
        ////////////////////////////////////
        // gsp.getGameSpace().getLighting().setLasetOn(false);
///////////////////////////////////////
    }

    private void banner_feith() {
        if (banner_feith && time_life > .7f) {
            banner_feith = false;
            gsp.getController().addBannerFeiath();
        }
    }

    private void generatorSled() {
        if (MathUtils.randomBoolean(.4f))
            if (Vector2.dst2(position.x, position.y, deltaSledVec.x, deltaSledVec.y) > 200) {
                deltaSledVec.set(getPosition());
                gsp.getGameSpace().addSled(position.x + direction.x * 3, position.y + direction.y * 3, direction.angleDeg());
                // gsp.getMainGame().audioEngine.pley_pip_1();
            }
    }

    public static int generateCommand() {
        if (MathUtils.randomBoolean()) return Heading_type.RED_COMMAND;
        else return Heading_type.BLUE_COMMAND;
    }


    private void moveMainTank(Vector2 directionMovementControll, boolean inTuch) { // движние основного танка
        rotation_the_tower(directionMovementControll);
        if (inTuch) {
            this.position.add(direction.nor().scl(SPEED).scl(Gdx.graphics.getDeltaTime()));
            gsp.getGameSpace().checkMapBordersReturnSpaceTank(getPosition());
            collisinRectangleTrue();
            collisinCircleTrue();
            replace_textures();
            collisinOtherTanksTrue();
        }



        /////////////////////////////////////////////////////////

        //  System.out.println(direction.len2());
///////////////////
    }

    private void tower_sec() {
        try {
            if (this.tr.getNomTarget() != null) {
                targetCoordinat = gsp.getTanksOther().getTankForID(this.tr.getNomTarget()).getPosition();
                // gsp.getGameSpace().getLighting().setLasetOn(true);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private void send_my_coordinat() {
        //  System.out.println(getPosition());
        //   ServiceClient.sendMuCoordinat(getPosition().x, getPosition().y, getTr().getAnTower(), gsp.getMainGame().getMainClient().getClient());
        gsp.getMainGame().getMainClient().getNetworkPacketStock().sendMuCoordinat(position.x,position.y,getTr().getAnTower());
    }


    private void rotation_the_tower(Vector2 directionMovementControll) {
        raz = Math.abs(direction.angleDeg() - directionMovementControll.angleDeg());
        if (raz > 10) // поворот башни
            if ((directionMovementControll.angleDeg(direction) > 180)) {
                this.direction.setAngleDeg(direction.angleDeg() - Gdx.graphics.getDeltaTime() * SPEED_ROTATION);
            } else {
                this.direction.setAngleDeg(direction.angleDeg() + Gdx.graphics.getDeltaTime() * SPEED_ROTATION);
            }
    }

    /////////////////////////////////////////collisin
    public void collisinRectangleTrue() {
        //Vector2 c = gsp.getGameSpace().getMainCollision().isCollisionsRectangle(getPosition());
        if (gsp.getGameSpace().getMainCollision().isCollisionsRectangle(getPosition()) != null)
            gsp.getGameSpace().getMainCollision().isCollisionsRectangleReturnPosition(position);

    }

    public void collisinCircleTrue() {
        Vector2 c = gsp.getGameSpace().getMainCollision().isCircleCircle(getPosition());
        if (c != null) position.add(c.scl(SPEED * Gdx.graphics.getDeltaTime()));

    }

    private void collisinOtherTanksTrue() {
        Vector2 ct = gsp.getTanksOther().isCollisionsTanks(position);
        if (ct != null) {  // танки другие
            position.sub(direction.cpy().nor().scl(Gdx.graphics.getDeltaTime() * 90)); // тут вроде норм
            //System.out.println("isCollisionsTanks");
        }
    }

    /////////////////////////////////////////////////
    private void generatorSmoke() { // генератор Дыма для танка
        gsp.pc.generatorSmoke(hp, position.x, position.y);
    }

    private void upDateHpHud() {

        gsp.getController().setHPHeroTank(this.hp);
//        gsp.getController().setBlueCommand(gsp.getScore_blue_command());
//        gsp.getController().setRedCommand(gsp.getScore_red_command());
        //  System.out.println("blue "+ gsp.getScore_blue_command()+ " red "+ gsp.getScore_red_command());
        gsp.getController().setFb(gsp.getLive_blue_command());
        gsp.getController().setFr(gsp.getLive_red_command());

    }


    private void replace_textures() {       // определение текстур для команды
        if (getMy_Command() == Heading_type.BLUE_COMMAND) { // синяя команда
            towers = img_1B;
            if (MathUtils.sin(gsp.getTimeInGame() * 50) >= 0) body = imgB;
            else body = imgB2;
        } else {                                              // красная  команда
            towers = img_1;
            if (MathUtils.sin(gsp.getTimeInGame() * 50) >= 0) body = img;
            else body = img2;
        }
    }


    public void renderTank(Vector2 directionMovement, boolean inTouch) {
        if (tr.isRotation()) gsp.getAudioEngine().pleySoundOfTower();
        else gsp.getAudioEngine().stopSoundOfTower(); // звук башни
        tr.setRotation(false);

        update(directionMovement, inTouch);
        //  System.out.println("coommand: " + getMy_Command());
        //   if (MathUtils.randomBoolean(0.2f)) command = MathUtils.random(0, 3);


        sb.setColor(0,0,0,.5f);
        sb.draw(body,  // тень тела
                position.x - 17, position.y - 17,
                20, 20,
                40, 40,
                1, 1,
                direction.angleDeg() + 180,
                0, 0,
                img.getWidth(), img.getHeight(),
                true, false);
        sb.draw(towers,
                position.x - 20, position.y - 20,
                20, 20,
                40, 40,
                1, 1,
                direction_tower.angleDeg() + 180,
                0, 0,
                img.getWidth(), img.getHeight(),
                false, false);

        sb.setColor(g + 1, g, 1, 1);
        sb.draw(body,
                position.x - 20, position.y - 20,
                20, 20,
                40, 40,
                1, 1,
                direction.angleDeg() + 180,
                0, 0,
                img.getWidth(), img.getHeight(),
                true, false);



        sb.setColor(0,0,0,.5f);
        sb.draw(towers,
                position.x - 16, position.y - 16,
                16, 16,
                40, 40,
                1, 1,
                direction_tower.angleDeg() + 180,
                0, 0,
                img.getWidth(), img.getHeight(),
                false, false);
        sb.setColor(g + 1, g, 1, 1);
        sb.draw(towers,
                position.x - 20, position.y - 20,
                20, 20,
                40, 40,
                1, 1,
                direction_tower.angleDeg() + 180,
                0, 0,
                img.getWidth(), img.getHeight(),
                false, false);




        // System.out.println(this.tr.getNomTarget());

        if (tr.getNomTarget() != null) {
            float ag = Interpolation.fade.apply(getTime_Tackt());
            sb.setColor(ag, 1 - ag, ag, .4f);
            sb.draw(target,
                    targetCoordinat.x - 35, targetCoordinat.y - 35,
                    35, 35,
                    70, 70,
                    1.1f + (MathUtils.cos(gsp.getTimeInGame() * 5) / 5), 1.1f + (MathUtils.cos(gsp.getTimeInGame() * 5) / 5),
                    (Interpolation.swing.apply(MathUtils.sin(gsp.getTimeInGame() / 4)) + 1) * 45,
                    1, 1,
                    80, 80,
                    false, false);


            if (tr.isGuidance()) {
                sb.setColor(.6f, 1, .6f, .2f);
                sb.draw(target_guidance,
                        targetCoordinat.x - 40, targetCoordinat.y - 40, 80, 80
                );

            }
            sb.setColor(1, 1, 1, 1);
        }
    }

    public float returnAngle(Vector2 position) {
        return position.cpy().sub(this.position).angleDeg();
    }

    public GamePlayScreen getGsp() {
        return gsp;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public Vector2 getDirection_tower() {
        return direction_tower;
    }

    public void getTargetCamera() {
        //directionMovementControll
        Vector2 temp = position.cpy().sub(direction_tower.cpy().scl(-160));
        if (isLive()) {
            gsp.getCameraGame().moveFloatCameraToPoint(temp.x, temp.y, (int) 3.5); //камера перемещение
            // куда пееремещать
        } else {
            gsp.getCameraGame().deathStatus(this);
        }

//        gsp.getGameSpace().getLighting().setPointL(position.x, position.y); //освещение перемещение
//        gsp.getGameSpace().getLighting().setCone(position.x, position.y, direction.angleDeg());
        //   gsp.getGameSpace().getLighting().setConeTower(position.x, position.y, direction_tower.angleDeg());
    }

    public void getTargetCamera(Vector2 t) {
        //directionMovementControll
        Vector2 temp = position.cpy().sub(t.cpy().nor().scl(-160));
        //  gsp.getCameraGame().moveFloatCameraToPoint(temp.x, temp.y, (int) 3.5); //камера перемещение
//        gsp.getGameSpace().getLighting().setPointL(position.x, position.y); //освещение перемещение
//        gsp.getGameSpace().getLighting().setCone(position.x, position.y, direction.angleDeg());
        //   gsp.getGameSpace().getLighting().setConeTower(position.x, position.y, direction_tower.angleDeg());
    }

    public boolean redyToAttack() {
        return getTr().isRedyToAttac();
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public boolean isLive() {
        //  System.out.println(hp + "  hp");
        if (hp > 0) return true;
        return false;
    }

    public float getTime_Tackt() {
        return tr.getTimerTackt();
    }
}