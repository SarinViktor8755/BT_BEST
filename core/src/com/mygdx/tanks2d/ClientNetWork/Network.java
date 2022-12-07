package com.mygdx.tanks2d.ClientNetWork;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;


public class Network {
    final public static int udpPort = 37001, tcpPort = 37001;

    //final public static Strin`g ip = "176.62.66.63";
    //final public static String ip = "185.231.68.81";
    //final public static String ip = "omskSarin2020.online";
    //final  public static String host = "127.0.0.1";
     final public static String host = "92.124.144.204";
    //final  public static String host = "192.168.1.129";

    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Integer.class);
        kryo.register(PleyerPosition.class);
        kryo.register(PleyerPositionNom.class);
        kryo.register(StockMessOut.class);
        kryo.register(StockMessInClient.class);
        kryo.register(GivePlayerParameters.class);
        kryo.register(Frag.class);
    }

    /////////////////////////////////////
    public static class PleyerPosition {   //позиция
        public float xp;
        public float yp;
        public float roy_tower;
    }

    public static class Frag {   //позиция

    }

    public static class PleyerPositionNom {   //ответ позиция с номером
        public int nom;
        public float xp;
        public float yp;
        public float roy_tower;

        @Override
        public String toString() {
            return "PleyerPositionNom{" +
                    "nom=" + nom +
                    ", xp=" + xp +
                    ", yp=" + yp +
                    ", roy_tower=" + roy_tower +
                    '}';
        }
    }


    public static class StockMessOut {   //сообщение из стока
        public int tip;
        public float p1;
        public float p2;
        public float p3;
        public float p4;
        public String textM;

        @Override
        public String toString() {
            return "StockMessOut{" +
                    "tip=" + Heading_type.getDomenTip(tip) +
                    ", p1=" + p1 +
                    ", p2=" + p2 +
                    ", p3=" + p3 +
                    ", p4=" + p4 +
                    ", textM='" + textM + '\'' +
                    '}';
        }
    }

    public static class StockMessInClient {   //сообщение из стока
        public int tip;
        public float p1;
        public float p2;
        public float p3;
        public float p4;
        public String textM;
        Integer nomerPlayer;
    }

    public static class GivePlayerParameters{   // лиент просит параметры играка )))
        public  int nomerPlayer;
        public String nik;

        @Override
        public String toString() {
            return
                    "nomerPlayer=" + nomerPlayer +
                    ", nik='" + nik + '\'' +
                    '}';
        }
    }

    public static class statusPlayer{ // пакет статусов Live - 1
        int idPlayer;
        byte status;
    }

}
