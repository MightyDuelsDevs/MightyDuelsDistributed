/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.SocketManagerServer;

import Server.Domain.Player;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rick Rongen, www.R-Ware.tk
 */
public class SocketManager {
    
    private static final Logger LOG = Logger.getLogger(SocketManager.class.getName());
    
    private static SocketManager instance = null;
    private Map<byte[],Player> playerMap;
    
    public static SocketManager getInstance(){
        if(instance == null){
            instance = new SocketManager();
        }
        
        return instance;
    }
    
    private ServerSocket ss;
    private Thread clientManagerThread;
    private Timer t;
    private List<SocketClient> clients;
    
    public void open(){
        if(ss!=null){
            try {
                //todo close all connections
                ss.close();
            } catch (IOException ex) {
                Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        clients = new LinkedList<>();
        playerMap = new HashMap<>();
        try {
            ss = new ServerSocket(420);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        clientManagerThread = new Thread(()->clientManager());
        clientManagerThread.start();
        t = new Timer();
        t.schedule(new TimerTask(){

            @Override
            public void run() {
                clients.removeIf((c)->!c.isClosed());
            }
        }, 0,10000);
    }
    
    private void clientManager(){
        while(!ss.isClosed()){
            Socket newClient;
            try {
                LOG.info("Waiting for connection");
                newClient = ss.accept();
                LOG.info("New Connection! " + newClient.toString());
            } catch (IOException ex) {
                Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                //todo check if need to quit
                continue;
            }
            clients.add(new SocketClient(newClient));
        }
    }
    
    public void stop(){
        clients.stream().forEach((c)->c.stop());
        t.cancel();
        clients.clear();
        try {
            ss.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addPlayer(byte[] token, Player player){
        playerMap.put(token,player);
    }
    
    public Player getPlayer(byte[] token){
        return playerMap.get(token);
    }
    
    public int activeConnections(){
        return clients.size();
    }
}
