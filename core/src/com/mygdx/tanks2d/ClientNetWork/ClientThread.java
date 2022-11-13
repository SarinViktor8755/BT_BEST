package com.mygdx.tanks2d.ClientNetWork;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.mygdx.tanks2d.ClientNetWork.VoiceChat.VoiceChatClient;

import java.io.IOException;
import java.util.HashMap;

public class ClientThread extends Thread {
    Client client;
    VoiceChatClient voiceChatClient;


    public ClientThread(Client client) {
        this.start();
        this.client = client;
        this.voiceChatClient = new VoiceChatClient(client.getKryo());

    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public VoiceChatClient getVoiceChatClient() {
        return voiceChatClient;
    }

    public void setVoiceChatClient(VoiceChatClient voiceChatClient) {
        this.voiceChatClient = voiceChatClient;
    }

    @Override
    public void run() {
        System.out.println("Hello, client!");
        client.run();
        Network.register(client);
        try {
            client.connect(5000, Network.host, Network.tcpPort, Network.udpPort);
            voiceChatClient = new VoiceChatClient(client.getKryo());
            voiceChatClient.addReceiver(client);
            Network.register(client);
            Kryo kryo = client.getKryo();
            voiceChatClient = new VoiceChatClient(kryo);

            // Читает пришедшие записи
            voiceChatClient.addReceiver(client);
            // Server communication after connection can go here, or in Listener#connected().
            //    System.out.println("1111");
        } catch (IOException ex) {
            //  ex.printStackTrace();
        }



    }
}
