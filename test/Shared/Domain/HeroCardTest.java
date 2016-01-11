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
public class HeroCardTest {
    HeroCard heroCardInstance  = new HeroCard(1,"testname","test","test",10,10,10,10,10);
    public HeroCardTest() {
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
     * Test of getPhysicalDamage method, of class HeroCard.
     */
    @Test
    public void testGetPhysicalDamage() {
        System.out.println("getPhysicalDamage");
        int expResult = 10;
        int result = heroCardInstance.getPhysicalDamage();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMagicalDamage method, of class HeroCard.
     */
    @Test
    public void testGetMagicalDamage() {
        System.out.println("getMagicalDamage");
        int expResult = 10;
        int result = heroCardInstance.getMagicalDamage();
        assertEquals(expResult, result);

    }

    /**
     * Test of getPhysicalBlock method, of class HeroCard.
     */
    @Test
    public void testGetPhysicalBlock() {
        System.out.println("getPhysicalBlock");
        int expResult = 10;
        int result = heroCardInstance.getPhysicalBlock();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMagicalBlock method, of class HeroCard.
     */
    @Test
    public void testGetMagicalBlock() {
        System.out.println("getMagicalBlock");
        int expResult = 10;
        int result = heroCardInstance.getMagicalBlock();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHealValue method, of class HeroCard.
     */
    @Test
    public void testGetHealValue() {
        System.out.println("getHealValue");
        int expResult = 10;
        int result = heroCardInstance.getHealValue();
        assertEquals(expResult, result);
    }
    
}
