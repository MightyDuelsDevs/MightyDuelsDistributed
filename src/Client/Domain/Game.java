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

    /**
     * Method that returns the player that is logged in in the client.
     * @return returns the logged in player.
     */
    public PlayerShared getPlayer() {
        return player;
    }

    /**
     * Initialise the game instance
     */
    public Game() {
        client = RMIClient.getInstance();
        try {
            cards = client.getCards();
            // Exists only to defeat instantiation.        
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
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
     * @return match creates a match with the param player
     */
    public byte[] startMatch() {
        //TODO
        try{
            return client.getNewMatch(token);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Method that logs a player in the game using RMI and the server.
     * @param Displayname, the display name that the user put in.
     * @param Password, the password that the user put in.
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
     * Method that registers a player in a database.
     * @param email, the email the player uses.
     * @param password, the player password.
     * @param displayname, the player display name.
     * @param passcheck, The check which determines if the user entered his password correctly.
     * @return integer that determines if the sign up is completed successful.
     * This integer can also identify different error messages.
     */
    public int signUpPlayer(String email, String displayname, String password, String passcheck) {
        try {
            return client.signUpPlayer(email, displayname, password, passcheck);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    /**
     * Method that gets a byte array that represents a method.
     * @param token, token that represents a player.
     * @return a byte array that represents a match.
     */
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

    /**
     * Method that returns all the decks of a certain player.
     * @param token, token that represents a player.
     * @return a list of the decks the player uses.
     */
    public List<Deck> getDecks(String token) {
        try {
            return client.getDecks(token);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Method that returns the selected deck of a player.
     * @param token, token that represents a player.
     * @return the selected deck of a player.
     */
    public Deck getDeck(String token) {
        try {
            return client.getDeck(token);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Method that returns all the icons a player can select.
     * @param token, token that represents a player.
     * @return a list of icons the player can use. 
     */
    public List<Icon> getIcons(String token) {
        try {
            return client.getIcons(token);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Method that selects an icon in the database.
     * This will make sure the player can see his selected icon when he restarts the game.
     * @param token, token that represents a player.
     * @param iconID, the ID of the token that the player selected.
     * @return a boolean that determines if the operation has succeeded.
     */
    public boolean setIcon(String token, int iconID) {
        try {
            boolean result = client.setIcons(token, iconID);
            player = client.getPlayer(token);
            return result;
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Method that adds a random deck to a certain player.
     * @param token, token that represents a player.
     * @param name, the name of the deck that is created.
     * @return a boolean that determines if the operation has succeeded.
     */
    public boolean addDeck(String token, String name) {
        try {
            return client.addDeck(token, name);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Method that removes a certain deck from the database.
     * @param token, token that represents a player.
     * @param id, the ID of the to be removed deck.
     * @return a boolean that determines if the operation has succeeded.
     */
    public boolean removeDeck(String token, int id) {
        try {
            return client.removeDeck(token, id);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Method that selects a deck for a player.
     * @param token, token that represents a player.
     * @param deckId, the ID of the deck.
     * @return a boolean that determines if the operation has succeeded.
     */
    public boolean setSelectedDeck(String token, int deckId) {
        try {
            return client.setSelectedDeck(token, deckId);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Method that returns a player that corresponds to the token.
     * @param token, token that represents a player.
     * @return a player that corresponds with the input token.
     */
    public PlayerShared getPlayer(String token) {
        try {
            return client.getPlayer(token);
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Method that returns the token of the player that is logged in.
     * @return a string that represents the player.
     */
    public String getToken() {
        return token;
    }
    
    /**
     * Method that returns if there a boolean that represents the matches.
     * If there is more than 0 it returns true.
     * @return a boolean if there is more than zero games.
     */
    public boolean isSpectatePossible(){
        try {
            return client.isPossibleSpectate();
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
