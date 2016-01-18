/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.SocketManagerClient;

import Client.Controller.StageController;
import Client.GUI.MatchController;
import Client.GUI.SpectateController;
import com.sun.media.jfxmedia.track.Track;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author Rick Rongen, www.R-Ware.tk
 */
public class SocketManager {

    private static final int error = -1;

    private static final Logger LOG = Logger.getLogger(SocketManager.class.getName());

    private Socket socket;
    private Thread inputReaderThread;
    private boolean lastAccepted = false;

    private MatchController controller;
    private SpectateController spectateController;
    
    private boolean spectating = false;

    /**
     * Initiating the SocketManager for the match.
     *
     * @param controller, The controller for the match from this players side.
     */
    public SocketManager(MatchController controller) {
        socket = new Socket();
        this.controller = controller;
    }
    
    public SocketManager(SpectateController controller) {
        spectating = true;
        socket = new Socket();
        this.spectateController = controller;
    }

    /**
     * Method that connects the SocketManager with an IP.
     *
     * @param ip, the IP the client uses to talk to the server.
     * @throws IOException
     */
    public void Connect(String ip) throws IOException {
        if (ip == null) {
            ip = "127.0.0.1";
        }
        socket.connect(new InetSocketAddress(ip, 420));
        inputReaderThread = new Thread(() -> inputReader());
        inputReaderThread.setName("ClientSocketReader");
        inputReaderThread.start();
    }

    private void inputReader() {
        InputStream in;
        try {
            in = socket.getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo make sure this go's to the GUI
            return;
        }
        while (!socket.isClosed() && socket.isConnected()) {
            int val = error;
            try {
                val = in.read();
            } catch (IOException ex) {
                Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                //todo to GUI?
            }
            if (val != error) {
                LOG.info("Command: " + val);
            }
            switch (val) {
                case error:
                    //todo to GUI?
                    break;
                case 0x01://CONN_ACCEPTED
                    synchronized (this) {
                        notify();
                    }
                    break;
                case 0x02://LOGIN ACCEPTED
                    lastAccepted = true;
                    synchronized (this) {
                        notify();
                    }
                    break;
                case 0x03://LOGIN_DENIED
                    lastAccepted = false;
                    synchronized (this) {
                        notify();
                    }
                    break;
                case 0x04://new match with user
                    
                    String username;
                    try {
                        username = readString(in);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                        continue;
                    }
                    
                    int iconId = -1;
                    try {
                        iconId = readInt16(in);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (iconId < 0 || username.length() < 1) {
                        //todo throw error;
                        return;
                    }
                    controller.setOpponent(username, iconId);
                    accepted();
                    StageController.matchFound = true;
                    break;
                case 0x05://TURN_END
                    int opponendCard = -1;
                    try {
                        opponendCard = readInt16(in);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    controller.turnEnd(opponendCard);
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
                    if (boardLocation < 0 || cardId < 0) {
                        //todo throw error
                        LOG.warning("Error receiving ADD_MINION command, data was " + boardLocation + ", " + cardId);
                        continue;
                    }
                    if(!spectating){
                        controller.addMinion(cardId, boardLocation);
                    }else{
                        spectateController.addMinion(cardId, boardLocation);
                    }
                    break;
                case 0x07: //SET_HEALTH
                    int character = -1,
                     value = -1;
                    try {
                        character = in.read();
                        value = in.read();
                    } catch (IOException ex) {
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    LOG.info("set health " + character + " " + value);
                    if (character < 1 || value < 1) {
                        LOG.warning("Error receiving SET_HEALTH command, data was " + character + ", " + value);
                        continue;
                    }
                    boolean self = character < 4;
                    boolean minion = self ? character > 1 : character > 4;
                    int minionId = self ? (character == 2 ? 1 : 2) : (character == 5 ? 1 : 2);
                    //self your side or other side
                    //minion if it is an minion
                    //minionId 1 or 2 for minion id
                    if(!spectating){
                        controller.setHealth(self, !minion, minionId, value);
                    }else{
                        spectateController.setHealth(self, !minion, minionId, value);
                    }
                    break;
                case 0x08://NEW_TURN
                    int card1 = -1,
                     card2 = -1,
                     card3 = -1;
                    try {
                        card1 = readInt16(in);
                        card2 = readInt16(in);
                        card3 = readInt16(in);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (card1 < 0 || card2 < 0 || card3 < 0) {
                        //throw error
                    }
                    accepted();
                    controller.newTurn(card1, card2, card3);

                    break;
                case 0x09://MATCH_END
                    int state = -1;
                    try {
                        state = in.read();
                    } catch (IOException ex) {
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (state < 0) {
                        //todo show error
                    }

                    final int fState = state;
                    //todo send to gui
                    if(!spectating){
                        Platform.runLater(() -> {
                            if (fState == 2) {
                                controller.win();
                            } else if (fState == 1) {
                                controller.tie();
                            } else {
                                controller.lose();
                            }
                        });
                    }else{
                       Platform.runLater(() -> {
                            if (fState == 2) {
                                spectateController.win();
                            } else if (fState == 1) {
                                spectateController.tie();
                            } else {
                                spectateController.lose();
                            }
                        }); 
                    }
                    nonFatalDisconnect();
                    break;
                case 0x0A://JOIN_MATCH
                    String p1Name;
                    String p2Name;
                    int p1Icon;
                    int p2Icon;
            
                    try {
                        p1Name = readString(in);
                        p1Icon = readInt16(in);
                        p2Name = readString(in);
                        p2Icon = readInt16(in);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                        continue;
                    }
                    spectateController.spectateNewMatch(p1Name, p2Name, p1Icon, p2Icon);
                    accepted();
            
                    break;
                case 0x0B://SPEC_TURN_END
                    int p1Card;
                    int p2Card;
            
                    try {
                        p1Card = readInt16(in);
                        p2Card = readInt16(in);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                        continue;
                    }
                    spectateController.turnEnd(p1Card, p2Card);
                    
                    break;
                case 0x80://MESSAGE
                    
                    String mes;
                    try {
                        mes = readString(in);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                        continue;
                    }
                    LOG.log(Level.INFO, "Reseaved message: {0}", mes);
                    if(!spectating){
                        controller.receiveMessage(mes);
                    }else{
                        spectateController.receiveMessage(mes);
                    }
                    accepted();
                    break;
                case 0xE0://PING
                    pong();
                    break;
                case 0xE1://PONG
                    synchronized (this) {
                        notify();
                    }
                    break;
                case 0xF0://ACCEPTD
                    lastAccepted = true;
                    synchronized (this) {
                        notify();
                    }
                    StageController.matchFound = true;
                    break;
                case 0xF1://ILLEGAL_ACTION
                    lastAccepted = false;
                    synchronized (this) {
                        notify();
                    }
                    break;
                case 0xFA:
                    lastAccepted = false;
                    synchronized (this) {
                        notifyAll();
                    }
                    ;
                    //todo notify fatal disconnection
                    break;
                case 0xFB:
                    lastAccepted = false;
                    synchronized (this) {
                        notifyAll();
                    }
                    //todo notify non fatal disconnection
                    break;
                default:
                    LOG.info("Unkown command: " + val);
                    break;
            }
        }
    }

    private String readString(InputStream in) throws IOException{
        ByteBuffer mbuf = ByteBuffer.allocate(1024);
        int message = -1;
        try {
            message = in.read();
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (message != 0x00) {
            if (message == -1) {
                //todo throw error

            }
            mbuf.put((byte) message);
            message = in.read();
        }
        String mes;
        try {
            mes = new String(mbuf.array(), 0, mbuf.position(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo fatal
            return null;
        }
        return mes;
    }
    
    private int readInt16(InputStream in) throws IOException {
        int lb = in.read();
        int ub = in.read();
        return lb + ub * 0x100;
    }

    /**
     * Method used to connect to the server.
     */
    public void connect() {
        byte[] data = new byte[]{0x4D, 0x44, 0x56, 0x01};//MDV(0x01)
        if (socket.isConnected()) {
            try {
                sendData(data);
                synchronized (this) {
                    wait();
                }
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
                //todo throw error
            }
        } else {
            //todo throw error
        }
    }

    /**
     * Method that tries to log in the server using the match byte array.
     *
     * @param loginHash, the hash that the client uses to try to log in.
     * @return a boolean that confirms if the log in was successful(true) or
     * not(false).
     */
    public boolean login(byte[] loginHash) {
        LOG.info("Hash lenght: " + loginHash.length);
        if (!socket.isConnected()) //todo throw error
        {
            return false;
        }
        byte[] data = new byte[loginHash.length + 1];
        data[0] = 0x01;
        System.arraycopy(loginHash, 0, data, 1, loginHash.length);
        try {
            sendData(data);
            synchronized (this) {
                wait();
            }
            return lastAccepted;
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo error
        }
        return false;
    }
    
    public boolean loginSpectate(byte[] loginHash){
        LOG.info("Hash lenght: " + loginHash.length);
        if (!socket.isConnected()) //todo throw error
        {
            return false;
        }
        byte[] data = new byte[loginHash.length + 1];
        data[0] = 0x06;
        System.arraycopy(loginHash, 0, data, 1, loginHash.length);
        try {
            sendData(data);
            synchronized (this) {
                wait();
            }
            return lastAccepted;
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo error
        }
        return false;
    }

    public void accepted() {
        byte[] data = new byte[]{(byte) 0xF0};
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo to GUI?
        }
    }

    public void illegalAction() {
        byte[] data = new byte[]{(byte) 0xF1};
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo to GUI?
        }
    }

    public void ping() {
        byte[] data = new byte[]{(byte) 0xE0};
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo to GUI?
        }
        try {
            synchronized (this) {
                wait();//wait for pong response
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void pong() {
        byte[] data = new byte[]{(byte) 0xE1};
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo to GUI?
        }
    }

    public void setCard(int cardId) throws Exception {
        LOG.info("setCard" + cardId);
        if (cardId > 0xFFFF) {
            throw new Exception("CardId out of range");
        }
        if (!socket.isConnected()) {
            //throw error not connected
        }
        byte lb = 0;
        byte hb = 0;
        if (cardId < 0x100) {
            lb = (byte) cardId;
        } else {
            lb = (byte) (cardId % 0x100);
            hb = (byte) ((cardId - cardId % 0x100) / 0x100); // replaced lb with cardId % 0x100 because of java singned byte
        }
        byte[] data = new byte[]{0x02, lb, hb};
        try {
            sendData(data);
            synchronized (this) {
                wait();
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo fatal exception
        }
    }

    public void setTarget(int source, int target) {
        //todo check source and target 1..3
        if (!socket.isConnected()) {
            //todo throw error
            return;
        }
        byte[] data = new byte[]{0x03, (byte) source, (byte) target};
        try {
            sendData(data);
            synchronized (this) {
                wait();
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo throw error
        }
    }

    public void setFinished(boolean finished) {
        LOG.info("Finished!");
        if (!socket.isConnected()) {
            //todo throw error
            return;
        }
        byte[] data = new byte[]{0x04, finished ? (byte) 0x01 : (byte) 0x00};
        try {
            sendData(data);
            synchronized (this) {
                wait();
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo throw error
        }
    }

    public void concede() {
        if (!socket.isConnected()) {
            //todo throw error
            return;
        }
        byte[] data = new byte[]{0x05};
        try {
            sendData(data);
            synchronized (this) {
                wait();
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo throw error
        }

    }
    
    public void nextMatch(){
        if (!socket.isConnected()) {
            //todo throw error
            return;
        }
        byte[] data = new byte[]{0x07};
        try {
            sendData(data);
            synchronized (this) {
                wait();
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo throw error
        }
    }

    public void sendMessage(String message) {
        byte[] encodedString;
        try {
            encodedString = message.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo throw fatal
            return;
        }
        byte[] data = new byte[encodedString.length + 2];

        System.arraycopy(encodedString, 0, data, 1, encodedString.length);
        data[0] = (byte) 0x80;
        data[data.length - 1] = 0x00;
        try {
            sendData(data);
            synchronized (this) {
                wait();
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo throw fatal
        }

    }

    public void fatalDisconnect() {
        if (!socket.isConnected()) {
            //todo throw error
            return;
        }
        byte[] data = new byte[]{(byte) 0xFA};
        try {
            sendData(data);
            //todo disconnect socket
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //socket dead
        }
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void nonFatalDisconnect() {
        if (!socket.isConnected()) {
            //todo throw error
            return;
        }
        byte[] data = new byte[]{(byte) 0xFA};
        try {
            sendData(data);
            //todo disconnect socket
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //socket dead
        }
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void sendData(byte[] data) throws IOException {
        LOG.info(Arrays.toString(data));
        socket.getOutputStream().write(data);
    }
}
