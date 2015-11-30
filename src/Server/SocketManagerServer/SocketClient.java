/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.SocketManagerServer;

import Server.Domain.Hero;
import Server.Domain.Match;
import Server.Domain.Player;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rick Rongen, www.R-Ware.tk
 */
public class SocketClient {

    private Socket socket;
    
    private Player player;
    private Match match;
    private Hero hero;
    
    private Thread inputThread;
    private boolean closed = false;
    
    public SocketClient(Socket socket) {
        this.socket = socket;
        inputThread = new Thread(()->inputHandler());
        inputThread.start();
    }
    
    private void inputHandler(){
        byte[] identifier = new byte[4];
        try {
            socket.getInputStream().read(identifier, 0, 4);
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(!(identifier[0]==0x4D && identifier[1]==0x44 && identifier[2]==0x56 && identifier[3]==0x01)){
            try {
                //check if the conneciton is good
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            closed=true;
        }
        InputStream in;
        try {
            in = socket.getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //todo throw error
            return;
        }
        while(socket.isConnected()){
            int input;
            try {
                input = in.read();
            } catch (IOException ex) {
                Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
                //todo throw error
                continue;
            }
            //todo send login request
            switch(input){
                case -1:
                    //todo read error
                    break;
                case 0x01:
                    int hashlength = 256;//todo tbd
                    byte[] hash = new byte[hashlength];
            
                    try {
                        in.read(hash, 0, hashlength);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    //todo get player and queue for match
                    //todo send login accepted
                    break;
                case 0x02://set_card
            
                    try {
                        int cardId = readInt16(in);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    //todo add getCard(int) method to CardDeckController
                    //hero.playCard(CardDeckController.getCard(cardId));     
                    //todo return accepted
                    break;
                
                case 0x03://set_target
                    int source,target;
                    try{
                       source = in.read();
                       target = in.read(); 
                    }catch(IOException ex){
                        Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
                        //todo error
                        continue;
                    }
                    
                    if(source<0||target<0){
                        //error
                    }
                    //todo determine source/target efficienly
                    //0=face 1=min1 2-min2
                    //todo return accepted
                    break;
                    
                case 0x04://set finished
                    int finished;
                    try {
                        finished = in.read();
                    } catch (IOException ex) {
                        Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
                        //todo error
                        continue;
                    }
                    hero.setFinished(finished==0x01);
                    //todo return accepted
                    break;
                case 0x05:
                    match.concede(hero);
                    //todo return accepted
                    break;
            }
            
        }
        
    }
    
    public boolean isClosed(){
        return closed;
    }
    
    public void stop(){
        
    }
    
    private int readInt16(InputStream in) throws IOException{
        int lb = in.read();
        int ub = in.read();
        return lb+ub*0x100;
    }
}
