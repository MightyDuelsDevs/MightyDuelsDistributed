/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Domain;

import Shared.Domain.HeroCard;
import Shared.Domain.PlayerShared;
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
public class MatchTest {
    Player player1 = new Player(104, "JANSSEN", 45, 1, 34, 12, 40);
    Player player2 = new Player(201, "TESTDECK", 45, 1, 34, 12, 40);
    Shared.Domain.Deck deck = new Shared.Domain.Deck();
    Server.Domain.Match match = new Server.Domain.Match(player1,player2);
    Server.Domain.Hero heroInstance = new Server.Domain.Hero(match,player1,deck);
    HeroCard card = new HeroCard(10,"Flamestrike","c://desktop/card/flamestrike","Overpowered as can be",1,2,3,4,5);
    Server.Domain.Hero expResult = new Server.Domain.Hero(match,player1,deck);
    public MatchTest() {
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
     * Test of getTurns method, of class Match.
     */
    @Test
    public void testGetTurns() {
        System.out.println("getTurns");
        int expResult = 0;
        int result = match.getTurns();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHero1 method, of class Match.
     */
    @Test
    public void testGetHero1() {
        System.out.println("getHero1");
        Hero result = match.getHero1();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHero2 method, of class Match.
     */
    @Test
    public void testGetHero2() {
        System.out.println("getHero2");
        Hero result = match.getHero2();
        assertEquals(expResult, result);
    }

    /**
     * Test of getGameState method, of class Match.
     */
    @Test
    public void testGetGameState() {
        System.out.println("getGameState");
        Match instance = null;
        GameState expResult = GameState.Active;
        GameState result = instance.getGameState();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of concede method, of class Match.
     */
    @Test
    public void testConcede() {
        System.out.println("concede");
        Hero hero = null;
        Match instance = null;
        instance.concede(hero);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of startTurn method, of class Match.
     */
    @Test
    public void testStartTurn() {
        System.out.println("startTurn");
        Match instance = null;
        instance.startTurn();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
