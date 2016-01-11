/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Domain;

import Shared.Domain.Deck;
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

     Deck deck = new Deck();
     Match match = new Match("1kdj2e98da","Token2");
     PlayerShared player = new PlayerShared(1, null, 45, 1, 34, 12, 40);
     Hero heroInstance = new Hero(match,player,deck);
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
     * Test of concede method, of class Match.
     */
    @Test
    public void testConcede() {
        System.out.println("concede");
        match.concede(heroInstance);
    }

    /**
     * Test of setITarget method, of class Match.
     */
    @Test
    public void testSetITarget() {
        System.out.println("setITarget");
        int source = 1;
        int target = 2;
        match.setITarget(source, target);
    }
    
}
