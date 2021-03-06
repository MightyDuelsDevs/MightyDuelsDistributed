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
import Server.Domain.WaitingPlayer;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rick Rongen, www.R-Ware.tk
 */
public class SocketClient {

    private static final Logger LOG = Logger.getLogger(SocketClient.class.getName());

    private Socket socket;

    private Player player;
    private Match match;
    private Hero hero;

    private Thread inputThread;
    private boolean closed = false;
    private boolean lastAccepted = false;

    /**
     *
     * @param socket, is the connecting element, given by the server.
     */
    public SocketClient(Socket socket) {
        this.socket = socket;
        inputThread = new Thread(() -> inputHandler());
        inputThread.start();
    }

    private void inputHandler() {
        byte[] identifier = new byte[4];
        try {
            socket.getInputStream().read(identifier, 0, 4);
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!(identifier[0] == 0x4D && identifier[1] == 0x44 && identifier[2] == 0x56 && identifier[3] == 0x01)) {
            try {
                //check if the conneciton is good
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            closed = true;
            return;
        }
        connAccepted();
        InputStream in;
        try {
            in = socket.getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            closed = true;
            //todo throw error
            return;
        }

        while (socket.isConnected() && !socket.isClosed()) {
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
            //LOG.info("Command: " + input);
            switch (input) {
                case -1:
                    //todo read error
                    break;
                case 0x01://login
                    LOG.info("Reading hash");
                    int hashlength = 32;//todo tbd
                    byte[] hash = new byte[hashlength];

                    try {
                        in.read(hash, 0, hashlength);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    LOG.info("Hash done");
//                    StringBuilder sb = new StringBuilder();
//                    for(byte d : hash){
//                        String hex = Integer.toHexString(0xff & d);
//                        if(hex.length() == 1) sb.append('0');
//                        sb.append(hex);
//                    }
                    player = SocketManager.getInstance().getPlayer(hash);
                    if (player == null) {
                        loginDenied();
                        closed = true;
                        try {
                            socket.close();

                        } catch (IOException ex) {
                            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return;
                    }
                    loginAccepted();
                    player.setSocket(this);
                    new WaitingPlayer(player);
                    break;
                case 0x02://set_card
                    LOG.info("setCard");
                    int cardId;
                    try {
                        cardId = readInt16(in);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
                        illegalAction();
                        return;
                    }

                    if (hero.playCard(CardDeckController.getCard(cardId))) {
                        accepted();
                    } else {
                        illegalAction();
                    }
                    break;

                case 0x03://set_target
                    int source,
                     target;
                    try {
                        source = in.read();
                        target = in.read();
                    } catch (IOException ex) {
                        Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
                        //todo error
                        continue;
                    }

                    if (source < 0 || target < 0) {
                        illegalAction();
                        return;
                    }
                    ITarget itarget;
                    Hero opponend = match.getHero1() == hero ? match.getHero2() : match.getHero1();
                    if (target == 0) {
                        itarget = opponend;
                    } else if (target == 1) {
                        List<Minion> om = opponend.getMinions();
                        if (om.size() >= 1) {
                            itarget = om.get(0);
                        } else {
                            illegalAction();
                            continue;
                        }
                    } else {
                        List<Minion> om = opponend.getMinions();
                        if (om.size() >= 2) {
                            itarget = om.get(1);
                        } else {
                            illegalAction();
                            continue;
                        }
                    }
                    if (source == 0) {
                        if (target != 0) {
                            illegalAction();//player can't attack minion
                            continue;
                        }
                        //todo? hero.setTarget(itarget);
                    } else {
                        List<Minion> mm = hero.getMinions();
                        if (source == 1 && mm.size() >= 1) {
                            mm.get(0).setITarget(itarget);
                        } else if (source == 2 && mm.size() >= 2) {
                            mm.get(1).setITarget(itarget);
                        } else {
                            illegalAction();
                            continue;
                        }
                    }
                    accepted();
                    //0=face 1=min1 2-min2
                    break;

                case 0x04://set finished
                    LOG.info("finished");
                    int finished;
                    try {
                        finished = in.read();
                    } catch (IOException ex) {
                        Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
                        //todo error
                        continue;
                    }
                    hero.setFinished(finished == 0x01);
                    accepted();
                    break;
                case 0x05://Consede
                    accepted();
                    match.concede(hero);
                    break;
                case 0x06://LOGIN_SPEC
                    LOG.info("Reading hash");
                    int hashlength2 = 32;//todo tbd
                    byte[] hash2 = new byte[hashlength2];

                    try {
                        in.read(hash2, 0, hashlength2);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    LOG.info("Hash done");
//                    StringBuilder sb = new StringBuilder();
//                    for(byte d : hash){
//                        String hex = Integer.toHexString(0xff & d);
//                        if(hex.length() == 1) sb.append('0');
//                        sb.append(hex);
//                    }
                    player = SocketManager.getInstance().getPlayer(hash2);
                    if (player == null) {
                        loginDenied();
                        closed = true;
                        try {
                            socket.close();

                        } catch (IOException ex) {
                            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return;
                    }
                    loginAccepted();
                    player.setSocket(this);
                    Match m = Game.getInstance().findMatch();
                    if (m == null) {
                        fatalDisconnect();
                    }
                    match = m;
                    match.addSpectator(player);
                    break;
                case 0x07://NEXT_MATCH
                    accepted();
                    Match m2 = Game.getInstance().findMatch();
                    if (m2 == null) {
                        fatalDisconnect();
                    }
                    if (match != null) {
                        match.removeSpectator(player);
                    }
                    match = m2;
                    match.addSpectator(player);
                    break;
                case 0x80://MESSAGE
                    ByteBuffer mbuf = ByteBuffer.allocate(1024);
                    int message = -1;
                    try {
                        message = in.read();
                    } catch (IOException ex) {
                        Logger.getLogger(SocketClient.class.getName() + "-" + player.getUsername()).log(Level.SEVERE, null, ex);
                    }
                    while (message != 0x00) {
                        if (message == -1) {
                            //todo throw error
                        }
                        mbuf.put((byte) message);
                        try {
                            message = in.read();
                        } catch (IOException ex) {
                            Logger.getLogger(SocketClient.class.getName() + "-" + player.getUsername()).log(Level.SEVERE, null, ex);
                        }
                    }
                    String mes;
                    try {
                        mes = new String(mbuf.array(), 0, mbuf.position(), "UTF-8");
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(SocketClient.class.getName() + "-" + player.getUsername()).log(Level.SEVERE, null, ex);
                        //todo fatal
                        continue;
                    }

                    match.forwardMessage(mes, hero);
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
                    break;
                case 0xF1://ILLEGAL_ACTION
                    lastAccepted = false;
                    synchronized (this) {
                        notify();
                    }
                    break;
                case 0xFA://fatal disconnect
                    if (hero.getHitPoints() > 0) {
                        match.concede(hero);
                    }
                    //todo send message
                    break;
                case 0xFB://non fatal disconnect

                    //todo send message
                    break;
                default:
                    LOG.info("Unkown command: " + input);
                    break;
            }

        }

    }

    /**
     * Boolean method checking if connection is closed
     *
     * @return Returns the true or false value of the connection state
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * This function is not initialised
     */
    public void stop() {

    }

    /**
     * Sends the confirmation of the connection back to the server.
     */
    public void connAccepted() {
        byte[] data = new byte[]{(byte) 0x01};
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo end connection?
        }
    }

    /**
     * Sends the acceptance of the login credentials.
     */
    public void loginAccepted() {
        byte[] data = new byte[]{(byte) 0x02};
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo end connection?
        }
    }

    /**
     * Sends the confirmation of the authentication failure to the server.
     */
    public void loginDenied() {
        byte[] data = new byte[]{(byte) 0x03};
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

    /**
     * Sends the confirmation of the connection back to the server.
     */
    public void accepted() {
        byte[] data = new byte[]{(byte) 0xF0};
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo end connection?
        }
    }

    public void illegalAction() {
        byte[] data = new byte[]{(byte) 0xF1};
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo end connection?
        }
    }

    /**
     * Pings the server
     */
    public void ping() {
        byte[] data = new byte[]{(byte) 0xE0};
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
            //todo end connection?
        }
        try {
            synchronized (this) {
                wait();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Pongs the server
     */
    public void pong() {
        byte[] data = new byte[]{(byte) 0xE1};
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void newMatch(Match match, String username, int id) {
        this.match = match;
        byte[] usernameEncoded;
        try {
            usernameEncoded = username.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //fatal error
            return;
        }
        byte[] int16Id = int16Encode(id);
        byte[] data = new byte[usernameEncoded.length + 4];
        data[0] = 0x04;
        System.arraycopy(usernameEncoded, 0, data, 1, usernameEncoded.length);
        data[usernameEncoded.length + 1] = 0x00;
        System.arraycopy(int16Id, 0, data, usernameEncoded.length + 2, 2);
        try {
            sendData(data);
            synchronized (this) {
                wait();
            }
            if (lastAccepted) {
                //todo check lastaccepted
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //fatal error
        }
    }

    public void joinMatch(String p1Name, String p2Name, int p1Icon, int p2Icon) {
        byte[] p1Encoded;
        byte[] p2Encoded;
        try {
            p1Encoded = p1Name.getBytes("UTF-8");
            p2Encoded = p2Name.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        ByteBuffer buffer = ByteBuffer.allocate(p1Encoded.length + p2Encoded.length + 4 + 2 + 1);
        buffer.put((byte) 0x0A);
        buffer.put(p1Encoded);
        buffer.put((byte) 0x00);
        buffer.put(int16Encode(p1Icon));
        buffer.put(p2Encoded);
        buffer.put((byte) 0x00);
        buffer.put(int16Encode(p2Icon));
        try {
            sendData(buffer.array());
            synchronized (this) {
                wait();
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void turnEnd(int card) {
        byte[] data = new byte[3];
        data[0] = 0x05;
        byte[] int16 = int16Encode(card);
        data[1] = int16[0];
        data[2] = int16[1];
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //fatal error
        }

    }

    public void specTurnEnd(int p1Card, int p2Card) {
        byte[] p1 = int16Encode(p1Card);
        byte[] p2 = int16Encode(p2Card);
        byte[] data = new byte[5];
        data[0] = 0x0B;
        data[1] = p1[0];
        data[2] = p1[1];
        data[3] = p2[0];
        data[4] = p2[1];
        try {
            sendData(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addMinion(boolean self, int pos, int card) {
        byte totalPos = (byte) (self ? pos : pos + 2);
        byte[] int16Card = int16Encode(card);
        byte[] data = new byte[4];
        data[0] = 0x06;
        data[1] = totalPos;
        data[2] = int16Card[0];
        data[3] = int16Card[1];
        try {
            sendData(data);
//            synchronized(this){
//                wait();
//            }
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //fatal error
        }
    }

    public void setHealth(boolean self, int pos, int value) {
        byte totalPos = (byte) (self ? pos : pos + 3);
        if (value < 0) {
            value = 0;
        }
        byte[] data = new byte[3];
        data[0] = 0x07;
        data[1] = totalPos;
        data[2] = (byte) value;
        try {
            sendData(data);
//            synchronized(this){
//                wait();
//            }
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //fatal error
        }
    }

    public void newTurn(Integer[] cards) {
        LOG.info("New turn " + Arrays.toString(cards));
        byte[] data = new byte[7];
        data[0] = 0x08;
        for (int i = 0; i < 3; i++) {
            byte[] card = int16Encode(cards[i]);
            data[i * 2 + 1] = card[0];
            data[i * 2 + 2] = card[1];
        }
        try {
            sendData(data);

//            synchronized(this){
//                wait();
//            }
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //fatal error
        }
    }

    //0=lose 1=tie 2=win
    public void matchEnd(int win) {
        byte[] data = new byte[2];
        data[0] = 0x09;
        data[1] = (byte) win;
        try {
            sendData(data);
//            synchronized(this){
//                wait();
//            }
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //fatal error
        }
    }

    public void sendMessage(String message) {
        byte[] encodedString;
        try {
            encodedString = message.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //todo throw fatal
            return;
        }
        byte[] data = new byte[encodedString.length + 2];
        data[0] = (byte) 0x80;
        System.arraycopy(encodedString, 0, data, 1, encodedString.length);
        data[data.length - 1] = 0x00;
        try {
            sendData(data);
            synchronized (this) {
                wait();
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //todo throw fatal
        }

    }

    public void fatalDisconnect() {
        if (hero.getHitPoints() > 0) {
            match.concede(hero);
        }
        if (!socket.isConnected()) {
            //todo throw error
            return;
        }
        byte[] data = new byte[]{(byte) 0xFA};
        try {
            sendData(data);
            //todo disconnect socket
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            //socket dead
        }
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int readInt16(InputStream in) throws IOException {
        int lb = in.read();
        int ub = in.read();
        return lb + ub * 0x100;
    }

    private byte[] int16Encode(int input) {
        byte[] data = new byte[2];
        if (input < 0x100) {
            data[0] = (byte) input;
            data[1] = 0;
        } else {
            data[0] = (byte) (input % 0x100);
            data[1] = (byte) ((input - input % 0x100) / 0x100);
        }
        return data;
    }

    private synchronized void sendData(byte[] data) throws IOException {
        LOG.info(Arrays.toString(data));
        socket.getOutputStream().write(data);
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }
}
