/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Domain;

import Shared.Domain.Card;
import Shared.Domain.Deck;
import Shared.Domain.HeroCard;
import Shared.Domain.PlayerShared;
import java.util.ArrayList;
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
    
    PlayerShared player = new PlayerShared(1, null, 45, 1, 34, 12, 40);
    Deck deck = new Deck();
    Match match = new Match("1kdj2e98da","Token2");
    Hero heroInstance = new Hero(match,player,deck);
    HeroCard card = new HeroCard(10,"Flamestrike","c://desktop/card/flamestrike","Overpowered as can be",1,2,3,4,5);

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
     * Test of setFinished method, of class Hero.
     */
    @Test
    public void testSetFinished() {
        System.out.println("setFinished");
        boolean finished = false;
        heroInstance.setFinished(finished);
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
     * Test of setNewHand method, of class Hero.
     */
    @Test
    public void testSetNewHand() {
        System.out.println("setNewHand");
        List<Card> inHand = null;
        heroInstance.setNewHand(inHand);

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
        Hero instance = null;
        heroInstance.removeMinion(minion);

    }
    
}
