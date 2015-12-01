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
    
    public WaitingPlayer(Player player){
        this.player = player;
        this.timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                Game.getInstance().findMatch(player);
            }
        }, 10*1000);
    }
    
}
