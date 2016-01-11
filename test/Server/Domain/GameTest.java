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
public class GameTest {
    Game instance = new Game();
    Player player = new Player(1, "Testname", 45, 1, 34, 12, 40);
    public GameTest() {
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
     * Test of getInstance method, of class Game.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        Game expResult = Game.getInstance();
        Game result = Game.getInstance();
        assertTrue(expResult.equals(result));
    }

    /**
     * Test of findMatch method, of class Game.
     */
    @Test
    public void testFindMatch() {
        System.out.println("findMatch");
        boolean expResult = false;
        boolean result = instance.findMatch(player);
        assertEquals(expResult, result);
    }

    /**
     * Test of addWaitingPlayer method, of class Game.
     */
    @Test
    public void testAddWaitingPlayer() {
        System.out.println("addWaitingPlayer");
        instance.addWaitingPlayer(player);
    }
    
}
