package Client.Domain;

import Client.RMI.RMIClient;
import Shared.Domain.PlayerShared;
import Shared.Domain.PlayerShared;
import Shared.Domain.Card;
import Shared.Domain.Icon;
import java.util.List;
import Shared.Domain.Icon;
import Shared.Domain.Card;
import java.util.List;

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
        this.match = new Match("token");
        return this.match;
    }

    /**
     *
     * @param username the player username
     * @param password the player password
     * @return player returns the player that gets logged in
     */
    public PlayerShared loginPlayer(String Displayname, String Password) {
        token = client.loginPlayer(Displayname, Password);
        if (token.isEmpty()){
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
        return client.signUpPlayer(email, displayname, password, passcheck);
    }

    //Methods from MainScreenProvider
    public String getNewMatch(String token) {
        return client.getNewMatch(token);
    }

    /**
     * Get all the cards the game knows off
     *
     * @return the list of 30 cards
     * @deprecated moved to Controller.CardDeckController
     */
    public List<Card> getCards() {
        return client.getCards();
    }

    public List<Server.Domain.Deck> getDeck(String token) {
        return client.getDeck(token);
    }

    public List<Icon> getIcons(String token) {
        return client.getIcons(token);
    }

    public boolean setIcon(String token, int iconID) {
        return client.setIcons(token, iconID);
    }

    public boolean addDeck(String token, List<Card> cards, String name) {
        return client.addDeck(token, cards, name);
    }

    public boolean removeDeck(String token, String name) {
        return client.removeDeck(token, name);
    }

    public PlayerShared getPlayer(String token) {
        return client.getPlayer(token);
    }
    
    public String getToken(){
        return token;
    }
}
