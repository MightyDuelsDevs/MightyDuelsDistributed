/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.SocketManagerServer;

import Server.Controller.CardDeckController;
import Server.Domain.Game;
import Server.Domain.Hero;
import Server.Domain.ITarget;
import Server.Domain.Match;
import Server.Domain.Minion;
import Server.Domain.Player;
import Server.Run.MightyDuelsServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.List;
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
    private boolean lastAccepted = false;
    
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
            return;
        }
        //todo send connection accepted
        InputStream in;
        try {
            in = socket.getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            closed = true;
            //todo throw error
            return;
        }
        while(socket.isConnected()){
            int input;
            try {
                input = in.read();
            } catch (IOException ex) {
                Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
                closed = true;
                try {
                    socket.close();
                } catch (IOException ex1) {
                    Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex1);
                }
                //todo throw error
                continue;
            }
            connAccepted();
            switch(input){
                case -1:
                    //todo read error
                    break;
                case 0x01:
                    int hashlength = 0x100;//todo tbd
                    byte[] hash = new byte[hashlength];
            
                    try {
                        in.read(hash, 0, hashlength);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    StringBuilder sb = new StringBuilder();
                    for(byte d : hash){
                        String hex = Integer.toHexString(0xff & d);
                        if(hex.length() == 1) sb.append('0');
                        sb.append(hex);
                    }
                    player = (Player) MightyDuelsServer.loginProvider.getPlayerFromToken(sb.toString());
                    if(player == null){
                        loginDenied();
                        closed=true;
                        try {
                            socket.close();
                        } catch (IOException ex) {
                            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    Game.getInstance().addWaitingPlayer(player);
                    loginAccepted();
                    break;
                case 0x02://set_card
                    int cardId;
                    try {
                        cardId = readInt16(in);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
                        illegalAction();
                        return;
                    }
                    
                    if(hero.playCard(CardDeckController.getCard(cardId))){
                        accepted();
                    }else{
                        illegalAction();
                    }
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
                        illegalAction();
                        return;
                    }
                    ITarget itarget;
                    Hero opponend = match.getHero1() == hero? match.getHero2() : match.getHero1();
                    if(target == 0){
                        itarget = opponend;
                    }else if(target==1){
                        List<Minion> om = opponend.getMinions();
                        if(om.size()>=1){
                            itarget = om.get(0);
                        }else{
                            illegalAction();
                            continue;
                        }
                    }else{
                        List<Minion> om = opponend.getMinions();
                        if(om.size()>=2){
                            itarget = om.get(1);
                        }else{
                            illegalAction();
                            continue;
                        }
                    }
                    if(source == 0){
                        if(target != 0){
                            illegalAction();//player can't attack minion
                            continue;
                        }
                        //todo hero.setTarget(itarget);
                    }else{
                        List<Minion> mm = hero.getMinions();
                        if(source == 1 && mm.size()>=1){
                            mm.get(1).setITarget(itarget);
                        }else if(source == 2 && mm.size()>=2){
                            mm.get(2).setITarget(itarget);
                        }else{
                            illegalAction();
                            continue;
                        }
                    }
                    accepted();
                    //0=face 1=min1 2-min2
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
                    accepted();
                    break;
                case 0x05:
                    match.concede(hero);
                    accepted();
                    break;
                case (byte)0x80://MESSAGE
                    ByteBuffer mbuf = ByteBuffer.allocate(1024);
                    int message = -1;
                    try {
                        message = in.read();
                    } catch (IOException ex) {
                        Logger.getLogger(SocketClient.class.getName()+"-"+player.getUsername()).log(Level.SEVERE, null, ex);
                    }      
                    while(message!=0x00){
                        if(message == -1){
                            //todo throw error
                        }
                        mbuf.put((byte)message);
                        try {
                            message = in.read();
                        } catch (IOException ex) {
                            Logger.getLogger(SocketClient.class.getName()+"-"+player.getUsername()).log(Level.SEVERE, null, ex);
                        }
                    }
                    String mes;
                    try {
                        mes = new String(mbuf.array(),"UTF-8");
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(SocketClient.class.getName()+"-"+player.getUsername()).log(Level.SEVERE, null, ex);
                        //todo fatal
                    }
                    //todo send message to other client
                    break;
                    case (byte)0xE0://PING
                    pong();
                    break;
                case (byte)0xE1://PONG
                    synchronized(this){
                        notify();
                    }
                    break;
                case 0xF0://ACCEPTD
                    lastAccepted = true;
                    synchronized(this){
                        notify();
                    }
                    break;
                case 0xF1://ILLEGAL_ACTION
                    lastAccepted = false;
                    synchronized(this){
                        notify();
                    }
                    break;
                case 0xFA:
                    //todo notify fatal disconnection
                    break;
                case 0xFB:
                    //todo notify non fatal disconnection
                    break;
            }
            
        }
        
    }
    
    public boolean isClosed(){
        return closed;
    }
    
    public void stop(){
        
    }
    
    public void connAccepted(){
        byte[] data = new byte[]{(byte)0x01};
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo end connection?
        }
    }
    
    public void loginAccepted(){
        byte[] data = new byte[]{(byte)0x02};
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo end connection?
        }
    }
    
    public void loginDenied(){
        byte[] data = new byte[]{(byte)0x03};
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        closed = true;
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void accepted(){
        byte[] data = new byte[]{(byte)0xF0};
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo end connection?
        }
    }
    
    public void illegalAction(){
        byte[] data = new byte[]{(byte)0xF1};
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo end connection?
        }
    }

    public void ping(){
        byte[] data = new byte[]{(byte)0xE0};
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo end connection?
        }
        try {
            synchronized(this){
                wait();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void pong(){
        byte[] data = new byte[]{(byte)0xE1};
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void newMatch(Match match,String username, int id){
        byte[] usernameEncoded;
        try {
            usernameEncoded = username.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //fatal error
            return;
        }
        byte[] int16Id = int16Encode(id);
        byte[] data = new byte[usernameEncoded.length +4];
        data[0] = 0x04;
        System.arraycopy(usernameEncoded, 0, data, 1, usernameEncoded.length);
        data[usernameEncoded.length]=0x00;
        System.arraycopy(int16Id, 0, data, usernameEncoded.length+1, 2);
        try {
            sendData(data);
            synchronized(this){
                wait();
            }
            if(lastAccepted){
                //todo check lastaccepted
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //fatal error
        }
    }
    
    public void turnEnd(int card){
        byte[] data = new byte[3];
        data[0] = 0x05;
        byte[] int16 = int16Encode(card);
        data[1]=int16[0];
        data[2]=int16[1];
        try {
            sendData(data);
            synchronized(this){
                wait();
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //fatal error
        }
        
    }
    
    public void addMinion(boolean self, int pos, int card){
        byte totalPos = (byte)(self?pos:pos+2);
        byte[] int16Card = int16Encode(card);
        byte[] data = new byte[4];
        data[0] = 0x06;
        data[1] = totalPos;
        data[2]=int16Card[0];
        data[3]=int16Card[1];
        try {
            sendData(data);
            synchronized(this){
                wait();
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //fatal error
        }
    }
    
    public void setHealth(boolean self, int pos, int value){
        byte totalPos = (byte)(self?pos:pos+3);
        byte[] data = new byte[3];
        data[0]=0x07;
        data[1]=totalPos;
        data[2]=(byte)value;
        try {
            sendData(data);
            synchronized(this){
                wait();
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //fatal error
        }
    }
    
    public void newTurn(int[] cards){
        byte[] data = new byte[7];
        data[0]=0x08;
        for(int i = 0;i<3;i++){
            byte[] card = int16Encode(cards[i]);
            data[i*2+1]=card[0];
            data[i*2+2]=card[1];
        }
        try {
            sendData(data);
            
            synchronized(this){
                wait();
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //fatal error
        }
    }
    
    //0=lose 1=tie 2=win
    public void matchEnd(int win){
        byte[] data = new byte[2];
        data[0]=0x09;
        data[1] = (byte)win;
        try {
            sendData(data);
            synchronized(this){
                wait();
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //fatal error
        }
    }
    
    public void sendMessage(String message){
        byte[] encodedString;
        try {
            encodedString = message.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //todo throw fatal
            return;
        }
        byte[] data = new byte[encodedString.length+2];
        data[0] = (byte)0x80;
        System.arraycopy(encodedString, 0, data, 1, encodedString.length);
        data[data.length-1]=0x00;
        try {
            sendData(data);
            synchronized(this){
                wait();
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //todo throw fatal
        }
        
    }
    
    public void fatalDisconnect(){
        if(!socket.isConnected()){
            //todo throw error
            return;
        }
        byte[] data = new byte[]{(byte)0xFA};
        try {
            sendData(data);
            //todo disconnect socket
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //socket dead
        }
    }
    
    public void nonFatalDisconnect(){
        if(!socket.isConnected()){
            //todo throw error
            return;
        }
        byte[] data = new byte[]{(byte)0xFA};
        try {
            sendData(data);
            //todo disconnect socket
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //socket dead
        }
    }
    
    private int readInt16(InputStream in) throws IOException{
        int lb = in.read();
        int ub = in.read();
        return lb+ub*0x100;
    }
    
    private byte[] int16Encode(int input){
        byte[] data = new byte[2];
        if(input<0x100){
            data[0] = (byte)input;
            data[1] = 0;
        }else{
            data[0] = (byte)(input % 0x100);
            data[1] = (byte)((input-data[0])/0x100);
        }
        return data;
    }
    
    private synchronized void sendData(byte[] data) throws IOException{
        socket.getOutputStream().write(data);
    }
}
