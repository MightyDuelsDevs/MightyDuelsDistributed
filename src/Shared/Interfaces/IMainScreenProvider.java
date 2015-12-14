/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared.Interfaces;

import Shared.Domain.*;
import java.rmi.Remote;
import java.util.List;
import Shared.Domain.Deck;
import java.rmi.RemoteException;

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
    public byte[] getNewMatch(String token) throws RemoteException;
    
    /**
     * Method to get all cards from the database
     * @return A list of all the cards in the database
     */
    public List<Card> getCards() throws RemoteException;
    
    /**
     * Method to request a specific deck
     * @param token Your token to know that the decks you get are yours
     * @return returns the selected Deck of a player.
     */
    public Deck getDeck(String token) throws RemoteException;
    
     /**
     * Method to request all decks from a player.
     * @param token Your token to know that the decks you get are yours
     * @return returns the Decks of a player.
     */
    public List<Deck> getDecks(String token) throws RemoteException;
    /**
     * Method to return all icons that are available to you 
     * @param token Your token to know that the icons that are returned are available to you
     * @return All your icons
     */
    public List<Icon> getIcons(String token) throws RemoteException;

    /**
     * Method to save an icon as your in-game icon
     * @param token Your token so the server knows who is changing an icon
     * @param iconID The ID of the selected icon so the server knows what icon is selected 
     * @return true if successful
     * false if something went wrong
     */
    public boolean setIcons(String token, int iconID) throws RemoteException;

    /**
     * Save a deck on your profile so you can use it later
     * @param token Token to let the server know who is adding a deck
     * @param cards The list of cards that are in the deck (Must be 30 cards)
     * @param name The name of the deck
     * @return true if successful
     * false if something went wrong
     */
    public boolean addDeck(String token, String name) throws RemoteException;

    /**
     * Remove a deck so that is will not show anymore
     * @param token Token to let the server know who is removing a deck
     * @param name Remove a deck by entering the name of that deck 
     * @return  true if successful
     * false if something went wrong
     */
    public boolean removeDeck(String token, int id) throws RemoteException;

    /**
     * Get a player by their token
     * @param token Token that represents a player
     * @return a player if a player is active with that token.
     * Else return null.
     */
    public PlayerShared getPlayer(String token) throws RemoteException;

     /**
     * Set a selected deck
     * @param token Token that represents a player
     * @return a boolean if the selection was successful.
     */
    public  boolean setSelectedDeck(String token, int deckId) throws RemoteException;
}
