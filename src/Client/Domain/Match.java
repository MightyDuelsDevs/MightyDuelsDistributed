package Client.Domain;

import Client.RMI.RMIClient;
import Client.SocketManagerClient.SocketManager;
import Shared.Domain.PlayerShared;
import java.util.Timer;
//import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * Controller for an match between two players
 */
public class Match {

    private static final Logger log = Logger.getLogger(Match.class.getName());

    private Game game;

    private int turns;
    private GameState gameState;

    private final PlayerShared player1;
    private PlayerShared player2;

    private final Hero hero1;
    private Hero hero2;

    Timer timer;

    /**
     * Create a new instance of match with P1 as the first player/hero
     *
     * @param token1, token of the first player.
     * @param token2, token of the second player.
     */
    public Match(String token1, String token2) {
        game = Game.getInstance();
        player1 = game.getPlayer(token1);
        player2 = game.getPlayer(token2);

        hero1 = new Hero(this, player1, game.getDeck(token1));//todo deck
    }

    /**
     * Get the current gamestate
     *
     * @return the current game state
     */
    public GameState getGameState() {
        return this.gameState;
    }

    /**
     * Concede a match, this will make a player force lose
     *
     * @param hero The hero that concedes
     */
    public void concede(Hero hero) {
//        SocketManager.getInstance().concede();
    }

    /**
     * Method that sets the target of an attack.
     * @param source, the source of the attack. This in an integer that determines which minion attacks.
     * @param target, the target of the attack. This is an integer that determines which target is attacked.
     */
    public void setITarget(int source, int target) {
//TODO
    }
}
