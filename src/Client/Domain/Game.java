package Client.Domain;

import Client.RMI.RMIClient;
import Shared.Domain.PlayerShared;
import Shared.Domain.Deck;
import Shared.Domain.Icon;
import Shared.Domain.Card;
import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An class for storing info about the current game instance
 */
public class Game {

    private static Game instance;
    private List<Icon> icons;
    private List<Card> cards;
    private PlayerShared player;
    private Match match;
    private RMIClient client;
    private String token;

    public PlayerShared getPlayer() {
        return player;
    }

    /**
     * Initialise the game instace
     */
    public Game() {
        client = RMIClient.getInstance();
        // Exists only to defeat instantiation.        
    }

    /**
     * get the icons the game knows of
     *
     * @return the list of icons
     * @deprecated moved to Controller.PlayerIconController
     */
    public List<Icon> getIcon() {
        return this.icons;
    }

    /**
     * return instance??instance=new game();
     *
     * @return this game instance
     */
    public synchronized static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    /**
     *
     * @param player
     * @return match creates a match with the param player
     */
    public Match startMatch() {//TODO
        this.match = new Match("token1", "token2");
        return this.match;
    }

    /**
     *
     * @param username the player username
     * @param password the player password
     * @return player returns the player that gets logged in
     */
    public PlayerShared loginPlayer(String Displayname, String Password) {
        try {
            token = client.loginPlayer(Displayname, Password);
            player = client.getPlayer(token);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (token.isEmpty()) {
            return null;
        }
        return getPlayer(token);
    }

    /**
     *
     * @param username the player username
     * @param password the player password
     * @return boolean returns true when the signUp has succeeded and false when
     * the username already exist
     */
    public int signUpPlayer(String email, String displayname, String password, String passcheck) {
        try {
            return client.signUpPlayer(email, displayname, password, passcheck);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    //Methods from MainScreenProvider
    public byte[] getNewMatch(String token) {
        try {
            return client.getNewMatch(token);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Get all the cards the game knows off
     *
     * @return the list of 30 cards
     * @deprecated moved to Controller.CardDeckController
     */
    public List<Card> getCards() {
        try {
            return client.getCards();
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<Deck> getDecks(String token) {
        try {
            return client.getDecks(token);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Deck getDeck(String token) {
        try {
            return client.getDeck(token);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<Icon> getIcons(String token) {
        try {
            return client.getIcons(token);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean setIcon(String token, int iconID) {
        try {
            return client.setIcons(token, iconID);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean addDeck(String token, List<Card> cards, String name) {
        try {
            return client.addDeck(token, cards, name);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean removeDeck(String token, String name) {
        try {
            return client.removeDeck(token, name);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public PlayerShared getPlayer(String token) {
        try {
            return client.getPlayer(token);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String getToken() {
        return token;
    }
}
