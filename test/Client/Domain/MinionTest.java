/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Domain;

import Shared.Domain.Deck;
import Shared.Domain.MinionCard;
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
public class MinionTest {
     MinionCard minionCard = new MinionCard(5, "Flamestrike", "c://documents/cards/flamestrike", "Kills the entire board", 50, 50, 50); 
     Minion minionInstance = new Minion(minionCard);
     PlayerShared player = new PlayerShared(1, null, 45, 1, 34, 12, 40);

    public MinionTest() {
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
     * Test of getPhysicalDamage method, of class Minion.
     */
    @Test
    public void testGetPhysicalDamage() {
        System.out.println("getPhysicalDamage");
        int expResult = 50;
        int result = minionInstance.getPhysicalDamage();
        assertEquals(expResult, result);

    }

    /**
     * Test of getMagicalDamage method, of class Minion.
     */
    @Test
    public void testGetMagicalDamage() {
        System.out.println("getMagicalDamage");
        int expResult = 50;
        int result = minionInstance.getMagicalDamage();
        assertEquals(expResult, result);

    }

    /**
     * Test of getMaxHitPoints method, of class Minion.
     */
    @Test
    public void testGetMaxHitPoints() {
        System.out.println("getMaxHitPoints");
        int expResult = 50;
        int result = minionInstance.getMaxHitPoints();
        assertEquals(expResult, result);
    }
    
}
