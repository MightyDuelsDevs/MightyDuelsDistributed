/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.SocketManagerServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rick Rongen, www.R-Ware.tk
 */
public class SocketManager {
    
    private static SocketManager instance = null;
    
    public static SocketManager getInstance(){
        if(instance == null){
            instance = new SocketManager();
        }
        
        return instance;
    }
    
    private ServerSocket ss;
    private Thread clientManagerThread;
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
        try {
            ss = new ServerSocket(420);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        clientManagerThread = new Thread(()->clientManager());
        clientManagerThread.start();
    }
    
    private void clientManager(){
        while(!ss.isClosed()){
            Socket newClient;
            try {
                newClient = ss.accept();
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
        try {
            ss.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
