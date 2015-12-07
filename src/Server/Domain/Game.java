package Server.Domain;

import java.util.List;

/**
 * An class for storing info about the current game instance
 */
public class Game {

    private static Game instance;
    private List<Player> waitingPlayers;
    private List<Match> matches;

    /**
     * Initialise the game instance
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

    public boolean findMatch(Player player) {
        Player closestPlayer = null;
        for (Player p : waitingPlayers) {
            if (closestPlayer == null){
                closestPlayer = p;
            }
            else if(Math.abs(closestPlayer.getRating() - player.getRating()) > Math.abs(p.getRating() - player.getRating())){
                closestPlayer = p;
            }
        }
        
        if(closestPlayer != null){
            matches.add(new Match(player, closestPlayer));
            
            waitingPlayers.remove(player);
            waitingPlayers.remove(closestPlayer);
            return true;
        }
        
        waitingPlayers.remove(player);
        return false;
    }

    public void addWaitingPlayer(Player waitingPlayer){
        waitingPlayers.add(waitingPlayer);
    }
}
