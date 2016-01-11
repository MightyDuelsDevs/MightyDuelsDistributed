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
public class MinionCardTest {
    MinionCard minionCard = new MinionCard(5, "Flamestrike", "c://documents/cards/flamestrike", "Kills the entire board", 50, 50, 50); 
    public MinionCardTest() {
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
     * Test of getPhysicalDamage method, of class MinionCard.
     */
    @Test
    public void testGetPhysicalDamage() {
        System.out.println("getPhysicalDamage");
        int expResult = 50;
        int result = minionCard.getPhysicalDamage();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMagicalDamage method, of class MinionCard.
     */
    @Test
    public void testGetMagicalDamage() {
        System.out.println("getMagicalDamage");
        int expResult = 50;
        int result = minionCard.getMagicalDamage();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHitPoints method, of class MinionCard.
     */
    @Test
    public void testGetHitPoints() {
        System.out.println("getHitPoints");
        int expResult = 50;
        int result = minionCard.getHitPoints();
        assertEquals(expResult, result);
    }
    
}
