/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared.Domain;

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
public class PlayerSharedTest {
    PlayerShared player = new PlayerShared(1, "Testname", 45, 1, 34, 12, 40);
    public PlayerSharedTest() {
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
     * Test of getId method, of class PlayerShared.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        int expResult = 1;
        int result = player.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getUsername method, of class PlayerShared.
     */
    @Test
    public void testGetUsername() {
        System.out.println("getUsername");
        String expResult = "Testname";
        String result = player.getUsername();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIconId method, of class PlayerShared.
     */
    @Test
    public void testGetIconId() {
        System.out.println("getIconId");
        int expResult = 45;
        int result = player.getIconId();
        assertEquals(expResult, result);        
    }

    /**
     * Test of getRating method, of class PlayerShared.
     */
    @Test
    public void testGetRating() {
        System.out.println("getRating");
        int expResult = 1;
        int result = player.getRating();
        assertEquals(expResult, result);

    }

    /**
     * Test of getMatches method, of class PlayerShared.
     */
    @Test
    public void testGetMatches() {
        System.out.println("getMatches");
        int expResult = 40;
        int result = player.getMatches();
        assertEquals(expResult, result);
    }

    /**
     * Test of getWins method, of class PlayerShared.
     */
    @Test
    public void testGetWins() {
        System.out.println("getWins");
        int expResult = 34;
        int result = player.getWins();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLosses method, of class PlayerShared.
     */
    @Test
    public void testGetLosses() {
        System.out.println("getLosses");
        int expResult = 12;
        int result = player.getLosses();
        assertEquals(expResult, result);
    }
    
}
