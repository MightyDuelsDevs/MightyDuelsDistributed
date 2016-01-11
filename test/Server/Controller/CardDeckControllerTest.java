/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Controller;

import Shared.Domain.Card;
import Shared.Domain.Deck;
import Shared.Domain.HeroCard;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ramòn Janssen
 */
public class CardDeckControllerTest {

    public CardDeckControllerTest() {
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
     * Test of cardDeckControllerInit method, of class CardDeckController.
     */
    @Test
    public void testCardDeckControllerInit() {
        System.out.println("cardDeckControllerInit");
        CardDeckController.cardDeckControllerInit();
        List<Card> allCards = CardDeckController.getAllCards();
        int expResult = 1;
        int result = allCards.get(0).getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCard method, of class CardDeckController.
     */
    @Test
    public void testGetCard() {
        System.out.println("getCard");
        int cardID = 1;
        int expResult = 1;
        int result = CardDeckController.getCard(cardID).getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllCards method, of class CardDeckController.
     */
    @Test
    public void testGetAllCards() {
        System.out.println("getAllCards");
        List<Card> allCards = CardDeckController.getAllCards();
        int index = 0;
        int expResult = 1;
        int result = allCards.get(index).getId();
        assertEquals(expResult, result);
        // Second check
        index = 1;
        expResult = 2;
        result = allCards.get(index).getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDeck method, of class CardDeckController.
     */
    @Test
    public void testGetDeck() {
        System.out.println("getDeck");
        int deckID = 101;
        int expResult = 101;
        int result = CardDeckController.getDeck(deckID).getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDeckFromPlayer method, of class CardDeckController.
     */
    @Test
    public void testGetDeckFromPlayer() {
        System.out.println("getDeckFromPlayer");
        int playerID = 101;
        int expResult = 101;
        int result = CardDeckController.getDeckFromPlayer(playerID).getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDecksFromPlayer method, of class CardDeckController.
     */
    @Test
    public void testGetDecksFromPlayer() {
        System.out.println("getDecksFromPlayer");
        int playerID = 101;
        int expResult = 101;
        List<Deck> decks = CardDeckController.getDecksFromPlayer(playerID);
        int result = decks.get(0).getId();
        assertEquals(expResult, result);
        // Second check
        result = decks.get(1).getId();
        expResult = 102;
        assertEquals(expResult, result);
    }

    /**
     * Test of addDeck method, of class CardDeckController.
     */
    @Test
    public void testAddDeck() {
        System.out.println("addDeck");
        int playerID = 101;
        String deckName = "addedTestDeck";
        boolean expResult = true;
        boolean result = CardDeckController.addDeck(playerID, deckName);
        assertEquals(expResult, result);
    }

    /**
     * Test of removeDeck method, of class CardDeckController.
     */
    @Test
    public void testRemoveDeck() {
        System.out.println("removeDeck");
        int playerId = 101;
        int deckId = CardDeckController.getDecksFromPlayer(playerId).get(2).getId();
        boolean expResult = true;
        boolean result = CardDeckController.removeDeck(playerId, deckId);
        assertEquals(expResult, result);
    }

    /**
     * Test of setSelectedDeck method, of class CardDeckController.
     */
    @Test
    public void testSetSelectedDeck() {
        System.out.println("setSelectedDeck");
        int playerId = 101;
        int deckId = 101;
        boolean expResult = true;
        boolean result = CardDeckController.setSelectedDeck(playerId, deckId);
        assertEquals(expResult, result);
    }

}
