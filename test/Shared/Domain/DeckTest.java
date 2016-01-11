/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared.Domain;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
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
public class DeckTest {
    HeroCard card = new HeroCard(10,"Flamestrike","c://desktop/card/flamestrike","Overpowered as can be",1,2,3,4,5);
    Deck deck = new Deck(1,"Testdeck");
    List<Card> testcards = new ArrayList<>();
    
    public DeckTest() {
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
     * Test of addCard method, of class Deck.
     */
    @Test
    public void testAddCard() {
        System.out.println("addCard");
        deck.addCard(card);
    }

    /**
     * Test of removeCard method, of class Deck.
     */
    @Test
    public void testRemoveCard() {
        System.out.println("removeCard");
        deck.removeCard(card);
    }

    /**
     * Test of setName method, of class Deck.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        String name = "Testname";
        deck.setName(name);
    }

    /**
     * Test of getId method, of class Deck.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        int expResult = 1;
        int result = deck.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getName method, of class Deck.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        String expResult = "Testdeck";
        String result = deck.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCards method, of class Deck.
     */
    @Test
    public void testGetCards() {
        System.out.println("getCards");
        deck.addCard(card);
        ArrayList<Card> result = deck.getCards();
        assertEquals(deck.getCards().get(0).getId(), result.get(0).getId());
    }
    
}
