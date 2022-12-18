package com.mygdx.tanks2d.ClientNetWork;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.tanks2d.ClientNetWork.VoiceChat.VoiceChatClient;
import com.mygdx.tanks2d.MainGame;
import com.mygdx.tanks2d.Units.Tanks.OpponentsTanks;
import com.mygdx.tanks2d.Units.Tanks.TanksOther;


import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.TreeMap;


public class MainClient {
    public float aplphaConnect;
    private Client client;   //клиент
    private boolean onLine;
    private MainGame mg;
    private int myIdConnect;
    private RouterSM routerSM;

    private static float coonection = -1; // происходит ли в реале конект или нет
    private static boolean key_coonection = false; // ключ на подключение

    private VoiceChatClient voiceChatClient;
    private NetworkPacketStock networkPacketStock;
    public TreeMap<Integer, Network.PleyerPositionNom> otherPlayer;
    public HashMap<Integer, Boolean> frameUpdates; //Обновления кадра для играков
//    public ArrayDeque<PacketModel> inDequePacket; // входящие пакеты для обработки;

    ClientThread clientThread;

    public MainClient(MainGame mg) {

        this.mg = mg;
        routerSM = new RouterSM(mg);

        int bufferSize = 22050; // Recommened value.

        client = new Client(bufferSize, bufferSize);
        client.start();

        // FrameworkMessage.Ping ping = new FrameworkMessage.Ping();
        Network.register(client);

        /////////////////VC
        Kryo kryo = client.getKryo();
//        voiceChatClient = new VoiceChatClient(kryo);
//
//        // Finally, allow the client to play audio recieved from the server.
//        voiceChatClient.addReceiver(client);


        clientThread = new ClientThread(client);
//////////////////

        frameUpdates = new HashMap<Integer, Boolean>();

        try {
            client.connect(5000, Network.host, Network.tcpPort, Network.udpPort);
            // Server communication after connection can go here, or in Listener#connected().
        } catch (IOException ex) {
            //  ex.printStackTrace();

        } catch (RuntimeException e3) {
            System.out.println("An error occurred please try again!");
        }

        otherPlayer = new TreeMap<>();
        onLine = true;

        this.networkPacketStock = new NetworkPacketStock(client);
        // this.startClient();

        client.addListener(new Listener() {
            public void connected(Connection connection) {
                setMyIdConnect(connection.getID());
            }

            public void received(Connection connection, Object object) {
                router(object);
            }

            public void disconnected(Connection connection) {
            }
        });
        //   Sytem.out.printlsn(client.isConnected() + "!!!!!!!!!!!!!!!1");
    }

    private void startClient() {
        //    System.out.println(client.isConnected());
        this.client = new Client();
        Network.register(client);
        this.client.start();
    }


    public int getMyIdConnect() {
        return myIdConnect;
    }

    public void setMyIdConnect(int myIdConnect) {
        this.myIdConnect = myIdConnect;
    }

    public Client getClient() {
        return client;
    }

    public void router(Object object) {
        if (!onLine) return;
        if (object instanceof Network.PleyerPositionNom) { // полученеи позиции играков
            Network.PleyerPositionNom pp = (Network.PleyerPositionNom) object;
            frameUpdates.put(pp.nom, true);
            // System.out.println("pp.nom  " + pp.nom + "   " +pp.xp);
            //   System.out.println(pp.nom + "x y " + pp.xp + " " + pp.yp );
            if (pp.nom == client.getID())  System.out.println("!!!!!!!!!!!!!______!!!MYYYYYYYYYYYYYYYYYYYYYY");
            if (pp.nom == client.getID()) return; // перить они НЕ должны приходить


            // System.out.println(mg.getGamePlayScreen().getTanksOther().get);
            try {
                if (mg.isMainMenuScreen()) return;
               // OpponentsTanks tank = mg.getGamePlayScreen().getTanksOther().getTankForID(pp.nom);
              //  System.out.println(tank);

                if (mg.getGamePlayScreen().getTanksOther().getTankForID(pp.nom) == null) {
                   // System.out.println("NET POLSOVATELY");
                    if(MathUtils.randomBoolean(.01f))
                   networkPacketStock.toSendMyParPlayer(pp.nom);


                }else
                mg.getGamePlayScreen().getTanksOther().setTankPosition(pp, mg.getMainClient().frameUpdates.get(pp.nom));
            } catch (NullPointerException e) {
                System.out.println("NET POLSOVATELY");
                //   mg.getGamePlayScreen().getTanksOther().createOponent(-10_000,-10_000,pp.nom,0);

            }


            // System.out.println("PleyerPositionNom");
//            try {
//                try {
//                    OpponentsTanks t = mg.getGamePlayScreen().getTanksOther().getTankForID(pp.nom);
//                    mg.getGamePlayScreen().getTanksOther().setTankPosition(pp, mg.getMainClient().frameUpdates.get(pp.nom));
//                } catch (NullPointerException e) {
//                    OpponentsTanks ot = new OpponentsTanks();
//                    mg.getGamePlayScreen().getTanksOther().createOponent(pp.xp, pp.yp, pp.nom, pp.roy_tower);
//                }
//
//                //mg.getMainClient().frameUpdates.put(pp.nom, false); /// закрывает флаг о рендере __
//            } catch (NullPointerException e) {
//                //   e.printStackTrace();
//            }

            return;
        }

        if (object instanceof Network.StockMessOut) {
            //    System.out.println((Network.StockMessOut) object);
            try {
                routerSM.routeSM((Network.StockMessOut) object);
            } catch (NullPointerException e) {
                // e.printStackTrace();
            }

        }

        if (object instanceof Network.Frag) {
            mg.getGamePlayScreen().getController().addFrag();
        }


    }


    public boolean isOnLine() {
        return true;
    }

    public void upDateClient() {

    }

    public NetworkPacketStock getNetworkPacketStock() {
        return this.networkPacketStock;
    }

    public boolean checkConnect(int status_game) {
        boolean result = true;
        reconectClienNewThred();
        //     System.out.println(NetworkPacketStock.required_to_send_tooken);
        getNetworkPacketStock().toSendMyTokken(status_game); // отправка ника и токкена
        //   if (!getClient().isConnected()) NetworkPacketStock.required_to_send_tooken = true;


        return result;
    }


    public VoiceChatClient getVoiceChatClient() {
        return this.clientThread.getVoiceChatClient();
    }

    synchronized void reconectClienNewThred() { // выполняется каждые 50 мс
        //   System.out.println(">>> " + coonection + "  " + key_coonection);
        coonection -= Gdx.graphics.getDeltaTime();

        if (coonection > 0) return;
        if (key_coonection) return;
        if (client.isConnected()) return;
        key_coonection = true;
        coonection = 8;


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        //   System.out.println(">->->->->->-  reconect ///////////");

                        getClient().reconnect(5000);
                        NetworkPacketStock.required_to_send_tooken = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                        coonection = 0;
                    } finally {
                        key_coonection = false;
                    }

                } catch (Exception e) {
                    //    e.printStackTrace();
                    System.out.println("not connect");
                }
            }
        }).start();
    }


}
