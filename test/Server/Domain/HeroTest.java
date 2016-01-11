/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Domain;

import Shared.Domain.Card;
import Shared.Domain.Deck;
import Server.Domain.Match;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Loek
 */
public class HeroTest {
    Deck deck = new Deck();
    Player player1 = new Player(102, "janssen", 45, 1, 34, 12, 40);
    Player player2 = new Player(202, "testdeck", 45, 1, 34, 12, 40);
    Match match = new Match(player1,player2);
    Hero heroInstance = new Hero(match, player1, deck);
    
    public HeroTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getDeck method, of class Hero.
     */
    @Test
    public void testGetDeck() {
        System.out.println("getDeck");
        Deck result = heroInstance.getDeck();
        assertEquals(deck, result);
    }

    /**
     * Test of setDeck method, of class Hero.
     */
    @Test
    public void testSetDeck() {
        System.out.println("setDeck");    
        heroInstance.setDeck(deck);
    }

    /**
     * Test of getFinished method, of class Hero.
     */
    @Test
    public void testGetFinished() {
        System.out.println("getFinished");
        boolean expResult = false;
        boolean result = heroInstance.getFinished();
        assertEquals(expResult, result);
    }

    /**
     * Test of setFinished method, of class Hero.
     */
    @Test
    public void testSetFinished() {
        System.out.println("setFinished");
        boolean finished = false;        
        heroInstance.setFinished(finished);        
    }

    /**
     * Test of getMinions method, of class Hero.
     */
    @Test
    public void testGetMinions() {
        System.out.println("getMinions");        
        List<Minion> expResult = null;
        List<Minion> result = heroInstance.getMinions();
        assertEquals(expResult, result);
    }

    /**
     * Test of setMinions method, of class Hero.
     */
    @Test
    public void testSetMinions() {
        System.out.println("setMinions");
        List<Minion> minions = null;
        heroInstance.setMinions(minions);        
    }

    /**
     * Test of getInHand method, of class Hero.
     */
    @Test
    public void testGetInHand() {
        System.out.println("getInHand");
        List<Card> expResult = null;
        List<Card> result = heroInstance.getInHand();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNewHand method, of class Hero.
     */
    @Test
    public void testSetNewHand() {
        System.out.println("setNewHand");
        List<Card> inHand = null;
        heroInstance.setNewHand(inHand);
    }

    /**
     * Test of getCardPlayed method, of class Hero.
     */
    @Test
    public void testGetCardPlayed() {
        System.out.println("getCardPlayed");
        Card expResult = null;
        Card result = heroInstance.getCardPlayed();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCardPlayed method, of class Hero.
     */
    @Test
    public void testSetCardPlayed() {
        System.out.println("setCardPlayed");
        Card cardPlayed = null;
        heroInstance.setCardPlayed(cardPlayed);
    }

    /**
     * Test of pullCards method, of class Hero.
     */
    @Test
    public void testPullCards() {
        System.out.println("pullCards");
        heroInstance.pullCards();
    }

    /**
     * Test of playCard method, of class Hero.
     */
    @Test
    public void testPlayCard() {
        System.out.println("playCard");
        Card card = null;
        boolean expResult = false;
        boolean result = heroInstance.playCard(card);
        assertEquals(expResult, result);
    }

    /**
     * Test of removeMinion method, of class Hero.
     */
    @Test
    public void testRemoveMinion() {
        System.out.println("removeMinion");
        Minion minion = null;
        heroInstance.removeMinion(minion);
    }

    /**
     * Test of getHitPoints method, of class Hero.
     */
    @Test
    public void testGetHitPoints() {
        System.out.println("getHitPoints");
        int expResult = 0;
        int result = heroInstance.getHitPoints();
        assertEquals(expResult, result);
    }

    /**
     * Test of setHitPoints method, of class Hero.
     */
    @Test
    public void testSetHitPoints() {
        System.out.println("setHitPoints");
        int hitPoints = 10;
        heroInstance.setHitPoints(hitPoints);
    }
    
}
