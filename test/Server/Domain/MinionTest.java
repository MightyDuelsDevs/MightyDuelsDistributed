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
public class MinionTest {

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
     * Test of attack method, of class Minion.
     */
    @Test
    public void testAttack() {
        System.out.println("attack");
        Minion instance = null;
        instance.attack(0, 0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPhysicalDamage method, of class Minion.
     */
    @Test
    public void testGetPhysicalDamage() {
        System.out.println("getPhysicalDamage");
        Minion instance = null;
        int expResult = 0;
        int result = instance.getPhysicalDamage();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMagicalDamage method, of class Minion.
     */
    @Test
    public void testGetMagicalDamage() {
        System.out.println("getMagicalDamage");
        Minion instance = null;
        int expResult = 0;
        int result = instance.getMagicalDamage();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMaxHitPoints method, of class Minion.
     */
    @Test
    public void testGetMaxHitPoints() {
        System.out.println("getMaxHitPoints");
        Minion instance = null;
        int expResult = 0;
        int result = instance.getMaxHitPoints();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getITarget method, of class Minion.
     */
    @Test
    public void testGetITarget() {
        System.out.println("getITarget");
        Minion instance = null;
        ITarget expResult = null;
        ITarget result = instance.getITarget();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setITarget method, of class Minion.
     */
    @Test
    public void testSetITarget() {
        System.out.println("setITarget");
        ITarget iTarget = null;
        Minion instance = null;
        instance.setITarget(iTarget);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHitPoints method, of class Minion.
     */
    @Test
    public void testGetHitPoints() {
        System.out.println("getHitPoints");
        Minion instance = null;
        int expResult = 0;
        int result = instance.getHitPoints();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setHitPoints method, of class Minion.
     */
    @Test
    public void testSetHitPoints() {
        System.out.println("setHitPoints");
        int hitPoints = 0;
        Minion instance = null;
        instance.setHitPoints(hitPoints);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
