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
public class CardTest {
     MinionCard minionCard = new MinionCard(5, "Flamestrike", "c://documents/cards/flamestrike", "Kills the entire board", 50, 50, 50); 
    public CardTest() {
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
     * Test of getId method, of class Card.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        int expResult = 5;
        int result = minionCard.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getName method, of class Card.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        String expResult = "Flamestrike";
        String result = minionCard.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFilename method, of class Card.
     */
    @Test
    public void testGetFilename() {
        System.out.println("getFilename");
        String expResult = "c://documents/cards/flamestrike";
        String result = minionCard.getFilename();
        assertEquals(expResult, result);

    }

    /**
     * Test of getDescription method, of class Card.
     */
    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
        String expResult = "Kills the entire board";
        String result = minionCard.getDescription();
        assertEquals(expResult, result);
    }

    public class CardImpl extends Card {

        public CardImpl() {
            super(0, "", "", "");
        }
    }
    
}
