/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Domain;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Ramòn Janssen
 */
public class WaitingPlayer {
    private final Player player;
    private final Timer timer;
    private Game game;
    
    public WaitingPlayer(Player player){
        this.game = Game.getInstance();
        this.player = player;
        this.timer = new Timer();
        
        game.addWaitingPlayer(player);
        
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if(game.findMatch(player)){
                    this.cancel();
                }
            }
        }, 5*1000, 5*1000);
    }
    
}
