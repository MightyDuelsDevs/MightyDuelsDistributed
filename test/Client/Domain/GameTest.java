/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Domain;

import Client.Controller.StageController;
import Shared.Domain.Card;
import Shared.Domain.Deck;
import Shared.Domain.Icon;
import Shared.Domain.PlayerShared;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sun.security.provider.certpath.BuildStep;

/**
 *
 * @author Loek
 */
public class GameTest {
    List<Icon> expResulticonlist = new ArrayList<Icon>();
    Game instance = new Game();
    private List<Card> cards;
    public GameTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
           
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        Icon first = new Icon(3,3,"testfirst");
        Icon second = new Icon(2,3,"testsecond");
        Icon third = new Icon(1,3,"testthird");
        Icon fourth = new Icon(4,3,"testfourth");        
        expResulticonlist.add(third);
        expResulticonlist.add(first);
        expResulticonlist.add(second);
        expResulticonlist.add(fourth);   
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getPlayer method, of class Game.
     */
    @Test
    public void testGetPlayer() {
        System.out.println("getPlayer");
        PlayerShared expResult = new PlayerShared(5,"loek",5,6,7,8,9);
        PlayerShared result = instance.getPlayer();
        assertEquals(null, result);
    }
    @Test
    public void testGame() {
        instance.getCards();
    }
    /**
     * Test of getIcon method, of class Game.
     */
    @Test
    public void testGetIcon() {
        System.out.println("getIcon");
        List<Icon> result = instance.getIcon();
        assertEquals(null, result);
    }

    /**
     * Test of getInstance method, of class Game.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        Game result = Game.getInstance();
        assertEquals(instance.getClass(), result.getClass());
    }

    /**
     * Test of startMatch method, of class Game.
     */
    @Test
    public void testStartMatch() {
        System.out.println("startMatch");
        byte[] expResult = null;
        byte[] result = instance.startMatch();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of loginPlayer method, of class Game.
     */
    @Test
    public void testLoginPlayer() {
        System.out.println("loginPlayer");
        String Displayname = "JANSSEN";
        String Password = "qqq";
        PlayerShared result = instance.loginPlayer(Displayname, Password);
        assertEquals(null, result);
    }

    /**
     * Test of signUpPlayer method, of class Game.
     */
    @Test
    public void testSignUpPlayer() {
        System.out.println("signUpPlayer");
        String email = "";
        String displayname = "";
        String password = "";
        String passcheck = "";
        int expResult = 0;
        int result = instance.signUpPlayer(email, displayname, password, passcheck);
        assertEquals(expResult, result);
    }

    /**
     * Test of getNewMatch method, of class Game.
     */
    @Test
    public void testGetNewMatch() {
        System.out.println("getNewMatch");
        String token = "";
        byte[] expResult = null;
        byte[] result = instance.getNewMatch(token);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getCards method, of class Game.
     */
    @Test
    public void testGetCards() {
        System.out.println("getCards");
        List<Card> expResult = null;
        List<Card> result = instance.getCards();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDecks method, of class Game.
     */
    @Test
    public void testGetDecks() {
        System.out.println("getDecks");
        String token = "";
        List<Deck> expResult = null;
        List<Deck> result = instance.getDecks(token);
        assertEquals(expResult, result.getClass());
    }

    /**
     * Test of getDeck method, of class Game.
     */
    @Test
    public void testGetDeck() {
        System.out.println("getDeck");
        String token = "";
        Deck expResult = null;
        Deck result = instance.getDeck(token);
        assertEquals(expResult, result);
    }

    /**
     * Test of getIcons method, of class Game.
     */
    @Test
    public void testGetIcons() {
        System.out.println("getIcons");
        String token = "";
        List<Icon> expResult = null;
        List<Icon> result = instance.getIcons(token);
        assertEquals(expResult, result);
    }

    /**
     * Test of setIcon method, of class Game.
     */
    @Test
    public void testSetIcon() {
        System.out.println("setIcon");
        String token = "";
        int iconID = 0;
        boolean expResult = false;
        boolean result = instance.setIcon(token, iconID);
        assertEquals(expResult, result);
    }

    /**
     * Test of addDeck method, of class Game.
     */
    @Test
    public void testAddDeck() {
        System.out.println("addDeck");
        String token = "";
        String name = "";
        boolean expResult = false;
        boolean result = instance.addDeck(token, name);
        assertEquals(expResult, result);
    }

    /**
     * Test of removeDeck method, of class Game.
     */
    @Test
    public void testRemoveDeck() {
        System.out.println("removeDeck");
        String token = "";
        int id = 0;
        boolean expResult = false;
        boolean result = instance.removeDeck(token, id);
        assertEquals(expResult, result);
    }

    /**
     * Test of setSelectedDeck method, of class Game.
     */
    @Test
    public void testSetSelectedDeck() {
        System.out.println("setSelectedDeck");
        String token = "";
        int deckId = 0;
        boolean expResult = false;
        boolean result = instance.setSelectedDeck(token, deckId);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPlayer method, of class Game.
     */
    @Test
    public void testGetPlayer_String() {
        System.out.println("getPlayer");
        String token = "";
        PlayerShared expResult = null;
        PlayerShared result = instance.getPlayer(token);
        assertEquals(expResult, result);
    }

    /**
     * Test of getToken method, of class Game.
     */
    @Test
    public void testGetToken() {
        System.out.println("getToken");
        String expResult = instance.getToken();
        String result = instance.getToken();
        assertEquals(expResult, result);
    }
    
}
