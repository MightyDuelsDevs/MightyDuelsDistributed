/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Controller;

import Server.Database.Database;
import Server.Domain.Player;
import Shared.Domain.Icon;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class PlayerIconControllerTest {

    public PlayerIconControllerTest() {
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
     * Test of playerIconControllerInit method, of class PlayerIconController.
     */
    @Test
    public void testPlayerIconControllerInit() {
        System.out.println("playerIconControllerInit");
        PlayerIconController.playerIconControllerInit();
        int rating = 1200;
        assertNotNull(PlayerIconController.getIcons(rating));
    }

    /**
     * Test of changePlayerIcon method, of class PlayerIconController.
     */
    @Test
    public void testChangePlayerIcon() {
        System.out.println("changePlayerIcon");
        int playerID = 101;
        int iconID = 1;
        boolean expResult = true;
        boolean result = PlayerIconController.changePlayerIcon(playerID, iconID);
        assertEquals(expResult, result);
    }

    /**
     * Test of signUpPlayer method, of class PlayerIconController.
     */
    @Test
    public void testSignUpPlayer() {
        System.out.println("signUpPlayer");
        String email = "UnitTest@test.nl";
        String displayname = "UnitTest";
        String password = "UnitTest";
        String passcheck = "UnitTest";
        String passcheckfail = "tseTtinU";
        int expResult = 1;
        int result = PlayerIconController.signUpPlayer(email, "iets", password, passcheckfail);
        assertEquals(expResult, result);

        expResult = 3;
        result = PlayerIconController.signUpPlayer(email, displayname, password, passcheck);
        assertEquals(expResult, result);
        expResult = 2;
        result = PlayerIconController.signUpPlayer(email, displayname, password, passcheck);
        assertEquals(expResult, result);

        String statement = "DELETE FROM PLAYER WHERE DISPLAYNAME = 'DEMAM'";
        try {
            Database.openConnection();
            if (Database.checkConnection()) {
                Database.DMLRecordIntoTable(statement);
            } else {
                System.out.println("Database connection is lost.");
            }
        } catch (SQLException ex) {
            System.out.println("error");
        }
    }

    /**
     * Test of logInPlayer method, of class PlayerIconController.
     */
    @Test
    public void testLogInPlayer() {
        System.out.println("logInPlayer");
        String displayname = "janssen";
        String password = "qqq";
        Player expResult = new Player(102, "JANSSEN", 1, 1200, 0, 0, 0);
        Player result = PlayerIconController.logInPlayer(displayname, password);
        assertEquals(expResult.getUsername(), result.getUsername());
    }

    /**
     * Test of createPlayer method, of class PlayerIconController.
     */
    @Test
    public void testCreatePlayer() {
        System.out.println("createPlayer");
        String displayname = "JANSSEN";
        Player expResult = new Player(102, "JANSSEN", 1, 1200, 0, 0, 0);
        Player result = PlayerIconController.createPlayer(displayname);
        assertEquals(expResult.getUsername(), result.getUsername());
    }

    /**
     * Test of createPlayerWithId method, of class PlayerIconController.
     */
    @Test
    public void testCreatePlayerWithId() {
        System.out.println("createPlayerWithId");
        int playerId = 102;
        Player expResult = new Player(102, "JANSSEN", 1, 1200, 0, 0, 0);
        Player result = PlayerIconController.createPlayerWithId(playerId);
        assertEquals(expResult.getUsername(), result.getUsername());
    }

    /**
     * Test of getIcons method, of class PlayerIconController.
     */
    @Test
    public void testGetIcons() {
        System.out.println("getIcons");
        int rating = 1200;
        List<Icon> expResult = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            expResult.add(new Icon(10, 1200, "test"));
        }
        List<Icon> result = PlayerIconController.getIcons(rating);
        assertEquals(expResult.size(), result.size());
    }

    /**
     * Test of updateRating method, of class PlayerIconController.
     */
    @Test
    public void testUpdateRating() {
        System.out.println("updateRating");
        int playerOneId = 101;
        int playerTwoId = 101;
        boolean playerOneWon = true;
        boolean expResult = true;
        boolean result = PlayerIconController.updateRating(playerOneId, playerTwoId, playerOneWon);
        assertEquals(expResult, result);
    }
}
