/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Domain;

import Shared.Domain.Card;
import Shared.Domain.Deck;
import Shared.Domain.HeroCard;
import Shared.Domain.MinionCard;
import Shared.Domain.PlayerShared;
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
 * @author Loek
 */
public class HeroTest {

    PlayerShared player;
    Deck deck;
    Game instance;

    Match match;
    Hero heroInstance;
    HeroCard card;
    MinionCard minionCard;
    MinionCard minionCard2;
    Minion minion1;
    Minion minion2;
    List<Minion> minionlist;

    public HeroTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        deck = new Deck();
        instance = new Game();
        player = instance.loginPlayer("unittest", "qqq");
        match = new Match(instance.getToken(), instance.getToken());
        heroInstance = new Hero(match, player, deck);
        card = new HeroCard(10, "Flamestrike", "c://desktop/card/flamestrike", "Overpowered as can be", 1, 2, 3, 4, 5);
        minionCard = new MinionCard(5, "Flamestrike", "c://documents/cards/flamestrike", "Kills the entire board", 50, 50, 50);
        minionCard2 = new MinionCard(5, "Flamestrike", "c://documents/cards/flamestrike", "Kills the entire board", 50, 50, 50);
        minion1 = new Minion(minionCard);
        minion2 = new Minion(minionCard2);
        minionlist = new ArrayList<>();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setFinished method, of class Hero.
     */
    @Test
    public void testSetFinished() {
        System.out.println("setFinished");
        boolean finished = false;
        heroInstance.setFinished(finished);
    }

    /**
     * Test of setMinions method, of class Hero.
     */
    @Test
    public void testSetMinions() {
        System.out.println("setMinions");
        List<Minion> minions = null;
        heroInstance.setMinions(minions);

    }

    /**
     * Test of setNewHand method, of class Hero.
     */
    @Test
    public void testSetNewHand() {
        System.out.println("setNewHand");
        List<Card> inHand = null;
        heroInstance.setNewHand(inHand);

    }

    /**
     * Test of setCardPlayed method, of class Hero.
     */
    @Test
    public void testSetCardPlayed() {
        System.out.println("setCardPlayed");
        Card cardPlayed = null;
        heroInstance.setCardPlayed(cardPlayed);
    }

    /**
     * Test of playCard method, of class Hero.
     */
    @Test
    public void testPlayCard() {
        System.out.println("playCard");
        Card card = null;
        boolean expResult = true;
        boolean result = heroInstance.playCard(card);
        assertEquals(expResult, result);
    }

    @Test
    public void testPlayCardDifferentCard() {
        System.out.println("playCard");
        minionlist.add(minion1);
        minionlist.add(minion2);
        heroInstance.setMinions(minionlist);
        boolean expResult = true;
        boolean result = heroInstance.playCard(card);
        assertEquals(expResult, result);
    }

    @Test
    public void testPlayCardMinionCard() {
        System.out.println("playCard");
        minionlist.add(minion1);
        minionlist.add(minion2);
        heroInstance.setMinions(minionlist);
        boolean expResult = false;
        boolean result = heroInstance.playCard(minionCard);
        assertEquals(expResult, result);
    }

    /**
     * Test of removeMinion method, of class Hero.
     */
    @Test
    public void testRemoveMinion() {
        System.out.println("removeMinion");
        Minion minion = null;
        Hero instance = null;
        heroInstance.removeMinion(minion);

    }

}
