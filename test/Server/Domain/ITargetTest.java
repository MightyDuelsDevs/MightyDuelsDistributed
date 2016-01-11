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
public class ITargetTest {
    
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
        int hitPoints = 0;
        ITarget instance = new ITargetImpl();
        instance.setHitPoints(hitPoints);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHitPoints method, of class ITarget.
     */
    @Test
    public void testGetHitPoints() {
        System.out.println("getHitPoints");
        ITarget instance = new ITargetImpl();
        int expResult = 0;
        int result = instance.getHitPoints();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class ITargetImpl implements ITarget {

        public void setHitPoints(int hitPoints) {
        }

        public int getHitPoints() {
            return 0;
        }
    }
    
}
