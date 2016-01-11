/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Domain;

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
        Match instance = null;
        int expResult = 0;
        int result = instance.getTurns();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHero1 method, of class Match.
     */
    @Test
    public void testGetHero1() {
        System.out.println("getHero1");
        Match instance = null;
        Hero expResult = null;
        Hero result = instance.getHero1();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHero2 method, of class Match.
     */
    @Test
    public void testGetHero2() {
        System.out.println("getHero2");
        Match instance = null;
        Hero expResult = null;
        Hero result = instance.getHero2();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGameState method, of class Match.
     */
    @Test
    public void testGetGameState() {
        System.out.println("getGameState");
        Match instance = null;
        GameState expResult = null;
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
