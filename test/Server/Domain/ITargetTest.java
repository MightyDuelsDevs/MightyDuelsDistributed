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
import Server.Domain.Minion;
import Shared.Domain.MinionCard;
import static org.junit.Assert.*;

/**
 *
 * @author Loek
 */
public class ITargetTest {
         MinionCard minionCard = new MinionCard(5, "Flamestrike", "c://documents/cards/flamestrike", "Kills the entire board", 50, 50, 50); 
        Minion instance = new Minion(minionCard);

    
    public ITargetTest() {
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
     * Test of setHitPoints method, of class ITarget.
     */
    @Test
    public void testSetHitPoints() {
        System.out.println("setHitPoints");
        int hitPoints = 50;
        instance.setHitPoints(hitPoints);
        assertEquals(instance.getHitPoints(), hitPoints);
    }

    /**
     * Test of getHitPoints method, of class ITarget.
     */
    @Test
    public void testGetHitPoints() {
        System.out.println("getHitPoints");
        int expResult = 50;
        instance.setHitPoints(50);
        int result = instance.getHitPoints();
        assertEquals(expResult, result);
    }

    public class ITargetImpl implements ITarget {

        public void setHitPoints(int hitPoints) {
        }

        public int getHitPoints() {
            return 0;
        }
    }
    
}
