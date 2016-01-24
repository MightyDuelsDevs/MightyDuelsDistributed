package Server.Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An class for storing info about the current game instance
 */
public class Game {

    private static Game instance;
    private final List<Player> waitingPlayers;
    private final List<Match> matches;
    private final Random random;

    /**
     * Initialise the game instance
     */
    public Game() {
        waitingPlayers = new ArrayList<>();
        matches = new ArrayList<>();
        random = new Random();
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
     * @return 
     */
    public synchronized boolean findMatch(Player player) {
        if(!waitingPlayers.contains(player)){
            return true;//player is already in an match
        }
        
        
        
        Player closestPlayer = null;
        for (Player p : waitingPlayers) {
            if (p == player) {
                continue;
            }
            if (closestPlayer == null) {
                closestPlayer = p;
            } else if (Math.abs(closestPlayer.getRating() - player.getRating()) > Math.abs(p.getRating() - player.getRating()) && !player.equals(p)) {
                closestPlayer = p;
            }
        }

        if (closestPlayer != null) {
            matches.add(new Match(player, closestPlayer));

            waitingPlayers.remove(player);
            waitingPlayers.remove(closestPlayer);
            return true;
        }
        return false;
    }

    /**
     * 
     * @param waitingPlayer add a player to the waiting list
     */
    public void addWaitingPlayer(Player waitingPlayer) {
        waitingPlayers.add(waitingPlayer);
    }

    /**
     * Method that returns the amount of matches in the game. This is used to
     * check if the player is able to spectate.
     *
     * @return amount of matches
     */
    public int countMatches() {
        return matches.size();
    }

    /**
     * Find an random match to spectate
     * @return the match to specate or null if no match was found
     */
    public Match findMatch() {
        if (matches.size() < 1) {
            return null;
        }
        return matches.get(random.nextInt(matches.size()));
    }
    
    /**
     * 
     * @param match the match you want  to remove from the match list
     */
    public void removeMatch(Match match){
        matches.remove(match);
    }
}
