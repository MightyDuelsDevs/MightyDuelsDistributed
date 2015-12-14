/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.SocketManagerClient;

import Client.GUI.MatchController;
import com.sun.media.jfxmedia.track.Track;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rick Rongen, www.R-Ware.tk
 */
public class SocketManager {
    private static final int DIEN_MAM = -1;
    
    private static final Logger LOG = Logger.getLogger(SocketManager.class.getName());
    
 //   private static SocketManager instance;
    
//    public static SocketManager getInstance(){
//        if(instance == null){
//            instance = new SocketManager();
//        }
//        return instance;
//    }

    private Socket socket;
    private Thread inputReaderThread;
    private boolean lastAccepted = false;
    
    private MatchController controller;
    
    public SocketManager(MatchController controller) {
        socket = new Socket();
        this.controller = controller;
    }
    
    public void Connect(String ip) throws IOException{
        socket.connect(new InetSocketAddress(ip,420));
        inputReaderThread = new Thread(()->inputReader());
        inputReaderThread.start();
    }
    
    private void inputReader(){
        InputStream in;
        try {
            in = socket.getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo make sure this go's to the GUI
            return;
        }
        while(!socket.isClosed()){
            int val = DIEN_MAM;
            try {
                val = in.read();
            } catch (IOException ex) {
                Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                //todo to GUI?
            }
            if(val!=DIEN_MAM)
                LOG.info("Command: " + val);
            switch(val){
                case DIEN_MAM:
                    //todo to GUI?
                    break;
                case 0x01://CONN_ACCEPTED
                    synchronized(this){
                        notify();
                    }
                    break;
                case 0x02://LOGIN ACCEPTED
                    lastAccepted = true;
                    synchronized(this){
                        notify();
                    }
                    break;
                case 0x03://LOGIN_DENIED
                    lastAccepted = false;
                    synchronized(this){
                        notify();
                    }
                    break;
                case 0x04://new match with user
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    int opponendName = -1;
                    try {
                        opponendName = in.read();
                    } catch (IOException ex) {
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                    }      
                    while(opponendName!=0x00){
                        if(opponendName == -1){
                            //todo throw error
                        }
                        buf.put((byte)opponendName);
                        try {
                            opponendName = in.read();
                        } catch (IOException ex) {
                            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    String username;
                    try {
                        username = new String(buf.array(),"UTF-8");
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                        //todo fatal
                        return;
                    }
                    int iconId = -1;
                    try {
                        iconId = readInt16(in);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(iconId < 0 || username.length()<1){
                        //todo throw error;
                        return;
                    }
                    controller.setOpponent(username, iconId);
                    accepted();
                    break;
                case 0x05://TURN_END
                    int opponendCard = -1;
                    try {
                        opponendCard = readInt16(in);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //todo check -1
                    //todo return the opponend card to the GUI
                    break;
                case 0x06://ADD_MINION
                    int boardLocation = -1;
                    int cardId = -1;
                    try {
                        boardLocation = in.read();
                        cardId = readInt16(in);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(boardLocation<0||cardId<0){
                        //todo throw error
                    }
                    controller.addMinion(cardId, boardLocation);
                    break;
                case 0x07: //SET_HEALTH
                    int character=-1,value=-1;
                    try{
                        character = in.read();
                        value = in.read();
                    }catch(IOException ex){
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(character<1||value<1){
                        //todo throw error
                    }
                    boolean self = character<4;
                    boolean minion = self ? character > 1 : character > 4;
                    int minionId = self ? (character == 2 ? 1 : 2) : (character == 5 ? 1 : 2);
                    //self your side or other side
                    //minion if it is an minion
                    //minionId 1 or 2 for minion id
                    controller.setHealth(self, minion, minionId, val);
                    break;
                case 0x08://NEW_TURN
                    int card1 = -1,card2 = -1,card3 = -1;
                    try{
                        card1 = readInt16(in);
                        card2 = readInt16(in);
                        card3 = readInt16(in);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if(card1<0||card2<0||card3<0){
                        //throw error
                    }
                    controller.newTurn(card1, card2, card3);
                    break;
                case 0x09://MATCH_END
                    int state = -1;
                    try {
                        state = in.read();
                    } catch (IOException ex) {
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(state<0){
                        //todo show error
                    }
                    //todo send to gui
                    if(state == 2){
                        controller.win();
                    }else if(state == 1){
                        controller.tie();
                    }else{
                        controller.lose();
                    }
                    break;
                case 0x80://MESSAGE
                    ByteBuffer mbuf = ByteBuffer.allocate(1024);
                    int message = -1;
                    try {
                        message = in.read();
                    } catch (IOException ex) {
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                    }      
                    while(message!=0x00){
                        if(message == -1){
                            //todo throw error
                        }
                        mbuf.put((byte)message);
                        try {
                            message = in.read();
                        } catch (IOException ex) {
                            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    String mes;
                    try {
                        mes = new String(mbuf.array(),"UTF-8");
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                        //todo fatal
                    }
                    //todo send message to GUI
                    break;
                case 0xE0://PING
                    pong();
                    break;
                case 0xE1://PONG
                    synchronized(this){ notify();}
                    break;
                case 0xF0://ACCEPTD
                    lastAccepted = true;
                    synchronized(this){ notify();}
                    break;
                case 0xF1://ILLEGAL_ACTION
                    lastAccepted = false;
                    synchronized(this){ notify();}
                    break;
                case 0xFA:
                    lastAccepted = false;
                    synchronized(this){ notifyAll();};
                    //todo notify fatal disconnection
                    break;
                case 0xFB:
                    lastAccepted = false;
                    synchronized(this){ notifyAll();}
                    //todo notify non fatal disconnection
                    break;
                default:
                    LOG.info("Unkown command: " + val);
                    break;
            }
        }
    }
    
    private int readInt16(InputStream in) throws IOException{
        int lb = in.read();
        int ub = in.read();
        return lb+ub*0x100;
    }
    
    public void connect(){
        byte[] data = new byte[]{0x4D,0x44,0x56,0x01};//MDV(0x01)
        if(socket.isConnected()){
            try {
                sendData(data);
                synchronized(this){
                    wait();
                }
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                //todo throw error
            }
        }else{
            //todo throw error
        }
    }
    
    public boolean login(byte[] loginHash){
        LOG.info("Hash lenght: " + loginHash.length);
        if(!socket.isConnected())
            //todo throw error
            return false;
        byte[] data = new byte[loginHash.length+1];
        data[0]=0x01;
        System.arraycopy(loginHash, 0, data, 1, loginHash.length);
        try {
            sendData(data);
            synchronized(this){
                wait();
            }
            return lastAccepted;
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo error
        }
        return false;
    }
    
    public void accepted(){
        byte[] data = new byte[]{(byte)0xF0};
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo to GUI?
        }
    }
    
    public void illegalAction(){
        byte[] data = new byte[]{(byte)0xF1};
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo to GUI?
        }
    }

    public void ping(){
        byte[] data = new byte[]{(byte)0xE0};
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo to GUI?
        }
        try {
            synchronized(this){
                wait();//wait for pong response
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
            //todo to GUI?
        }
    }
    
    public void setCard(int cardId) throws Exception{
        if(cardId>0xFFFF){
            throw new Exception("CardId out of range");
        }
        if(!socket.isConnected()){
            //throw error not connected
        }
        byte lb = 0;
        byte hb = 0;
        if(cardId<0x100){
            lb=(byte)cardId;
        }else{
            lb=(byte)(cardId % 0x100);
            hb=(byte)((cardId - cardId % 0x100)/0x100); // replaced lb with cardId % 0x100 because of java singned byte
        }
        byte[] data = new byte[]{0x02,lb,hb};
        try {
            sendData(data);
            synchronized(this){
                wait();
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo fatal exception
        }
    }
    
    public void setTarget(int source, int target){
        //todo check source and target 1..3
        if(!socket.isConnected()){
            //todo throw error
            return;
        }
        byte[] data = new byte[]{0x03,(byte)source,(byte)target};
        try {
            sendData(data);
            synchronized(this){
                wait();
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo throw error
        }
    }
    
    public void setFinished(boolean finished){
        if(!socket.isConnected()){
            //todo throw error
            return;
        }
        byte[] data = new byte[]{0x04,finished?(byte)0x01:(byte)0x00};
        try {
            sendData(data);
            synchronized(this){
                wait();
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo throw error
        }
    }
    
    public void concede(){
        if(!socket.isConnected()){
            //todo throw error
            return;
        }
        byte[] data = new byte[]{0x05};
        try {
            sendData(data);
            synchronized(this){
                wait();
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo throw error
        }
        
    }
    
    public void sendMessage(String message){
        byte[] encodedString;
        try {
            encodedString = message.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //socket dead
        }
    }
    
    private synchronized void sendData(byte[] data) throws IOException{
        socket.getOutputStream().write(data);
    }
}
