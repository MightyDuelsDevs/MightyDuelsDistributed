/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Controller;

import Server.Database.Database;
import Shared.Domain.Card;
import Server.Domain.Deck;
import Shared.Domain.HeroCard;
import Shared.Domain.MinionCard;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martijn van Buul
 */
public class CardDeckController {

    private static List<Card> allCards;

    /**
     * Initialize the CardDeckController
     */
    public static void cardDeckControllerInit() {
        allCards = getAllCardsFromDB();
    }

    public static List<Card> getAllCards() {
        return allCards;
    }

    /**
     * Method to get all the cards from the database and save them in a local
     * variable.
     *
     * @return Returns all the cards that are used in the game.
     */
    private static List<Card> getAllCardsFromDB() {
        if (allCards != null) {
            return allCards;
        }

        String statement = "SELECT * FROM CARD";
        List<Card> cards = new ArrayList<>();

        try {
            if (Database.checkConnection()) {
                List<List> resultSet = Database.selectRecordFromTable(statement);

                for (List<String> column : resultSet) {
                    if ("HEROCARD".equals(column.get(4))) {

                        int id = Integer.parseInt(column.get(0));
                        String cardName = column.get(1);
                        String fileName = column.get(2);
                        String description = column.get(3);
                        int physicalDamage = Integer.parseInt(column.get(5));
                        int magicalDamage = Integer.parseInt(column.get(6));
                        int physicalBlock = Integer.parseInt(column.get(7));
                        int magicalBlock = Integer.parseInt(column.get(8));
                        int healValue = Integer.parseInt(column.get(9));

                        cards.add(new HeroCard(id, cardName, fileName, description, physicalDamage, magicalDamage, physicalBlock, magicalBlock, healValue) {
                        });
                    } else {

                        int id = Integer.parseInt(column.get(0));
                        String cardName = column.get(1);
                        String fileName = column.get(2);
                        String description = column.get(3);
                        int physicalDamage = Integer.parseInt(column.get(5));
                        int magicalDamage = Integer.parseInt(column.get(6));
                        int physicalBlock = Integer.parseInt(column.get(7));

                        cards.add(new MinionCard(id, cardName, fileName, description, physicalDamage, magicalDamage, physicalBlock) {
                        });
                    }
                }
            } else {
                System.out.println("Database connection is lost.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerIconController.class.getName()).log(Level.SEVERE, null, ex);
        }
        allCards = cards;
        return allCards;
    }

    /**
     * Function that returns a complete deck. It uses the local variable that
     * contains all the cards.
     *
     * @param deckID, ID of the deck in the database.
     * @return Returns the deck corresponding with the "deckID".
     */
    public static Deck getDeck(int deckID) {
        String statement = String.format("SELECT * FROM DECK WHERE ID = %1$s", deckID);
        Deck deck = new Deck();

        try {
            if (Database.checkConnection()) {
                List<List> resultSet = Database.selectRecordFromTable(statement);
                List<String> column = resultSet.get(0);

                for (int i = 3; i < column.size(); i++) {
                    deck.addCard(allCards.get(Integer.parseInt(column.get(i)) - 1));
                }

            } else {
                System.out.println("Database connection is lost.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerIconController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return deck;
    }

    /**
     * Function that returns all the decks of a single player. It uses the local
     * variable that contains all the cards.
     *
     * @param playerID, ID of the player in the database.
     * @return Returns all the decks of the player corresponding with the
     * "playerID".
     */
    public static List<Deck> getDecksFromPlayer(int playerID) {
        String statement = String.format("SELECT * FROM DECK WHERE PLAYERID = %1$s", playerID);
        List<Deck> decks = new ArrayList<>();

        try {
            if (Database.checkConnection()) {
                List<List> resultSet = Database.selectRecordFromTable(statement);

                for (List<String> column : resultSet) {
                    Deck deck = new Deck();

                    for (int i = 3; i < column.size(); i++) {
                        deck.addCard(allCards.get(Integer.parseInt(column.get(i)) - 1));
                    }

                    decks.add(deck);
                }

            } else {
                System.out.println("Database connection is lost.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerIconController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return decks;
    }

    public static boolean removeDeck(int playerID, String deckName) {
        String statement = String.format("DELETE FROM DECK WHERE PLAYER ID = %1$s AND NAME = '%2$s'", playerID, deckName);
        try {
            if (Database.checkConnection()) {
                Database.DMLRecordIntoTable(statement);
            } else {
                System.out.println("Database connection is lost.");
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerIconController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public static boolean addDeck(int playerID, List<Card> cards, String deckName){
        String cardString = "";
        
        for(Card card : cards)
        {
            cardString += card.getId()+ ", ";
        }
        
        String statement = String.format("INSERT INTO DECK VALUES(null, %1$s, %2$s %3$s)", playerID, deckName, cardString);
        try {
            if (Database.checkConnection()) {
                Database.DMLRecordIntoTable(statement);
            } else {
                System.out.println("Database connection is lost.");
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerIconController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
}
