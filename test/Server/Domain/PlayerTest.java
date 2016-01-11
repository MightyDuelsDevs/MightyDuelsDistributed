/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Domain;

import Server.SocketManagerServer.SocketClient;
import Shared.Domain.Icon;
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
public class PlayerTest {
    
    public PlayerTest() {
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
     * Test of setSocket method, of class Player.
     */
    @Test
    public void testSetSocket() {
        System.out.println("setSocket");
        SocketClient socket = null;
        Player instance = null;
        instance.setSocket(socket);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSocket method, of class Player.
     */
    @Test
    public void testGetSocket() {
        System.out.println("getSocket");
        Player instance = null;
        SocketClient expResult = null;
        SocketClient result = instance.getSocket();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setIconId method, of class Player.
     */
    @Test
    public void testSetIconId() {
        System.out.println("setIconId");
        int iconId = 0;
        Player instance = null;
        instance.setIconId(iconId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setRating method, of class Player.
     */
    @Test
    public void testSetRating() {
        System.out.println("setRating");
        int rating = 0;
        Player instance = null;
        instance.setRating(rating);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMatches method, of class Player.
     */
    @Test
    public void testSetMatches() {
        System.out.println("setMatches");
        Player instance = null;
        instance.setMatches();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setWins method, of class Player.
     */
    @Test
    public void testSetWins() {
        System.out.println("setWins");
        Player instance = null;
        instance.setWins();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLoses method, of class Player.
     */
    @Test
    public void testSetLoses() {
        System.out.println("setLoses");
        Player instance = null;
        instance.setLoses();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of changeIcon method, of class Player.
     */
    @Test
    public void testChangeIcon() {
        System.out.println("changeIcon");
        Icon icon = null;
        Player instance = null;
        instance.changeIcon(icon);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setId method, of class Player.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        int id = 0;
        Player instance = null;
        instance.setId(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
