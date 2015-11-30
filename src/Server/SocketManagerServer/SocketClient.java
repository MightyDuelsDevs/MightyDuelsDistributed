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
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.ByteBuffer;
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
                    //todo respond with PONG
                    break;
                case (byte)0xE1://PONG
                    //todo finish last PING request
                    break;
                case 0xF0://ACCEPTD
                    //todo accept last request
                    break;
                case 0xF1://ILLEGAL_ACTION
                    //todo fail last command
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
            //todo wait for accepted
        } catch (IOException ex) {
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
            //todo wait for accepted
        } catch (IOException ex) {
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
            //todo wait for accepted
        } catch (IOException ex) {
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
            //todo wait for accepted
        } catch (IOException ex) {
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
            //todo wait for accepted
        } catch (IOException ex) {
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
            //todo wait for accepted
        } catch (IOException ex) {
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
            //todo await accepted
        } catch (IOException ex) {
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
