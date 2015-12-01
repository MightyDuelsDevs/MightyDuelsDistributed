package Server.Domain;

import java.util.List;

/**
 * An class for storing info about the current game instance
 */
public class Game {

    private static Game instance;
    private List<Player> players;
    private List<Match> matches;

    /**
     * Initialise the game instace
     */
    public Game() {
        // Exists only to defeat instantiation.        
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

    public void findMatch(Player player) {
        players.remove(player);
        Player closestPlayer = null;
        for (Player p : players) {
            if (closestPlayer == null){
                closestPlayer = p;
            }
            else if(closestPlayer == null){//TODO
            }
        }
    }

}
