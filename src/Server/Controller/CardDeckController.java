/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Controller;

import Server.Database.Database;
import Shared.Domain.Card;
import Shared.Domain.Deck;
import Shared.Domain.HeroCard;
import Shared.Domain.MinionCard;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

    /**
     * Function that returns a Card at a certain index.
     * @param index, index in the list of cards.
     * @return returns a card that corresponds with the index of the list.
     */
    public static Card getCard(int index) {
        return allCards.stream().filter((c)->c.getId()==index).findFirst().get();
    }

    /**
     * Function that returns all the cards.
     * @return A list of cards that contains every single card.
     */
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
                        int hitPoints = Integer.parseInt(column.get(9));

                        cards.add(new MinionCard(id, cardName, fileName, description, physicalDamage, magicalDamage, hitPoints) {
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
        Deck deck = null;

        try {
            if (Database.checkConnection()) {
                List<List> resultSet = Database.selectRecordFromTable(statement);
                List<String> column = resultSet.get(0);

                deck = new Deck(Integer.parseInt(column.get(0)), column.get(2));

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
     * Function that returns a complete deck. It uses the local variable that
     * contains all the cards.
     *
     * @param playerID, ID of the player in the database.
     * @return Returns the deck corresponding with the selected deck of the
     * player with the corresponding playerID.
     */
    public static Deck getDeckFromPlayer(int playerID) {
        String statement = String.format("SELECT * FROM DECK WHERE ID = (SELECT SELDECKID FROM PLAYER WHERE ID = %1$s)", playerID);
        Deck deck = null;
        
        try {
            if (Database.checkConnection()) {
                List<List> resultSet = Database.selectRecordFromTable(statement);
                List<String> column = resultSet.get(0);

                deck = new Deck(Integer.parseInt(column.get(0)), column.get(2));

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
                    Deck deck = new Deck(Integer.parseInt(column.get(0)), column.get(2));

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

    /**
     * A function that is used to remove a deck from the database.
     * @param playerId, the ID of the player of whom a deck will be deleted.
     * @param deckId, The ID of the deck that will be deleted.
     * @return a boolean if the removal was successful.
     */
    public static boolean removeDeck(int playerId, int deckId) {
        String checkDecks = String.format("SELECT COUNT(UNIQUE(ID)) FROM DECK WHERE PLAYERID = %1$s", playerId);
        try{
            if(Database.checkConnection()){
                List<List> data = Database.selectRecordFromTable(checkDecks);
                if(data.get(0).get(0).equals("1") || data.get(0).get(0).equals("0")){
                    Logger.getLogger(PlayerIconController.class.getName()).log(Level.SEVERE, "Only one deck left, can't remove");
                    return false;
                }
            } else {
                System.out.println("Database connection is lost.");
                return false;
            }
        }catch(SQLException ex){
            Logger.getLogger(PlayerIconController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        String statement = String.format("DELETE FROM DECK WHERE PLAYERID = %1$s AND ID = %2$s", playerId, deckId);
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

    /**
     * Function that adds a deck to the database.
     * This function will create a randomised deck and add it to the database.
     * @param playerID, The player that created the deck.
     * @param deckName, The name of the deck that will be created.
     * @return a boolean if the addition was successful.
     */
    public static boolean addDeck(int playerID, String deckName) {
        String cardString = "";

        List<Card> cards = new ArrayList();
        Random random = new Random();
        
        for(int i = 0; i < 30; i++){
            cards.add(allCards.get(random.nextInt(allCards.size())));
        }
        
        for (Card card : cards) {
            cardString += ", " + card.getId();
        }

        String statement = String.format("INSERT INTO DECK VALUES(null, %1$s, '%2$s' %3$s)", playerID, deckName, cardString);
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
    
    /**
     * Function that selects a deck for a player in the database.
     * @param playerId, The ID of the player that is selecting.
     * @param deckId, The ID of the deck that the player selected.
     * @return a boolean if the selection was successful.
     */
    public static boolean setSelectedDeck(int playerId, int deckId) {
        String statement = String.format("UPDATE PLAYER SET SELDECKID = %1$s WHERE ID = %2$s", deckId, playerId);
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
