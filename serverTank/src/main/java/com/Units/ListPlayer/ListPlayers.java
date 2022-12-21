package main.java.com.Units.ListPlayer;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.tanks2d.ClientNetWork.Heading_type;
import com.mygdx.tanks2d.ClientNetWork.Network;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.esotericsoftware.kryonet.Connection;

import javax.swing.text.Position;

import main.java.com.Bots.TowerRotationLogic;
import main.java.com.GameServer;
import main.java.com.MatchOrganization.IndexMath;

public class ListPlayers {
    static public final int DEFULT_COUNT_BOT = 60;

    private int size_list_player_in_game = 0;

    private HashMap<Integer, Player> players;
    private ArrayList<Player> basket;
    //  ConcurrentHashMap<String, Integer> playersTokken; // tooken/ id

    GameServer gameServer;
    Network.PleyerPositionNom pn = new Network.PleyerPositionNom();

    Vector2 temp1 = new Vector2();
    Vector2 temp2 = new Vector2();

    static final float MAX_DIST = 150 * 150;

    //размеры команд
//    private int blue_size;
//    private int red_size;

    private StatisticMath statisticMath;
    //живы игроки
//    private int size_live_player; // оличество живых играков
//    private int size_bot_player;
//
//    private int team_difference;
//
//    private int live_blue_size;
//    private int live_red_size;
    ////////////// дублирую переменные _ НО так надо - используются  двух разных методов
//    private int live_blue_size_player;  // Это среднии живые игроки
//    private int live_red_size_player;

    private static Vector2 blue_average = new Vector2(0, 0);
    private static Vector2 red_average = new Vector2(0, 0);
    private static Vector2 average_cord = new Vector2(0, 0);

    public ListPlayers(GameServer gameServer) {
        this.basket = new ArrayList<>();
        this.players = new HashMap<Integer, Player>();
        // this.playersTokken = new ConcurrentHashMap<>();
        this.gameServer = gameServer;

        //  System.out.println("install_ListPlayers : " + GameServer.getDate());

//        red_size = 0;
//        blue_size = 0;
        statisticMath = new StatisticMath(this);
    }

    public boolean there_is_player(int id) {
        if (getPlayerForId(id) == null) return false;
        else return true;
    }

    public ArrayList<Player> getBasket() {
        return basket;
    }

    public boolean disconect(int id) {
        basket.add(getPlayerForId(id));
        players.remove(id);
        return true;
    }

    public Player getPlayerForId(int id) { // почему то вызывается  иногда
        Player result = players.get(id);
        if (result == null) {
            if (id < -99) return null;
        }
        return players.get(id);

    }

    // public ConcurrentHashMap<String, Integer> getPlayersTokken() {
//        return playersTokken;
//    }

    public HashMap<Integer, Player> getPlayers() {
        return players;
    }

    private boolean checkTokken(String tokken, int connct_id) { // проверяет был литакой токкен
//        playersTokken.put(tokken, connct_id);
//        Integer p = playersTokken.get(tokken);
//        if (p == null) {
//            playersTokken.put(tokken, connct_id);
//            return false;
//        } else {
//            return true;
//        }
        return true;
    }

    public void addPlayer(int con) {
        update_the_average_coordinates_of_the_commands();
        // this.players.put(con, new Player(con, gameServer.getMainGame().getIndexMath().getCommand()));
        //   System.out.println(this.players);
    }

    public void addPlayer(Player p) { // конструктоор для ботов
        update_the_average_coordinates_of_the_commands();
        this.players.put(p.getId(), p);
    }

    public boolean find_tokken_and_replace_old(int id, String tokken) { // токкен есть обносить путь до него
        boolean key = false;
        Iterator<Map.Entry<Integer, Player>> entries = players.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, Player> e = entries.next();
            if (tokken.equals(e.getValue().getTokken())) {
                players.put(id, e.getValue());
                // players.remove(e.getValue().status =);
                e.getValue().status = Heading_type.DISCONECT_PLAYER;
                key = true;
            }


        }
        if (!key) players.put((int) id, new Player(id));
        return key;
    }

    public void delete_by_toiken(String tokken) { // токкен есть обносить путь до него
        Iterator<Map.Entry<Integer, Player>> entries = players.entrySet().iterator();
        while (entries.hasNext()) { // удалить копию
            Map.Entry<Integer, Player> e = entries.next();
            //      if(e.getKey() == id) continue;
            if (tokken.equals(e.getValue().getTokken())) {
                players.remove(e.getKey());
                System.out.println("remove_tokken");
                // e.getValue().status = Heading_type.DISCONECT_PLAYER;
                // gameServer.send_DISCONECT_PLAYER(e.getKey());
            }
        }
        //  players.put(id, new Player(id, my_coomand, tokken));/// добавили нового пользователя
    }

    public void clearList() {
        this.players.clear();
    }


    public int getSize() {
        return this.players.size();
    }

//    public int getLive_blue_size_player() {
//        return live_blue_size_player;
//    }
//
//    public int getLive_red_size_player() {
//        return live_red_size_player;
//    }

//    public Player updatePosition(int id, Network.PleyerPosition pp) { // записать парметры Игрока
////        Player p = players.get(id);
////        if (p == null)
////            players.put(id, new Player(id, gameServer.getMainGame().getIndexMath().getCommand()));
////        p.setPosition(pp.xp, pp.yp);
////        p.setRotTower(pp.roy_tower);
////        return p;
//    }

    public void remove_player(int id) { // del player
        this.players.remove(id);
    }

    public static Vector2 getAverage_cord() {
        return average_cord;
    }

    public void sendToAllPlayerPosition(int id, Network.PleyerPosition pp) {
        /// тут надо подумать )))) както не отппавлять если нет ТОкккена !!!!!!
        pn.nom = id;
        pn.xp = pp.xp;
        pn.yp = pp.yp;
        pn.roy_tower = pp.roy_tower;
        gameServer.getServer().sendToAllExceptUDP(id, pn);
        //  System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!------------");


//        Iterator<Map.Entry<Integer, Player>> entries = players.entrySet().iterator();
//        while (entries.hasNext()) {
//            Map.Entry<Integer, Player> entry = entries.next();


    }


//    public void accept_bot(DBBot dbBot) {
//        Player p_bot = getPlayerForId(dbBot.getId());
//        p_bot.getPosi().x = dbBot.getPosition().x;
//        p_bot.getPosi().y = dbBot.getPosition().y;
//        p_bot.rotTower = dbBot.getAngle_rotation_tower().angleDeg();
//    }

    private void checkPlayerForDisconect(Player p) {
        if (p.status == Heading_type.DISCONECT_PLAYER) {
            p.setPosition(-10_000, -10_000);
            p.setHp(-1000);
        }

    }


    public void sendParametersPlayers(int aboutPlayerID) { // рассылка о характеристиках игрока по id
        Network.StockMessOut sm = new Network.StockMessOut();
        Player p = this.players.get(aboutPlayerID);
        try {
            //  System.out.println(">>>" + p.nikName);
            sm.textM = p.nikName;
            sm.p1 = aboutPlayerID;
            sm.p2 = p.command;
            sm.p3 = p.hp;
            //sm.p4 = еще какой т опараметр;
            sm.tip = Heading_type.PARAMETERS_PLAYER;
            gameServer.getServer().sendToAllExceptTCP(aboutPlayerID, sm);
            gameServer.send_PARAMETERS_MATH();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    public int projectile_collide_with_players(int author_id, float xs, float ys) {
        int res = -1;
        this.size_list_player_in_game = 0;
        temp1.set(xs, ys);
        Iterator<Map.Entry<Integer, Player>> entries = players.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, Player> entry = entries.next();
            if (!entry.getValue().in_game_player()) continue;
            if (entry.getValue().hp < 1) continue;

            //if (entry.getValue().hp < 1) continue;
            temp2.set(entry.getValue().getPosi().x, entry.getValue().getPosi().y);
            if (((temp1.dst2(temp2) < 500) && author_id != entry.getValue().getId()))
                res = entry.getKey();
            if (res != -1) return res;
        }
        //   System.out.println();
        return res;
    }
//////////////

    public int getSizeLivePlayer() { // kolichesto hivih i botov i igrakov
        int i = 0;
        Iterator<Map.Entry<Integer, Player>> entries = players.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, Player> entry = entries.next();
//            //entry.getValue().hp;
//            System.out.println(entry.getValue().hp);
            if (entry.getValue().hp > 0) i++;
        }
        return i;
    }


    public int getSizeComandSize(int command) { // играков в команде
        int i = 0;
        Iterator<Map.Entry<Integer, Player>> entries = players.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, Player> entry = entries.next();
            if (entry.getValue().getStatus() == Heading_type.DISCONECT_PLAYER) continue;
            if (entry.getValue().getCommand() == command) i++;
        }
        return i;
    }

    public int getSizeLiveBots() { // kolichesto botov
        int i = 0;
        Iterator<Map.Entry<Integer, Player>> entries = players.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, Player> entry = entries.next();
//            //entry.getValue().hp;
//            System.out.println(entry.getValue().hp);
            if (entry.getValue().id < -99) i++;
        }
        return i;
    }

    public int getSizeLiveRealPlayers() { // kolichesto real
        int i = 0;
        Iterator<Map.Entry<Integer, Player>> entries = players.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, Player> entry = entries.next();
//            //entry.getValue().hp;
//            System.out.println(entry.getValue().hp);
            //  System.out.println(entry.getValue().status + "  " + entry.getValue().getPosi().x);
            if (entry.getValue().status == Heading_type.DISCONECT_PLAYER) continue;
            if (entry.getValue().id > -99) i++;
        }
        return i;
    }

    public StatisticMath getStatisticMath() {
        return statisticMath;
    }

    public void send_bot_coordinates() {
        try {
            if (MathUtils.randomBoolean(.05f)) update_the_average_coordinates_of_the_commands();
            Iterator<Map.Entry<Integer, Player>> entries = players.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<Integer, Player> entry = entries.next();
                checkPlayerForDisconect(entry.getValue()); // проверка на дисконект игрока
                // if (!isBot(entry.getValue())) continue;
                Player p = entry.getValue();
                pn.nom = entry.getKey();
                pn.xp = p.getPosi().x;
                pn.yp = p.getPosi().y;
                pn.roy_tower = p.getRotTower();

                Connection[] connections = this.gameServer.getServer().getConnections();
                for (int i = 0, n = connections.length; i < n; i++) {
                    Connection connection = connections[i];
                    //  System.out.println(getPlayerForId(connection.getID()).isClickButtonStart());
                    try {

                        //     if (!getPlayerForId(connection.getID()).isClickButtonStart()) continue;
                        Player ppp = players.get(connections[i].getID());
                        if (ppp == null) return;
                        if (connections[i].getID() == pn.nom) continue;
                        //float dst = Vector2.dst2(pn.xp, pn.yp, ppp.getPosi().x, ppp.getPosi().y);

                        boolean bx = !MathUtils.isEqual(pn.xp, ppp.getPosi().x, 500);
                        boolean by = !MathUtils.isEqual(pn.yp, ppp.getPosi().y, 350);
                        boolean res = bx && by;
//                (dst > 230400)
                        if (!ppp.isLive()) {
                            connection.sendUDP(pn);
                        } else if ((((res) && !MathUtils.randomBoolean(.12f)))) continue;
                        connection.sendUDP(pn);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        //     players.remove(connections[i].getID());
                    }
                }
            }
        } catch (
                ConcurrentModificationException e) {
            e.printStackTrace();
        }
    }

//
//    private void update_number_of_clicks(int coomand) {
//        if (coomand == Heading_type.BLUE_COMMAND) blue_size++;
//        if (coomand == Heading_type.RED_COMMAND) red_size++;
//    }
//
//    private void update_number_of_clicks(int coomand, boolean live) {
//        if (coomand == Heading_type.BLUE_COMMAND) {
//            blue_size++;
//        //    if (live) live_blue_size_player++;
//        }
//
//
//        if (coomand == Heading_type.RED_COMMAND) {
//            red_size++;
//    //        if (live) live_red_size_player++;
//        }
//    }


    public void update_the_average_coordinates_of_the_commands() { // обновить средние координаты команд
        Iterator<Map.Entry<Integer, Player>> entries = this.players.entrySet().iterator();
        float xb = 0;
        float yb = 0;
        float xr = 0;
        float yr = 0;
        int r = 0;
        int b = 0;
        while (entries.hasNext()) {
            Player p = entries.next().getValue();
            if (!p.isLive()) continue;
            if (p.getCommand() == Heading_type.BLUE_COMMAND) {
                xb += p.getPosi().x;
                yb += p.getPosi().y;
                b++;
            }
            if (p.getCommand() == Heading_type.RED_COMMAND) {
                xr += p.getPosi().x;
                yr += p.getPosi().y;
                r++;
            }

        }
        ListPlayers.blue_average.set(xb / b, yb / b); /// среднее голубая команда  - ккординаты
        ListPlayers.red_average.set(xr / r, yr / r);/// среднее голубая команда  - ккординаты
        ///среднее по двум командам
        ListPlayers.average_cord.set(((ListPlayers.blue_average.x + ListPlayers.red_average.x) / 2), ((ListPlayers.blue_average.y + ListPlayers.red_average.y) / 2));
        //  System.out.println(blue_average + "  " + r + "   " + b + "  " + red_average + "    -- Общая средняя " + average_cord);

//        live_blue_size = b;
//        live_red_size = r;


    }

//    public int getLive_blue_size() {
//        return live_blue_size_player;
//    }
//
//    public int getLive_red_size() {
//        return live_red_size_player;
//    }

//    public void setLive_blue_size(int live_blue_size) {
//        this.live_blue_size = live_blue_size;
//    }

    public static Vector2 getBlue_average() {
        return blue_average;
    }

    public static Vector2 getRed_average() {
        return red_average;
    }


    //////////////collisin

    public Vector2 isCollisionsTanks(Vector2 pos) {
//        red_size = 0;
//        blue_size = 0;
        // if(MathUtils.randomBoolean(.005f))  System.out.println("RedC " + getSizeComandSize(Heading_type.RED_COMMAND) + "BlueC " + getSizeComandSize(Heading_type.BLUE_COMMAND));

        for (Map.Entry<Integer, Player> tank : this.players.entrySet()) {
            //   System.out.print(tank.getValue().getId() + "  " + tank.getValue().status  +"  "+ tank.getValue().getPosi().x + " | ");


            // System.out.println(tank.getValue().id + "  isCollisionsTanks");
            //    if (!tank.getValue().isLive() || tank.getValue().id > 0) continue; // ---- Вот после  этой строчки почему то колизиия перестает работать !!! НО СТРОЧКА НУЖНА!!!!
            //  if (tank.getValue().hp < 1) continue;
            //   System.out.println(tank.getValue().id + "   " + tank.getValue().pos+ "   " + tank.getValue().status);
            if (tank.getValue().isCollisionsTanks(pos)) {
                if (!tank.getValue().isLive()) continue;
                //  System.out.println("!!!!!!!!!!!!!!!"+ tank.getValue().nikName);
                return new Vector2().set(pos.cpy().sub(tank.getValue().pos).nor());
            }
        }
        //System.out.println();
        // System.out.println("red " + red_size + " " + "blue " + blue_size + "  " + (blue_size+red_size));
        return null;
    }


///////////////////

    public Integer targetTankForBotAttack(Vector2 myPosi, Player my) { // найти цель для бота - просто перебор кто первый попадется
        Iterator<Map.Entry<Integer, Player>> entries = players.entrySet().iterator();
        while (entries.hasNext()) {
            Player p = entries.next().getValue();
            if (!p.isLive()) continue;
            if (!p.in_game_player()) continue;
            if (my.getCommand() == p.getCommand()) continue;
            float dst = p.getPosi().dst2(myPosi);
            if (dst > TowerRotationLogic.rast_to_target) continue;
            if (dst < 5) continue;
            if (MathUtils.randomBoolean() && myPosi.equals(p.getPosi())) continue;
            return p.id;

        }
        return null;

    }

    public Vector2 search_for_nearest_tank(float x, float y) {
        float min_dst = 100_000;

        Vector2 result = null;
        Iterator<Map.Entry<Integer, Player>> entries = players.entrySet().iterator();
        while (entries.hasNext()) {
            Player p = entries.next().getValue();
            if (!p.isLive()) continue;
            if (!p.in_game_player()) continue;
            float d = p.getPosi().dst2(x, y);

            if (d < min_dst && d != 0) {
                min_dst = d;
                result = p.getPosi();
            }
        }
        //  System.out.println(min_dst + " !!!");
        if (min_dst > MAX_DIST || min_dst == 100_000) result = null;
        return result;

    }

    public Vector2 search_for_nearest_tank(Vector2 p) {
        return this.search_for_nearest_tank(p.x, p.y);
    }

    public int getCommandForId(int id) { // взять команду по ид ))) не тестировалоась
        try {
            return players.get(id).getCommand();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }

    }


    public int blue_players_size() {
        return StatisticMath.getSttisticMath()[2];
    }

    public int red_players_size() {
        return StatisticMath.getSttisticMath()[3];
    }

    public void respownAllPlaers() { //рестарт игкраков -
        gameServer.send_PARAMETERS_MATH();
        Iterator<Map.Entry<Integer, Player>> entries = players.entrySet().iterator();
        //  players.clear();
        respaunPlayer();
        while (entries.hasNext()) {
            Player p = entries.next().getValue();
            if (p.getId() < -99)
                //  System.out.println(players + "@@@@@__" + players.size());
                gameServer.getIndexBot().respaunBot(p);


        }
    }


    public void respaunPlayer() { // респаун игрока живого - новый плеер
        Iterator<Map.Entry<Integer, Player>> entries = players.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, Player> entry = entries.next();
            if (entry.getKey() < -99) continue;
            Player p = entry.getValue();
            if (!p.in_game_player()) continue;
            if (p.status == Heading_type.DISCONECT_PLAYER) {
                p.setPosition(-10_000, -10_000);
                p.setHp(-1000);
            }

            gameServer.send_RESPOUN_PLAYER(p.getId(), 50, 50);
            if (!p.in_game_player()) continue;
            p.setHp(100);
            if (!p.isLive()) {
                //     if (MathUtils.randomBoolean(0.05f)) {

//                    if (p.getCommand() == Heading_type.RED_COMMAND) p.setPosition(gameServer.getMainGame().getMapSpace().getRasp1());
//                    else p.setPosition(gameServer.getMainGame().getMapSpace().getRasp2());
                /// gameServer.send_PARAMETERS_PLAYER(p);


                //   System.out.println("!!!!!!!! RESPOWN" + p.getPosi());
                //       ..     }
            }
        }
    }

    //    public Player getRandomPlayer(){
//        Player result = null;
//
//
//    }
//    public void counting_games() { // подсчет всех видов играков;
////        //if(MathUtils.randomBoolean(.8f)) return;
////        size_live_player = 0;
////        size_bot_player = 0;
////        blue_size = 0;
////        red_size = 0;
//
////        live_blue_size_player = 0;
////        live_red_size_player = 0;
//        Iterator<Map.Entry<Integer, Player>> entries = players.entrySet().iterator();
//        while (entries.hasNext()) {
//            Map.Entry<Integer, Player> entry = entries.next();
//            Player p = entry.getValue();
//            checking_empty_players(p); // проверка пустых игроков
//            //  System.out.println("ID: " +p.getId()+"  "+p.getNikName() + "pos " + p.pos + "Status"+ p.status);
//
//            if (p.id > -99) {
//                if (!p.in_game_player()) continue;
//                update_number_of_clicks(p.getCommand(), p.isLive()); // добавить в команду
//                if(p.getPosi().x != -10_000) size_live_player++; // количество жиых играков ___ реальных играков
//
//
//            } else {
//                // if(p.getNikName().equals("tokken_123")) this.players.remove(p);
//                update_number_of_clicks(p.getCommand(), p.isLive()); // добавить в команду
//                size_bot_player++;// количество БОТОВ играков ___ НЕ РЕАЛЬНЫХ играков
//
//            }
//
//
//        }
//
////        System.out.println("_____________________________________________________");
////        System.out.println("size_live_player " + size_live_player + " || size_bot_player " + size_bot_player);
////        System.out.println("blue_size " + blue_size + "  |      red_size " + red_size);
////        System.out.println("BLUE_LIVE " + live_blue_size_player + "  |      RED_LIVE " + live_red_size_player);
////        System.out.println("_____________________________________________________");
//
//        this.team_difference = getRed_size() - getBlue_size();
//     //   System.out.println(get_activ_player_bots() + "   " + getRed_size() + " " + getBlue_size());
//    }

    public void checking_empty_players(Player p) {
        if (p.getId() > -99) return;
        if (p.nikName.equals(Heading_type.DEFULT_NAME)) {
            gameServer.send_DISCONECT_PLAYER(p.getId());
            this.players.remove(p);
        }
    }

    public int getIdRandomBot() {
        Iterator<Map.Entry<Integer, Player>> entries = players.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, Player> entry = entries.next();
            Player p = entry.getValue();
            if (isBot(p)) return p.id;

        }

        return 99;
    }

    public int getSize_list_player_in_game() {
        return size_list_player_in_game;
    }

    public void clearAllBots() { // не тестировал
        Iterator<Map.Entry<Integer, Player>> entries = players.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, Player> entry = entries.next();
            Player p = entry.getValue();
            if (isBot(p)) remove_player(p.getId());
        }
    }

    private boolean isBot(Player p) {
        if (p.getId() < -99) return true;
        return false;
    }

    public void print_list_player() {
        System.out.println("^^^^^^^^^^^^^^^^^LP^^^^^^^^^^^^^^^^^^^^^");
        for (Map.Entry<Integer, Player> entry : getPlayers().entrySet()) {
            //  System.out.println("ID =  " + entry.getKey() + " День недели = " + entry.getValue());
            if (isBot(entry.getValue())) continue;
            System.out.println(entry.getValue());
        }

        System.out.println("^^^^^^^^^^^^^^^^^BOTS^^^^^^^^^^^^^^^^^^^^^");
        for (Map.Entry<Integer, Player> entry : getPlayers().entrySet()) {
            //  System.out.println("ID =  " + entry.getKey() + " День недели = " + entry.getValue());
            if (!isBot(entry.getValue())) continue;
            System.out.println(entry.getValue());
        }
        System.out.println("----------LP-------------");
    }


}
