/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared.Interfaces;

import Shared.Domain.*;
import java.rmi.Remote;
import java.util.List;
import Server.Domain.Deck;
import Server.Domain.Player;

/**
 *Interface that shows how to load and save everything in the main menu 
 * @author Matthijs
 */
public interface IMainScreenProvider extends Remote {
    /**
     * Method to start or join a match
     * @param token Your token to inform the server which user is connected
     * @return Match token to know what match you are connected to
     */
    public String getNewMatch(String token);
    
    /**
     * Method to get all cards from the database
     * @return A list of all the cards in the database
     */
    public List<Card> getCards();
    
    /**
     * Method to request a specific deck
     * @param token Your token to know that the decks you get are yours
     * @return All your decks
     */
    public List<Deck> getDeck(String token);
    
    /**
     * Method to return all icons that are available to you 
     * @param token Your token to know that the icons that are returned are available to you
     * @return All your icons
     */
    public List<Icon> getIcons(String token);

    /**
     * Method to save a icon as your in-game icon
     * @param token Your token so the server knows who is changing an icon
     * @param iconID The ID of the selected icon so the server knows what icon is selected 
     * @return true if successful
     * false if something went wrong
     */
    public boolean setIcons(String token, int iconID);

    /**
     * Save a deck on your profile so you can use it later
     * @param token Token to let the server know who is changing a icon
     * @param cards The list of cards that are in the deck (Must be 30 cards)
     * @param name The name of the deck
     * @return true if successful
     * false if something went wrong
     */
    public boolean addDeck(String token, List<Card> cards, String name);

    /**
     * Remove a deck so that is will not show anymore
     * @param token Token to let the server know who is removing a deck
     * @param name Remove a deck by entering the name of that deck 
     * @return  true if successful
     * false if something went wrong
     */
    public boolean removeDeck(String token, String name);

    /**
     * Get a player by their token
     * @param token Token that represents a player
     * @return a player if a player is active with that token.
     * Else return null.
     */
    public Player getPlayer(String token);

}
