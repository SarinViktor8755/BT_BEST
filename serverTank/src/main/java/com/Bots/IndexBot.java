package main.java.com.Bots;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.tanks2d.ClientNetWork.Heading_type;
import com.mygdx.tanks2d.ClientNetWork.Network;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import main.java.com.GameServer;
import main.java.com.MainGame;
import main.java.com.Units.ListPlayer.ListPlayers;
import main.java.com.Units.ListPlayer.Player;
import main.java.com.Units.ListPlayer.StatisticMath;

public class IndexBot extends Thread {
    public static GameServer gs;
    ConcurrentHashMap<Integer, DBBot> dbBots;

    final static float SPEED = 120f;
    final static float SPEED_ROTATION = 180f;
    final static float SPEED_BULLET = 700;


    private static BehaviourBot botBehavior;

    private static int NOM_ID_BOT = -100;

    private final Vector2 speed_constanta = new Vector2(90, 0);

    private Vector2 temp_position_vector;
    private static Vector2 temp_position_vector_static;
    private int countBot; // порядковый номер ботов


    //   private int sizeBot = 5;

    private TowerRotationLogic tr;


    public IndexBot(GameServer gameServer, int number_bots) {
        this.countBot = number_bots;
        this.temp_position_vector = new Vector2();

        this.gs = gameServer;
        this.dbBots = new ConcurrentHashMap<>();
        //  this.sizeBot = number_bots;
        this.tr = new TowerRotationLogic();
        // this.botBehavior = new BotBehavior(botList); // поведение бота - тут вся логика  )))
        // this.allPlayers = new HashMap<Integer, TowerRotation>();

        //      System.out.println("install_Bot : " + GameServer.getDate() + "  " + countBot);
    }


    private void addBot() {

        System.out.println("Add_bot");
        int command = gs.getMainGame().getIndexMath().getCommand();
        Player p = new Player(NOM_ID_BOT, command,true);
        //     System.out.println(command);

        p.setHp(100);
        p.setNikName(getNikNameGen());

        NOM_ID_BOT--;
        //    System.out.println("add_bot+ : " + NOM_ID_BOT + "  " + p.getCommand());

        gs.getLp().addPlayer(p); // добавляем в базу играков

        DBBot bot = new DBBot(p.getId());
        dbBots.put(p.getId(), bot);
        //  p.setCommand(gs.getMainGame().getIndexMath().getCommand());

        if (p.getCommand() == Heading_type.RED_COMMAND)
            p.setPosition(gs.getMainGame().getMapSpace().getRasp2());
        else p.setPosition(gs.getMainGame().getMapSpace().getRasp1());
        //  p.setPosition(MathUtils.random(200, 1100), MathUtils.random(200, 1100));
        StatisticMath.setTrue();
    }

    public void updaeteBot(float deltaTime) {
        if(MainGame.isPause()) return;
        if(!gs.isServerLivePlayer()) return;
        //  if(MainGame.isPause()) return;

        actionBot(deltaTime);
        send_bot_coordinates();
    }


    private void attackBot(DBBot dbtank, float deltaTime, Player tank) {

        dbtank.updateTackAttack(deltaTime);
        if (MathUtils.randomBoolean(0.7f)) return;
        if (!tank.isLive()) return;

        if (!dbtank.isRedyToAttac()) return;
        if (dbtank.getNomTarget() == null) return;
        botShoot(tank.getId());
    }

    //////////////////////////////
    public void actionBot(float deltaTime) { // перемещения поля
        Iterator<Map.Entry<Integer, DBBot>> entries = dbBots.entrySet().iterator();
        while (entries.hasNext()) {
            try {
                Map.Entry<Integer, DBBot> entry = entries.next();
                DBBot tank = entry.getValue();

                Player p = gs.getLp().getPlayerForId(tank.getId());
                if (!p.isLive()) {
                    p.setPosition(-100_000, -100_000);
                    continue;
                }

                //respaunBot(p);

                gs.getMainGame().getMapSpace().resolving_conflict_with_objects(p.getPosi(), deltaTime); /// проверка столкновений с обьектами


                //   System.out.println(entry.getValue().getId() + " --");


                collisinOtherTanksTrue(p.getPosi(), deltaTime, p.getBody_rotation()); /// calisiion tanks
                gs.getMainGame().getMapSpace().returnToSpace(p.getPosi());

                // if(MathUtils.randomBoolean(.005f))tank.getValue().getTarget_body_rotation_angle().setAngleDeg(MathUtils.random(-180,180));
                TowerRotationLogic.updateTowerRotation(deltaTime, tank, p, gs.getLp()); /// поворот башни
                ///////
                attackBot(tank, deltaTime, p);/// атака бота
                moveBot(deltaTime, tank, p, gs.getLp());
            } catch (ConcurrentModificationException e) {
                e.printStackTrace();
            }
        }

        //System.out.println(gs.getLp());

    }

    private void checkBotDisconect() { // соотношение с с основнйо базой - проверка Жи ли бот

    }

    public void send_bot_coordinates() {
        gs.getLp().send_bot_coordinates();
    }


    private void moveBot(float deltaTime, DBBot tank, Player p, ListPlayers lp) {
        //isPointInCollision
        boolean r = rotation_body(deltaTime, tank, p.getBody_rotation()); // поворот туловеща
        // tank.getTarget_body_rotation_angle().nor().scl(MathUtils.random(50, 80));
//

        updateBaseTarget(p, tank, r); // обовление базовой цели
        go_to_tarpent_point(p, tank, r); // движение к точки цели
        moving_away_from_tanks(p, tank, r); /// обход других танков
        go_around_an_obstacle(tank, p); /// обход препядствий


        p.getPosi().sub(p.getBody_rotation().cpy().nor().scl(deltaTime * SPEED)); /// перемещение танка
        // перемещеени вперед


    }

    private void updateBaseTarget(Player p, DBBot tank, boolean r) {
        // if(MathUtils.randomBoolean(.9f)) return;
        try {
            if (tank.getNomTarget() != null && MathUtils.randomBoolean(.1f)) {
                tank.setGlobalTarget(gs.getLp().getPlayerForId(tank.getNomTarget()).getPosi().cpy().add(MathUtils.random(-150, +150), MathUtils.random(-150, +150)));
                //  System.out.println("11__");
            } else {
//                if (MathUtils.randomBoolean(.05f)) return;
//                //  tank.getGlobalTarget().set(500, 500);
//
////
//                if(p.getCommand()== Heading_type.RED_COMMAND)tank.getGlobalTarget().set(gs.getMainGame().getMapSpace().getRasp1());
//                if(p.getCommand()== Heading_type.BLUE_COMMAND)tank.getGlobalTarget().set(gs.getMainGame().getMapSpace().getRasp2());

                if (p.getCommand() == Heading_type.RED_COMMAND)
                    tank.getGlobalTarget().set(ListPlayers.getBlue_average());
                if (p.getCommand() == Heading_type.BLUE_COMMAND)
                    tank.getGlobalTarget().set(ListPlayers.getRed_average());


                tank.getGlobalTarget().set(ListPlayers.getAverage_cord());
                // System.out.println("222");
            }
        } catch (NullPointerException e) {

            e.printStackTrace();
        }
    }

    private void moving_away_from_tanks(Player p, DBBot tank, boolean r) { // уход от других танков
        if (MathUtils.randomBoolean(.55f)) return;
        Vector2 away_tank = gs.getLp().search_for_nearest_tank(p.getPosi());
        // System.out.println("at" + away_tank);
        if (away_tank == null) return;
        if (!r) return;
        // Vector2 stick = get_vector_from_players_position(tank.getTarget_body_rotation_angle(), p).scl(-1);
        tank.getTarget_body_rotation_angle().set(p.getPosi().cpy().sub(away_tank).scl(-1)).rotateDeg(MathUtils.random(-30, 30));
        tank.setTime_to_operation(MathUtils.random(.5f, 5));

    }

    private void go_around_an_obstacle(DBBot tank, Player p) { // обход припятсвий
        //    Vector2 stick = get_vector_from_players_position(p.getBody_rotation(), p).nor().scl(-50);
        if (MathUtils.randomBoolean(.5f)) return;
        Vector2 rot = temp_position_vector.set(1, 0).setAngleDeg(tank.getTarget_body_rotation_angle().angleDeg()).scl(MathUtils.random(70, 100));
        Vector2 stick = p.getPosi().cpy().sub(rot);


        //   System.out.println("space: "+!gs.getMainGame().getMapSpace().isPointWithinMmap(stick) + "__calision "+ gs.getMainGame().getMapSpace().isPointInCollision(stick.x, stick.y));
        if (!gs.getMainGame().getMapSpace().isPointWithinMmap(stick) || gs.getMainGame().getMapSpace().isPointInCollision(stick.x, stick.y)) {
            if (MathUtils.randomBoolean()) {
                tank.getTarget_body_rotation_angle().rotateDeg(MathUtils.random(25, 100));
            } else {
                tank.getTarget_body_rotation_angle().rotateDeg(MathUtils.random(-100, -25));
            }

            tank.setTime_to_operation(MathUtils.random(.1f, 6));
        }

    }

    private void go_to_tarpent_point(Player p, DBBot tank, boolean key) { // двигаться к целевой точке
        if (!tank.isFreeForOperation()) return;
        if (tank.getGlobalTarget().equals(DBBot.etalon_target)) return;
        if (!tank.is_redy_move()) return;
        //  if (!key) return;
        // System.out.println("point");
        tank.getTarget_body_rotation_angle().set(p.getPosi().cpy().sub(tank.getGlobalTarget()));

    }


    private static boolean rotation_body(float dt, DBBot db_bot, Vector2 rotaton) { // поворот тела
        boolean a = MathUtils.isEqual(rotaton.angleDeg(), db_bot.getTarget_body_rotation_angle().angleDeg(), 4);
        if (!a) {
            if ((rotaton.angleDeg(db_bot.getTarget_body_rotation_angle()) > 180)) {
                rotaton.rotateDeg(SPEED_ROTATION * dt);
            } else rotaton.rotateDeg(-SPEED_ROTATION * dt);
        }
        return a;
    }

    public void respaunBot(Player p) {
        //  if (!p.isLive()) {
        //     if (MathUtils.randomBoolean(0.05f)) {
        p.setHp(100);
        if (p.getCommand() == Heading_type.RED_COMMAND) {
            p.setPosition(gs.getMainGame().getMapSpace().getRasp2().cpy());
            p.getPosi().y += MathUtils.random(300);
        } else if (p.getCommand() == Heading_type.BLUE_COMMAND) {
            p.setPosition(gs.getMainGame().getMapSpace().getRasp1());
            p.getPosi().y += MathUtils.random(300);
        }
        // System.out.println("----------");
        gs.send_PARAMETERS_PLAYER(p);
        //      }
        //  }
    }

    private void collisinOtherTanksTrue(Vector2 position, float dt, Vector2 rotation) {
        Vector2 ct = gs.getLp().isCollisionsTanks(position);
        if (ct != null) {  // танки другие
            position.sub(rotation.cpy().scl(dt * 90 * -2.5f)); // !!!!!! тут вроде норм (кализия танков поправить проблема в колизии сдругими живыми игроками)
        }
    }


    /////////////////////////////////////////////////


    public void updateCountBot(int lPlayers, int target_plaers) {

        if (StatisticMath.getPlayersSize() < target_plaers) addBot();
        if (StatisticMath.getPlayersSize() > target_plaers + 1) remove_extra_bot();

        //if (gs.lp.get_activ_player_bots() == target_plaers) return;
        //     if (gs.lp.get_activ_player_bots() < target_plaers) addBot();
        // else delBot();

        //    if(gs.lp.get_activ_player_bots() > target_plaers) delateBot();
    }


    public static void botShoot(int id) { /// выстрел LAVEL_1
        Player p = gs.getLp().getPlayerForId(id);
        Player bot = gs.getLp().getPlayerForId(id);
        Vector2 velBullet = new Vector2(SPEED_BULLET, 0).setAngleDeg(bot.getRotTower());

        Network.StockMessOut sm = new Network.StockMessOut();

        //   IndexBot.temp_position_vector_static.setAngleDeg(p.getRotTower());

        Vector2 rot = new Vector2(1, 0).setAngleDeg(p.getRotTower()).scl(-30);
        Vector2 smooke = p.getPosi().cpy().sub(rot);

        int n = 5000 + MathUtils.random(99999999);
        sm.p1 = smooke.x;
        sm.p2 = smooke.y;
        sm.p3 = p.getRotTower();
        sm.p4 = n;
        sm.tip = Heading_type.MY_SHOT;

        gs.getMainGame().getBullets().addBullet(new Vector2(bot.getPosi().x, bot.getPosi().y), velBullet, n, bot.getId());
        gs.sendToAllTCP_in_game(sm);
    }


    private void remove_extra_bot(){
      //  if(MathUtils.randomBoolean(.99f)) return;
        System.out.println("delete");
        int random_id = gs.lp.getIdRandomBot();
        if(random_id == 99) return;
        delateBot(random_id);
        System.out.println(random_id+"   del");

        ///////////////////////

        int target_comand = 0;
        if(StatisticMath.getBlueSize() > StatisticMath.getRedSize()) target_comand = Heading_type.BLUE_COMMAND;
        if(StatisticMath.getBlueSize() < StatisticMath.getRedSize()) target_comand = Heading_type.RED_COMMAND;
        if(target_comand == 0) return;

    }

    public void clearAllBot(){ /// чистить всех ботов - это для меню
        System.out.println("clearAllBot");
        for (Map.Entry<Integer, Player> entry : gs.lp.getPlayers().entrySet()) {
            //  System.out.println("ID =  " + entry.getKey() + " День недели = " + entry.getValue());
            if(!isBot(entry.getValue())) continue;
            System.out.println(entry.getValue().getId());

            int id = entry.getValue().getId();
            gs.lp.remove_player(id);
            dbBots.remove(id);
          //  gs.send_DISCONECT_PLAYER(id);
        }

    }

    private boolean isBot(Player p){
        if(p.getId()<-99) return true; return false;
    }

    private void delateBot(int id) { // дописать нужно с какой команды удалять ))

        gs.send_DISCONECT_PLAYER(id);
        gs.lp.remove_player(id);
        dbBots.remove(id);
        //System.out.println();

////        Integer firstKey = dbBots.keySet().iterator().next();
////        System.out.println("delete");
////        gs.lp.remove_player(firstKey);
////        dbBots.remove(firstKey);
////        gs.send_DISCONECT_PLAYER(firstKey);
//        if (gs.lp.getBlue_size() <1) return;
//    //    System.out.println("DELETA BOT");
//        int target_conmand;
//       // if()
//        if (gs.lp.getRed_size() < gs.lp.getBlue_size()) {
//            // System.out.println("RED_COMMAND");
//            target_conmand = Heading_type.BLUE_COMMAND;
//        }else
//        if (gs.lp.getBlue_size() < gs.lp.getRed_size()) {
//            //System.out.println("BLUE COMAND");
//            target_conmand = Heading_type.RED_COMMAND;
//        }else
//        if (MathUtils.randomBoolean()) target_conmand = Heading_type.RED_COMMAND;
//        else target_conmand = Heading_type.BLUE_COMMAND;
//
//        Iterator<Map.Entry<Integer, DBBot>> inter_bot = dbBots.entrySet().iterator();
//        Player p;
//        while (inter_bot.hasNext()) {
//            Map.Entry<Integer, DBBot> bot = inter_bot.next();
//            p = gs.lp.getPlayerForId(bot.getKey());
//            if(p.getCommand()== target_conmand);
//            {
//                int id = bot.getKey();
//                System.out.println("DELATE: " + id);
//                gs.send_DISCONECT_PLAYER(id);
//                gs.lp.remove_player(id);
//                dbBots.remove(id);
//
//            }
//
//        }

    }


    static String getNikNameGen() {
        ArrayList<String> names = new ArrayList<>();
        names.add("Bubba");
        names.add("Honey");
        names.add("Bo");
        names.add("Sugar");
        names.add("Doll");
        names.add("Peach");
        names.add("Snookums");
        names.add("Queen");
        names.add("Ace");
        names.add("Punk");
        names.add("Sugar");
        names.add("Gump");
        names.add("Rapunzel");
        names.add("Teeny");
        names.add("MixFix");
        names.add("BladeMight");
        names.add("Rubogen");
        names.add("Lucky");
        names.add("Tailer");
        names.add("IceOne");
        names.add("Sugar");
        names.add("Gump");
        names.add("Rapunzel");
        names.add("Teeny");
        names.add("MixFix");
        names.add("BladeMight");
        names.add("Rubogen");
        names.add("Lucky");
        names.add("Tailer");
        names.add("IceOne");
        names.add("TrubochKa");
        names.add("HihsheCKA");
        names.add("R2-D2");
        names.add("Breha Organa");
        names.add("Yoda");
        names.add("Obi-Wan Kenob");
        names.add("C-3PO");
        names.add("Darth Sidious");
        names.add("Darth Vader");
        names.add("Boba Fett");
        names.add("Sarin");
        names.add("Sasha");


        names.add("Buddy");
        names.add("King");
        names.add("Champ");
        names.add("Bro");
        names.add("Bubba");
        names.add("Tiny");
        names.add("Sport");
        names.add("Mouse");
        names.add("Bug");

        names.add("Oldie");
        names.add("Teacup");
        names.add("Kiddo");
        names.add("Smarty");
        names.add("Ace");
        names.add("Goon");
        names.add("Punk");
        names.add("Rambo");
        names.add("Smiley");

        names.add("Handsome");
        names.add("Boo");
        names.add("Stud");
        names.add("Bae");
        names.add("Babe");
        names.add("Honey");
        names.add("Casanova");
        names.add("Sexy");
        names.add("McDreamy");
        names.add("ALEKSIZE");
        return names.get(MathUtils.random(names.size() - 1)) + "@Bot";
    }

    ////////////////////////
    private Vector2 get_stickSlipy(Player p, float length) { // взять палочку для просмотра местности
        return get_vector_from_players_position(p.getBody_rotation(), p).nor();
    }

    private Vector2 get_stickSlipy(Player p, float length, float align) {
        return get_stickSlipy(p, length).rotateDeg(align);
    }

    private Vector2 get_vector_from_players_position(Vector2 in, Player forPlayer) { // вернуть ветор от позиции танка (очень важный метод)
        return in.cpy().sub(forPlayer.getPosi());
    }


}
