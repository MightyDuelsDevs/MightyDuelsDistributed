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
public class IconTest {
    Icon iconinstance = new Icon(1, 20,"filename");
    public IconTest() {
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
     * Test of getId method, of class Icon.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        int expResult = 1;
        int result = iconinstance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRatingLock method, of class Icon.
     */
    @Test
    public void testGetRatingLock() {
        System.out.println("getRatingLock");
        int expResult = 20;
        int result = iconinstance.getRatingLock();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFileName method, of class Icon.
     */
    @Test
    public void testGetFileName() {
        System.out.println("getFileName");
        String expResult = "filename";
        String result = iconinstance.getFileName();
        assertEquals(expResult, result);
    }
    
}
