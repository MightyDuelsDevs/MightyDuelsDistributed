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
import java.rmi.RemoteException;
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
    
    String Displayname = "JANSSEN";
    String Password = "qqq";
    PlayerShared expResult = new PlayerShared(5,"JANSSEN", 5, 500, 20, 5, 25);
    PlayerShared result = instance.loginPlayer(Displayname, Password);    
    

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
        PlayerShared result = instance.getPlayer();
        assertEquals(expResult.getUsername(), result.getUsername());
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
        byte[] expResult = new byte[0];
        byte[] result = instance.startMatch();
        assertEquals(expResult.getClass(), result.getClass());
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
        assertEquals(expResult.getUsername(), result.getUsername());
    }

    /**
     * Test of signUpPlayer method, of class Game.
     */
    @Test
    public void testSignUpPlayer() {
        System.out.println("signUpPlayer");
        String email = "LoekDelahaye@gmail.com";
        String displayname = "Loek";
        String password = "Testpw";
        String passcheck = "Testpw";
        int expResult = 2;
        int result = instance.signUpPlayer(email, displayname, password, passcheck);
        assertEquals(expResult, result);
    }

    /**
     * Test of getNewMatch method, of class Game.
     */
    @Test
    public void testGetNewMatch() {
        System.out.println("getNewMatch");
        String token = instance.getToken();
        byte[] expResult = new byte[0];
        byte[] result = instance.getNewMatch(token);
        assertEquals(expResult.getClass(), result.getClass());
    }

    /**
     * Test of getCards method, of class Game.
     */
    @Test
    public void testGetCards() {
        System.out.println("getCards");
        List<Card> expResult = new ArrayList<Card>();
        List<Card> result = instance.getCards();
        assertEquals(expResult.getClass(), result.getClass());
    }

    /**
     * Test of getDecks method, of class Game.
     */
    @Test
    public void testGetDecks() {
        System.out.println("getDecks");
        String token = instance.getToken();
        List<Deck> expResult = new ArrayList<Deck>();
        List<Deck> result = instance.getDecks(token);
        assertEquals(expResult.getClass(), result.getClass());
    }

    /**
     * Test of getDeck method, of class Game.
     */
    @Test
    public void testGetDeck() {
        System.out.println("getDeck");
        String token = instance.getToken();
        Deck expResult = new Deck(1,"de mam");
        Deck result = instance.getDeck(token);
        assertEquals(expResult.getName(), result.getName());
    }

    /**
     * Test of getIcons method, of class Game.
     */
    @Test
    public void testGetIcons() {
        System.out.println("getIcons");
        String token = instance.getToken();
        List<Icon> expResult = new ArrayList<Icon>();
        List<Icon> result = instance.getIcons(token);
        assertEquals(expResult.getClass(), result.getClass());
    }

    /**
     * Test of setIcon method, of class Game.
     */
    @Test
    public void testSetIcon() {
        System.out.println("setIcon");
        String token = instance.getToken();
        int iconID = 1;
        boolean expResult = true;
        boolean result = instance.setIcon(token, iconID);
        assertEquals(expResult, result);
    }

    /**
     * Test of addDeck method, of class Game.
     */
    @Test
    public void testAddDeck() {
        System.out.println("addDeck");
        String token = instance.getToken();
        String name = "Testdeck";
        boolean expResult = true;
        boolean result = instance.addDeck(token, name);
        assertEquals(expResult, result);
    }

    /**
     * Test of removeDeck method, of class Game.
     */
    @Test
    public void testRemoveDeck() {
        System.out.println("removeDeck");
        String token = instance.getToken();
        int id = 1;
        boolean expResult = true;
        boolean result = instance.removeDeck(token, id);
        assertEquals(expResult, result);
    }

    /**
     * Test of setSelectedDeck method, of class Game.
     */
    @Test
    public void testSetSelectedDeck() {
        System.out.println("setSelectedDeck");
        String token = instance.getToken();
        int deckId = 1;
        boolean expResult = true;
        boolean result = instance.setSelectedDeck(token, deckId);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPlayer method, of class Game.
     */
    @Test
    public void testGetPlayer_String() {
        System.out.println("getPlayer");
        String token = instance.getToken();
        PlayerShared result = instance.getPlayer(token);
        assertEquals("JANSSEN", result.getUsername());
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
