package main.java.com;

import static com.mygdx.tanks2d.ClientNetWork.Network.register;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.tanks2d.ClientNetWork.Heading_type;
import com.mygdx.tanks2d.ClientNetWork.Network;
import com.mygdx.tanks2d.ClientNetWork.VoiceChat.VoiceChatServer;


import java.io.IOException;
import java.util.Date;


import main.java.com.Bots.IndexBot;
import main.java.com.MatchOrganization.IndexMath;
import main.java.com.Units.ListPlayer.ListPlayers;
import main.java.com.Units.ListPlayer.Player;
import main.java.com.Units.SpaceMap.IndexMap;

public class GameServer {

    Server server;
    MainGame mainGame;
    IndexBot indexBot; // количество играков - по нему боты орентируюься сколько их нужно = для автобаласа
    private VoiceChatServer relay;

    public static boolean break_in_the_game;

    static long previousStepTime; // шаг для дельты
    public ListPlayers lp = new ListPlayers(this);


    public GameServer(String[] args, ServerLauncher serverLauncher) throws IOException {
        GameServer.break_in_the_game = false;

        int bufferSize = 22050; // Recommened value.
        server = new Server(bufferSize, bufferSize);
        register(server);
        server.bind(Network.tcpPort, Network.udpPort);
        server.start();
        previousStepTime = System.currentTimeMillis();
///////////
        relay = new VoiceChatServer(server.getKryo());
///////////////
        mainGame = new MainGame(this, getSizeBot(args));
        server.addListener(new Listener() {


                               @Override
                               public void disconnected(Connection connection) {
                                   lp.getPlayerForId(connection.getID()).setStatus(Heading_type.DISCONECT_PLAYER);
                                   lp.getPlayerForId(connection.getID()).setPosition(-10000, -10000);
                                   send_DISCONECT_PLAYER(connection.getID());
                               }

                               @Override
                               public void connected(Connection connection) {
                                   lp.addPlayer(connection.getID());
                                   lp.getPlayerForId(connection.getID()).setStatus(Heading_type.IN_MENU);
                                   send_MAP_PARAMETOR(connection.getID());

                               }


                               @Override
                               public void received(Connection connection, Object object) {
                                   relay.relayVoice(connection, object, server);
                                   ///      System.out.println(server.getConnections().length +"    -------------");
                                   if (object instanceof Network.PleyerPosition) {
                                       if(getLp().getPlayerForId(connection.getID()) == null) return;
                                       //надо проверить - есть литакой игрок
                                       lp.updatePosition(connection.getID(), (Network.PleyerPosition) object);
                                       lp.sendToAllPlayerPosition(connection.getID(), (Network.PleyerPosition) object);
                                       lp.getPlayerForId(connection.getID()).setStatus(Heading_type.IN_GAME);
                                       return;
                                   }

                                   if (object instanceof Network.StockMessOut) {// полученеи сообщения
                                       Network.StockMessOut sm = (Network.StockMessOut) object;
                                       //  System.out.println(sm);
                                       RouterMassege.routeSM(sm, connection.getID(), getMainGame().gameServer);
                                   }

                                   if (object instanceof Network.GivePlayerParameters) {
                                       //System.out.println(connection.getID() + " ::GivePlayerParameters" + (Network.GivePlayerParameters) object);
                                       Network.GivePlayerParameters gpp = (Network.GivePlayerParameters) object;
                                       lp.getPlayerForId(connection.getID()).setNikName(gpp.nik);

                                       Player p = mainGame.gameServer.getLp().getPlayerForId(gpp.nomerPlayer);
//
                                       if (p.getNikName() != null)
                                           mainGame.gameServer.send_PARAMETERS_PLAYER(p, connection.getID(), gpp.nomerPlayer);
                                   }

                               }
                           }
        );
        this.indexBot = new IndexBot(this, GameServer.getCountBot(args));
    }

    public Server getServer() {
        return server;
    }


    public void sendSHELL_RUPTURE(float x, float y, int nom, int author) {
        Network.StockMessOut stockMessOut = new Network.StockMessOut();
        stockMessOut.tip = Heading_type.SHELL_RUPTURE;
        stockMessOut.p1 = x;
        stockMessOut.p2 = y;
        stockMessOut.p3 = nom;
        stockMessOut.p4 = author;
        this.sendToAllTCP_in_game(stockMessOut);

    }

    public void send_PARAMETERS_PLAYER(int HP, int comant, String nikName, int forIdPlayer, int aboutPlayer) {
        Network.StockMessOut stockMessOut = new Network.StockMessOut();
        stockMessOut.tip = Heading_type.PARAMETERS_PLAYER;
        //  System.out.println(nikName);
        stockMessOut.p1 = aboutPlayer; // ХП
        stockMessOut.p2 = getCoomandforPlayer(aboutPlayer);// КОМАНДА
        stockMessOut.p3 = HP; // номер игрока
        stockMessOut.p4 = HP; // номер игрока
        stockMessOut.textM = nikName; // ник нейм
        this.server.sendToTCP(forIdPlayer, stockMessOut);

        //      System.out.println(nikName + ">>>>");
    }

    public void send_RESPOUN_PLAYER(int id, float x, float y) {
        Network.StockMessOut stockMessOut = new Network.StockMessOut();
        stockMessOut.tip = Heading_type.RESPOWN_TANK_PLAYER;
        stockMessOut.p1 = x; // позиция респауна
        stockMessOut.p2 = y; // позиция респауна
        stockMessOut.p3 = id; /// ид игрока
        this.server.sendToTCP(id, stockMessOut);
    }

    public void send_Chang_screen(boolean pause) { // нужно добаить время на сколько уходим на паузу
        Network.StockMessOut stockMessOut = new Network.StockMessOut();
        stockMessOut.tip = Heading_type.CHANGE_THE_SCREEN;
        if(pause)stockMessOut.p1 = Heading_type.PAUSE_GAME; else stockMessOut.p1 = Heading_type.PLAY_GAME;
        if(stockMessOut.p1 == Heading_type.PLAY_GAME) return;

        this.server.sendToAllTCP(stockMessOut);
    }

    public void send_PARAMETERS_PLAYER(Player p, int forIdPlayer, int abautPlayer) {
        send_PARAMETERS_PLAYER(p.getHp(), p.getCommand(), p.getNikName(), forIdPlayer, abautPlayer);
    }

    public void send_PARAMETERS_MATH() { // разослать параметры матча
        Network.StockMessOut stockMessOut = new Network.StockMessOut();
        stockMessOut.tip = Heading_type.PARAMETERS_MATH;
        stockMessOut.p1 = IndexMath.getBlue_team_score(); //счетсиний команды
        stockMessOut.p2 = IndexMath.getRed_team_score(); //счетсиний команды

        stockMessOut.p3 = getMainGame().getTimeMath(); // осталось времени в матче
        //stockMessOut.p4 = lp.getLive_blue_size_player(); // ОСТАЛОСЬ ЖИВЫХ КРАСНЫХ
        //ПЕРЕНЕСТИ В ПАКЕТ МАТЧА
        // stockMessOut.p3 = IndexMath.getRealTimeMath(); // вернуьт ревльное время матча

//        stockMessOut.p4 = IndexMath.getRealTimeMath(); // ОСТАЛОСЬ ЖИВЫХ СИНИХ
//        stockMessOut.p3 = IndexMath.getRealTimeMath(); // ОСТАЛОСЬ ЖИВЫХ КРАСНЫХ


        //stockMessOut.textM = ///IndexMath. // Номер карты будем делать менедже карт
        this.sendToAllTCP_in_game(stockMessOut);


    }

    public void send_PARAMETERS_PLAYER(Player p) { // для всех рассылк апараметров
        Network.StockMessOut stockMessOut = new Network.StockMessOut();
        if (p.getStatus() == Heading_type.DISCONECT_PLAYER) {
            send_DISCONECT_PLAYER(p.getId());
            return;
        }
        stockMessOut.tip = Heading_type.PARAMETERS_PLAYER;
        stockMessOut.p1 = p.getId(); // id
        stockMessOut.p2 = getCoomandforPlayer(p.getId());// КОМАНДА
        stockMessOut.p3 = p.getHp(); // ХП
        stockMessOut.p4 = p.getCommand(); // номер игрока
        stockMessOut.textM = p.getNikName(); // ник нейм
        this.sendToAllTCP_in_game(stockMessOut);
    }

    public void send_MAP_PARAMETOR() { // сообщить название карты
        Network.StockMessOut stockMessOut = new Network.StockMessOut();
        stockMessOut.tip = Heading_type.PARAMETERS_MAP;
        //stockMessOut.p1 = IndexMath.;
        stockMessOut.textM = mainGame.mapSpace.getMap_math();
//        if (mainGame.pause_game) stockMessOut.p1 = Heading_type.PAUSE_GAME;
//        else stockMessOut.p1 = Heading_type.PLAY_GAME;
//        if (GameServer.break_in_the_game) stockMessOut.p1 = Heading_type.PAUSE_GAME;
//        else stockMessOut.p1 = Heading_type.PLAY_GAME;
        this.server.sendToAllTCP(stockMessOut);


    }

    public void send_MAP_PARAMETOR(int id) { // сообщить название карты для одного
        Network.StockMessOut stockMessOut = new Network.StockMessOut();
        stockMessOut.tip = Heading_type.PARAMETERS_MAP;

        if (GameServer.break_in_the_game) stockMessOut.p1 = Heading_type.PAUSE_GAME;
        else stockMessOut.p1 = Heading_type.PLAY_GAME;

        stockMessOut.textM = mainGame.mapSpace.getMap_math();

        this.server.sendToTCP(id, stockMessOut);
        System.out.println("!!!!!!!!!!MAP:::");
    }

    public void sendToAllTCP_in_game(Object object) { // разослать тем кто в игре
        Connection[] connections = server.getConnections();
        for (int i = 0, n = connections.length; i < n; i++) {
            Connection connection = connections[i];
            if (lp.getPlayerForId(connection.getID()).isClickButtonStart())
                connection.sendTCP(object);
        }
    }

    public void send_DISCONECT_PLAYER(int idPlayer) {
        Network.StockMessOut stockMessOut = new Network.StockMessOut();
        stockMessOut.tip = Heading_type.DISCONECT_PLAYER;
        stockMessOut.p1 = idPlayer;
        this.sendToAllTCP_in_game(stockMessOut);
    }


    public MainGame getMainGame() {
        return mainGame;
    }


    private int getSizeBot(String args[]) {
        try {
            return Integer.valueOf(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return ListPlayers.DEFULT_COUNT_BOT;
        }
    }

    public static long getDeltaTime() {
        long time = System.currentTimeMillis();
        long raz = (time - previousStepTime);
        previousStepTime = time;
        return raz;
    }

    public boolean isServerLivePlayer() {
        // System.out.println(server.getConnections().length > 0);

        if (server.getConnections().length > 0) return true;
        else return false;
    }

    public int countLivePlayer() {


        return 1;
        // return server.getConnections().length;
    }

    public IndexBot getIndexBot() {
        return indexBot;
    }

    public ListPlayers getLp() {
        return lp;
    }

    public static String getDate() {
        // Инициализация объекта date
        Date date = new Date();
        // Вывод текущей даты и времени с использованием toString()
        return String.valueOf(date);
    }

    private static int getCountBot(String[] par) {
        int res = 10;
        try {
            res = Integer.parseInt(par[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return res;
    }

    private int getCoomandforPlayer(int id) {
        return lp.getPlayerForId(id).getCommand();
//        if (MathUtils.randomBoolean()) return Heading_type.BLUE_COMMAND;
//        else return Heading_type.RED_COMMAND;
    }


}

