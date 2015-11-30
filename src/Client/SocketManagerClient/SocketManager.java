/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.SocketManagerClient;

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
    private static SocketManager instance;
    
    public static SocketManager getInsance(){
        if(instance == null){
            instance = new SocketManager();
        }
        return instance;
    }

    private Socket socket;
    private Thread inputReaderThread;
    
    public SocketManager() {
        socket = new Socket();
    }
    
    public void Connect(String ip) throws IOException{
        socket.connect(new InetSocketAddress(ip,420));
        inputReaderThread = new Thread(()->inputReader());
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
            int val = -1;
            try {
                val = in.read();
            } catch (IOException ex) {
                Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                //todo to GUI?
            }
            switch((byte)val){
                case -1:
                    //todo to GUI?
                    break;
                case 0x01://CONN_ACCEPTED
                    //todo send login info
                    break;
                case 0x02://LOGIN ACCEPTED
                    //wait for match
                    break;
                case 0x03://LOGIN_DENIED
                    //todo notify user
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
                    try {
                        String username = new String(buf.array(),"UTF-8");
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                        //todo fatal
                    }
                    int iconId = -1;
                    try {
                        iconId = readInt16(in);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //tod check -1
                    //todo return iconId and username to GUI for new match
                    //todo return ACCEPTED
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
                    //todo update GUI 
                                        
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
                    //todo update GUI set health
                    /* 1=self
                       2=self_minion1
                       3=self_minion2
                       4=opponend
                       5=opponend_minion1
                       6=opponend_minion2*/
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
                    //todo send new cards to gui
                    break;
                case 0x09:
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
                        //win
                    }else if(state == 1){
                        //tie
                    }else{
                        //lose
                    }
                    break;
                case (byte)0x80://MESSAGE
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
                case (byte)0xE0://PING
                    //todo respond with PONG
                    break;
                case (byte)0xE1://PONG
                    //todo finish last PING request
                    break;
                case (byte)0xF0://ACCEPTD
                    //todo accept last request
                    break;
                case (byte)0xF1://ILLEGAL_ACTION
                    //todo fail last command
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
                //todo wait accepted
            } catch (IOException ex) {
                Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                //todo throw error
            }
        }else{
            //todo throw error
        }
    }
    
    public void login(byte[] loginHash){
        if(!socket.isConnected())
            //todo throw error
            return;
        byte[] data = new byte[loginHash.length+1];
        data[0]=0x01;
        System.arraycopy(loginHash, 0, data, 1, loginHash.length);
        try {
            sendData(data);
            //todo wait accepted
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo error
        }
    }
    
    public void setCard(int cardId){
        if(cardId>0xFFFF){
            //throw error bigger then 16bit uint
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
            hb=(byte)((cardId-lb)/0x100);
        }
        byte[] data = new byte[]{0x02,lb,hb};
        try {
            sendData(data);
            //todo wait accepted
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo fatal exception
        }
    }
    
    public void setTarger(int source, int target){
        //todo check source and target 1..3
        if(!socket.isConnected()){
            //todo throw error
            return;
        }
        byte[] data = new byte[]{0x03,(byte)source,(byte)target};
        try {
            sendData(data);
            //todo await accepted
        } catch (IOException ex) {
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
            //todo wait accepted
        } catch (IOException ex) {
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
            //todo wait accepted
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo throw error
        }
        
    }
    
    private synchronized void sendData(byte[] data) throws IOException{
        socket.getOutputStream().write(data);
    }
}
