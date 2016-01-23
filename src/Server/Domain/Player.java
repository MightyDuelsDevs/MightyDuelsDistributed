package Server.Domain;

import Server.SocketManagerServer.SocketClient;
import Shared.Domain.Icon;
import java.io.Serializable;
import Shared.Domain.PlayerShared;
/**
 * An class containing attributes of a player
 */
public class Player extends PlayerShared implements Serializable {

    /**
     * The socketclient
     */
    private SocketClient socket;
    
    /**
     *
     * @param id Id of the player
     * @param username The username of the player
     * @param iconId The id of the icon of the player
     * @param rating The rating of the player
     * @param wins The wins of the player
     * @param losses The losses of the player
     * @param matches The amount of matches of the player
     */
    public Player(int id, String username, int iconId, int rating, int wins, int losses, int matches) {
        super(id, username, iconId, rating, wins, losses, matches);
    }

    /**
     * Sets the socketClient
     * 
     * @param socket
     */
    public void setSocket(SocketClient socket){
        this.socket = socket;
    }
    
    /**
     * Returns the socketClient
     * 
     * @return the socketclient
     */
    public SocketClient getSocket(){
        return socket;
    }
    
    /**
     * Sets the IconID of the player
     * @param iconId Sets the parameter icon id.
     */
    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    /**
     * Sets the rating of the player
     * @param rating Sets the rating of the player
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * set increment the amount of matches played
     */
    public void setMatches() {
        this.matches++;

    }
    
    /**
     * increment the number op wins of the player
     */
    public void setWins() {
        this.wins++;
    }

    /**
     * increment the number of losses the player has made to total.
     *
     */
    public void setLoses() {
        this.losses++;
    }

    /**
     * Change the icon of the player
     * @param icon the new icon
     */
    public void changeIcon(Icon icon) {
        this.iconId = icon.getId();
    }

    /**
     * set the id of the player
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }
}
