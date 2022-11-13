package main.java.com.Units.ListPlayer;

import com.mygdx.tanks2d.ClientNetWork.Heading_type;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StatisticMath {   // класс дял подчета количетва играков )))))

    public static boolean key_recalculate_statistics = true;
    private ListPlayers lp;

    private static int sttistic[] = new int[6];
    private static HashMap<String,PlayerStatic> table = new HashMap();



//    private int size_live_player;
//    private int size_bot_player;
//
//    private int blue_size; // всех в команде
//    private int red_size;


    // разница команд в коичестве
    private int team_difference;

    private int live_blue_size;
    private int live_red_size;

    private boolean access_key;


    public StatisticMath(ListPlayers lp) {
        this.lp = lp;
        access_key = true; // ключ домтупа
        key_recalculate_statistics = true; // ключ начать пересчет
    }

//    public int[] getArrayStatic(){
//        return int
//    }

    public static void setTrue() {
        key_recalculate_statistics = true;
    }

    public synchronized StatisticMath counting_p() { // посчитать статичтику
        if (!key_recalculate_statistics) return this;
        //System.out.println("counting_p  " + lp.getSize());
        if (!access_key) return null;
        access_key = false;
        int size_live_player = 0; // rоличество живых играков
        int size_bot_player = 0; // количество ботов
        int live_blue_size_player = 0; // живых в команде
        int live_red_size_player = 0;  // живых в команде
        int blue_size = 0; // размер команды
        int red_size = 0; // размер команды
        Iterator<Map.Entry<Integer, Player>> entries = lp.getPlayers().entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, Player> entry = entries.next();
            Player p = entry.getValue();
           // System.out.println(p);
            if(p.status == Heading_type.DISCONECT_PLAYER) continue;
            lp.checking_empty_players(p); // ???
            ///////////////////////////
            if (p.command == Heading_type.BLUE_COMMAND) {
                blue_size++;
                if (isLive(p)) live_blue_size_player++;
            }

            if (p.command == Heading_type.RED_COMMAND) {
                red_size++;
                if (isLive(p)) live_red_size_player++;
            }
            if (!isBot(p)) {
                if (!p.in_game_player()) continue;
                    size_live_player++; // количество жиых играков ___ реальных играков
            } else {
                size_bot_player++;// количество БОТОВ играков ___ НЕ РЕАЛЬНЫХ играков
            }
        }
        StatisticMath.sttistic[0] = size_live_player;
        StatisticMath.sttistic[1] = size_bot_player;
        StatisticMath.sttistic[2] = blue_size;
        StatisticMath.sttistic[3] = red_size;
        StatisticMath.sttistic[4] = live_blue_size_player;
        StatisticMath.sttistic[5] = live_red_size_player;
        key_recalculate_statistics = false;
        access_key = true;
        return this;
    }

    ////////////////////////////////////
    public synchronized static int getLivePlayer() {
        return StatisticMath.sttistic[0];
    }

    public synchronized static int getSizeBot() {
        return StatisticMath.sttistic[1];
    }

    public synchronized static int getBlueSize() {
        return StatisticMath.sttistic[2];
    }

    public synchronized static int getRedSize() {
        return StatisticMath.sttistic[3];
    }

    public synchronized static int getLiveBlueSize() {
        return StatisticMath.sttistic[4];
    }

    public synchronized static int getLiveRedSize() {
        return StatisticMath.sttistic[5];
    }

    public synchronized static int getPlayersSize() {
        return getBlueSize() + getRedSize();
    }
    ////////////////////////////////////


    private static boolean isBot(Player p) {
        if (p.id < -99) return true;
        else return false;
    }

    private boolean isLive(Player p) {
        if (isBot(p)) {
          //  if (p.id > -99) return false;
            if (p.pos.x == -100000) return false;
        } else {
            if (p.hp < 1) return false;
        }
        return true;
    }

    public static int[] getSttisticMath() {
        return sttistic;
    }

    public static void printSttisticMath() {
        for (int i = 0; i < sttistic.length; i++) {
            System.out.print(sttistic[i] + "  ");
        }
        System.out.println();
    }

    ///////////

}
