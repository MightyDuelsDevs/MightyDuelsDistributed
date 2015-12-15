package Server.Domain;

import Server.Controller.CardDeckController;
import java.util.List;
import java.util.Timer;
import java.util.logging.Logger;

import Shared.Domain.Card;
import Shared.Domain.MinionCard;
import Shared.Domain.HeroCard;
import Shared.Domain.Deck;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.logging.Level;

/**
 * Controller for an match between two players
 */
public class Match {

    private static final Logger log = Logger.getLogger(Match.class.getName());
    ;
    
	private int turns;
    private GameState gameState;

    private final Player player1;
    private Player player2;

    private final Hero hero1;
    private Hero hero2;
    
    private boolean pingP1 = true;

    private ScheduledExecutorService timer;

    /**
     * check's the players health and updates the gamestate according to their
     * health
     */
    private void determineGameState() {
        if (player2 == null) {
            gameState = GameState.Waiting;
            return;
        }
        if (hero1.getHitPoints() <= 0 ^ hero2.getHitPoints() <= 0) {//^= XOR true+false = true, false+false = false and true+true=false
            gameState = GameState.Defined;
            timer.shutdown();
            if(hero1.getHitPoints()>0){
                player1.getSocket().matchEnd(2);
                player2.getSocket().matchEnd(0);
            }else if(hero2.getHitPoints()>0){
                player1.getSocket().matchEnd(0);
                player2.getSocket().matchEnd(2);
            }
            return;
        }
        if (hero1.getHitPoints() > 0 && hero2.getHitPoints() > 0) {
            gameState = GameState.Active;
            return;
        }
        gameState = GameState.Tie;
        timer.shutdown();
        player1.getSocket().matchEnd(1);
        player2.getSocket().matchEnd(1);
    }

    /**
     * Processes one turn. (card's played, minion attacks and gamestate update)
     */
    private void processTurn() {
        //log.info(String.format("Processing turn %d of player '%s' and '%s'.",turns,player1.getUsername(),player2.getUsername()));
        Card p1 = hero1.getCardPlayed();
        Card p2 = hero2.getCardPlayed();

            //check minion card
        //create new minions
        if (p1 instanceof MinionCard) {
            //log.info(String.format("Adding minion %s to %s",p1.getName(),player1.getUsername()));
            Minion m = new Minion((MinionCard) p1);
            List<Minion> min1 = hero1.getMinions();
            if(min1.size()>=2){
                //todo not possible
            }else{
                min1.add(m);
                if(min1.size()==1){
                    player1.getSocket().addMinion(true, 1, p1.getId());
                    player2.getSocket().addMinion(false, 1, p1.getId());
                }else{
                    player1.getSocket().addMinion(true, 2, p1.getId());
                    player2.getSocket().addMinion(false, 2, p1.getId());
                }
            }
                //hero1.addMinion(m);
            
            //todo set target somhow?
        }
        if (p2 instanceof MinionCard) {
            //log.info(String.format("Adding minion %s to %s",p2.getName(),player2.getUsername()));
            Minion m = new Minion((MinionCard) p2);
            List<Minion> min2 = hero2.getMinions();
            if(min2.size()>=2){
                //todo not possible
            }else{
                min2.add(m);
                if(min2.size()==1){
                    player2.getSocket().addMinion(true, 1, p1.getId());
                    player1.getSocket().addMinion(false, 1, p1.getId());
                }else{
                    player2.getSocket().addMinion(true, 2, p1.getId());
                    player1.getSocket().addMinion(false, 2, p1.getId());
                }
            }
                //hero2.addMinion(m);
            //todo set target somhow?
        }

            //log.info("Processing minion attacks. Obtaining minions");
        //get all active minions
        List<Minion> p1min = hero1.getMinions();
        List<Minion> p2min = hero2.getMinions();

            //log.info("Filter non hero attack, attack minions");
        //all not player attacks            
        p1min.stream().filter((m) -> m.getITarget() != hero2).forEach((m) -> {
            m.attack();});//attack all not players
        p2min.stream().filter((m) -> m.getITarget() != hero1).forEach((m) -> {
            m.attack();});//attack all not players

            //log.info("Remove dead minions");
        //remove dead minions
        p1min.forEach((m)->{
            if(m.getHitPoints()<=0){
                player1.getSocket().setHealth(true, p1min.indexOf(m)==0?2:3, 0);
                player2.getSocket().setHealth(false, p1min.indexOf(m)==0?2:3, 0);
            }
        });
        p2min.forEach((m)->{
            if(m.getHitPoints()<=0){
                player1.getSocket().setHealth(false, p2min.indexOf(m)==0?2:3, 0);
                player2.getSocket().setHealth(true, p2min.indexOf(m)==0?2:3, 0);
            }
        });
        p1min.removeIf((m) -> m.getHitPoints() <= 0);
        p2min.removeIf((m) -> m.getHitPoints() <= 0);

            //log.info("Filter hero attacks, attack hero");
        //all minion to player attacks
        p1min.stream().filter((m) -> m.getITarget() == hero2).forEach((m) -> m.attack());//attack hero2
        p2min.stream().filter((m) -> m.getITarget() == hero1).forEach((m) -> m.attack());//attack hero1

            //log.info("Process HeroCard attacks");
        //attack using cards
        //attack player card
        if (p1 instanceof HeroCard) {
            //log.info(String.format("%s attacks %s with %s",player1.getUsername(),player2.getUsername(),p1.getName()));
            HeroCard p1h = (HeroCard) p1;

            int hp = hero2.getHitPoints();
            //Matthijs
            int pSchield = 0, mSchield = 0, healing = 0, physicalDamageDone, magicalDamageDone, totalValue;
            if (p2 instanceof HeroCard) {
                //log.info(String.format("%s blocks %s with %s",player2.getUsername(),player1.getUsername(),p2.getName()));
                HeroCard p2h = (HeroCard) p2;
                pSchield = p2h.getPhysicalBlock();
                mSchield = p2h.getMagicalBlock();
                healing = p2h.getHealValue();
            }
            physicalDamageDone = p1h.getPhysicalDamage() - pSchield;
            if (physicalDamageDone < 0) {
                physicalDamageDone = 0;
            }
            magicalDamageDone = p1h.getMagicalDamage() - mSchield;
            if (magicalDamageDone < 0) {
                magicalDamageDone = 0;
            }
            totalValue = (physicalDamageDone + magicalDamageDone) - healing;
            if (hp - totalValue > 50) {
                hero2.setHitPoints(50);
            } else {
                hero2.setHitPoints(hp - totalValue);
            }

            log.info(String.format("hero 2 heeft %s hp over", hero2.getHitPoints()));
            //Matthijs
        }
        if (p2 instanceof HeroCard) {
            //log.info(String.format("%s attacks %s with %s",player2.getUsername(),player1.getUsername(),p2.getName()));
            HeroCard p2h = (HeroCard) p2;
            int hp = hero1.getHitPoints();
            //Matthijs
            int pSchield = 0, mSchield = 0, healing = 0, physicalDamageDone, magicalDamageDone, totalValue;
            if (p1 instanceof HeroCard) {
                //log.info(String.format("%s blocks %s with %s",player1.getUsername(),player2.getUsername(),p1.getName()));
                HeroCard p1h = (HeroCard) p1;
                pSchield = p1h.getPhysicalBlock();
                mSchield = p1h.getMagicalBlock();
                healing = p1h.getHealValue();
            }
            physicalDamageDone = p2h.getPhysicalDamage() - pSchield;
            if (physicalDamageDone < 0) {
                physicalDamageDone = 0;
            }
            magicalDamageDone = p2h.getMagicalDamage() - mSchield;
            if (magicalDamageDone < 0) {
                magicalDamageDone = 0;
            }
            totalValue = (physicalDamageDone + magicalDamageDone) - healing;
            if (hp - totalValue > 50) {
                hero1.setHitPoints(50);
            } else {
                hero1.setHitPoints(hp - totalValue);
            }
            log.info(String.format("hero 1 heeft %s hp over", hero1.getHitPoints()));
            //Matthijs
        }
        log.info("Turn finished");
        
        
        player1.getSocket().setHealth(true, 1, hero1.getHitPoints());
        player1.getSocket().setHealth(false, 1, hero2.getHitPoints());
        player2.getSocket().setHealth(true, 1, hero2.getHitPoints());
        player2.getSocket().setHealth(false, 1, hero1.getHitPoints());
        
        for (int i = 0; i < Math.min(hero1.getMinions().size(),2); i++) {
            player1.getSocket().setHealth(true, i+1, hero1.getMinions().get(i).getHitPoints());
            player2.getSocket().setHealth(false, i+1, hero1.getMinions().get(i).getHitPoints());
        }
        for (int i = 0; i < Math.min(hero2.getMinions().size(),2); i++) {
            player1.getSocket().setHealth(false, i+1, hero2.getMinions().get(i).getHitPoints());
            player2.getSocket().setHealth(true, i+1, hero2.getMinions().get(i).getHitPoints());
        }
        
        //todo here or somwere else?
        determineGameState();
        //Matthijs
        hero1.setFinished(false);
        hero2.setFinished(false);
        
        //Matthijs
        turns++;
    }

    /**
     * Create a new instance of match with P1 as the first player/hero
     *
     * @param P1 The first player
     * @param P2 The second player
     */
    public Match(Player P1, Player P2) {
        player1 = P1;
        player2 = P2;
        hero1 = new Hero(this, player1, CardDeckController.getDeckFromPlayer(player1.getId()));
        hero2 = new Hero(this, player2, CardDeckController.getDeckFromPlayer(player2.getId()));
        player1.getSocket().setHero(hero1);
        player2.getSocket().setHero(hero2);
        player1.getSocket().newMatch(this, player2.getUsername(), player2.getIconId());
        player2.getSocket().newMatch(this, player1.getUsername(), player1.getIconId());
        
        
        log.info("New Turn! " + player1.getUsername() + " " + player2.getUsername());
        determineGameState();
        hero1.pullCards();
        hero2.pullCards();
        //send all card ids
        player1.getSocket().newTurn(hero1.getInHand().stream().map((i)->i.getId()).toArray(Integer[]::new));
        player2.getSocket().newTurn(hero2.getInHand().stream().map((i)->i.getId()).toArray(Integer[]::new));
        
        determineGameState();
        timer = Executors.newScheduledThreadPool(1);
        
        timer.scheduleAtFixedRate(new Runnable(){

            @Override
            public void run() {
                //ping one or the other to detect a dead connection
                //(pingP1 ? player1 : player2).getSocket().ping();
                
                //pingP1 = !pingP1;
                
                if (hero1.getFinished() && hero2.getFinished()) {
                    player1.getSocket().turnEnd(hero2.getCardPlayed().getId());
                    player2.getSocket().turnEnd(hero1.getCardPlayed().getId());
                    processTurn();
                    try {
                        Thread.sleep(1000);//wait a second before showing new cards
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(gameState == GameState.Tie || gameState == GameState.Defined){
                        timer.shutdown();
                    }
                    startTurn();
                }
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
        
        
    }

    /**
     * Get the amount of turns passed
     *
     * @return The amount of turns
     */
    public int getTurns() {
        return this.turns;
    }

    /**
     * Get the first hero
     *
     * @return the first hero object
     */
    public Hero getHero1() {
        return hero1;
    }

    /**
     * Get the second hero
     *
     * @return the second hero object
     */
    public Hero getHero2() {
        return hero2;
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
        //gameState = GameState.Defined;
        hero.setHitPoints(0);
        determineGameState();
    }

    /**
     * Checks the match state and both players states and processes the turn.
     */
    public void startTurn() {
        log.info("New Turn! " + player1.getUsername() + " " + player2.getUsername());
        determineGameState();
        
        hero1.pullCards();
        hero2.pullCards();
        //send all card ids
        player1.getSocket().newTurn(hero1.getInHand().stream().map((i)->i.getId()).toArray(Integer[]::new));
        player2.getSocket().newTurn(hero2.getInHand().stream().map((i)->i.getId()).toArray(Integer[]::new));
        
    }
}
