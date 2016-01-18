/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.RMI;

import Client.Controller.StageController;
import Shared.Domain.Deck;
import Shared.Domain.Card;
import Shared.Domain.Icon;
import Shared.Domain.PlayerShared;
import Shared.Interfaces.ILoginProvider;
import Shared.Interfaces.IMainScreenProvider;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martijn
 */
public class RMIClient {

    private static final String bindingNameLogin = "loginProvider";
    private static final String bindingNameMainScreen = "mainScreenProvider";

    private ILoginProvider loginProvider = null;
    private IMainScreenProvider mainScreenProvider = null;

    private Registry loginRegistry = null;
    private Registry mainScreenRegistry = null;

    //private static final String ipAdress = "127.0.0.1";
    private static String ipAdress = System.getProperty("MightyDuels.RMIServer");

    private static RMIClient instance;

    /**
     * Constructor that creates a RMIClient.
     */
    public RMIClient() {
        if(ipAdress == null){
            ipAdress = "127.0.0.1";
        }
        try {
            loginRegistry = LocateRegistry.getRegistry(ipAdress, 421);
            mainScreenRegistry = LocateRegistry.getRegistry(ipAdress, 422);
        } catch (RemoteException ex) {
            StageController.getInstance().popup("Server connection", false, "server connection failed.");
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (loginRegistry != null) {
            try {
                loginProvider = (ILoginProvider) loginRegistry.lookup("loginProvider");
            } catch (RemoteException | NotBoundException ex) {
                StageController.getInstance().popup("Server connection", false, "server connection failed.");
                Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (mainScreenRegistry != null) {
            try {
                mainScreenProvider = (IMainScreenProvider) mainScreenRegistry.lookup("mainScreenProvider");
            } catch (RemoteException | NotBoundException ex) {
                StageController.getInstance().popup("Server connection", false, "server connection failed.");
                Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Method that returns the only instance of the RMIClient.
     * If it does not exist yet it will be created.
     * @return the RMIClient.
     */
    public synchronized static RMIClient getInstance() {
        if (instance == null) {
            instance = new RMIClient();
        }

        return instance;
    }

    /**
     * Method that logs in the player via the loginProvider.
     * @param Displayname, the display name of the user trying to log in.
     * @param Password, the password the user is using to log in.
     * @return a token that represents the player.
     * @throws RemoteException
     */
        public String loginPlayer(String Displayname, String Password) throws RemoteException {
        return loginProvider.loginPlayer(Displayname, Password);
    }

    /**
     * Method that signs a player up in the database.
     * @param email, the email of the player that is trying to sign up.
     * @param displayname, the display name of the player signing up.
     * @param password, the password that the player is registering.
     * @param passcheck, a check to see if the password the user is entering is legit.
     * @return
     * @throws RemoteException
     */
    public int signUpPlayer(String email, String displayname, String password, String passcheck) throws RemoteException {
        return loginProvider.signUpPlayer(email, displayname, password, passcheck);
    }

    /**
     * Method that gets a byte array that represents an array using the player token.
     * @param token, a string that represents the player.
     * @return a byte array that represents a match.
     * @throws RemoteException
     */
        public byte[] getNewMatch(String token) throws RemoteException {
        return mainScreenProvider.getNewMatch(token);
    }

    /**
     * Method that returns th list of cards in the database.
     * @return A list of cards.
     * @throws RemoteException
     */
    public List<Card> getCards() throws RemoteException {
        return mainScreenProvider.getCards();
    }

    /**
     * Method that returns all the decks of a certain player.
     * @param token, string that represents a player.
     * @return all the decks of the player the token belongs to.
     * @throws RemoteException
     */
    public List<Deck> getDecks(String token) throws RemoteException {
        return mainScreenProvider.getDecks(token);
    }

    /**
     * Method that returns the selected deck of a player.
     * @param token, string that represents a player.
     * @return the selected deck of a player.
     * @throws RemoteException
     */
    public Deck getDeck(String token) throws RemoteException {
        return mainScreenProvider.getDeck(token);
    }

    /**
     * Method that returns all the icons that a player has unlocked.
     * @param token, string that represents a player.
     * @return A list of icons that the player has unlocked.
     * @throws RemoteException
     */
    public List<Icon> getIcons(String token) throws RemoteException {
        return mainScreenProvider.getIcons(token);
    }

    /**
     * Method that sets the icon of a certain player corresponding to the token.
     * @param token, string that represents a player.
     * @param iconID
     * @return if the selection was successful.
     * @throws RemoteException
     */
    public boolean setIcons(String token, int iconID) throws RemoteException {
        return mainScreenProvider.setIcons(token, iconID);
    }

    /**
     * Method that adds a deck to the player.
     * @param token, string that represents a player.
     * @param name, the name of the deck.
     * @return if the selection was successful.
     * @throws RemoteException
     */
    public boolean addDeck(String token, String name) throws RemoteException {
        return mainScreenProvider.addDeck(token, name);
    }

    /**
     * Method that removes a deck from the player.
     * @param token, string that represents a player.
     * @param id, the id of the deck to be deleted.
     * @return if the selection was successful.
     * @throws RemoteException
     */
    public boolean removeDeck(String token, int id) throws RemoteException {
        return mainScreenProvider.removeDeck(token, id);
    }

    /**
     * Method that selects a deck for the player in the database.
     * @param token, string that represents a player.
     * @param deckId, the ID of the chosen deck.
     * @return if the selection was successful.
     * @throws RemoteException
     */
    public boolean setSelectedDeck(String token, int deckId) throws RemoteException {
        return mainScreenProvider.setSelectedDeck(token, deckId);
    }

    /**
     * Method that returns a player from the database.
     * @param token, string that represents a player.
     * @return The constructed player from the database.
     * @throws RemoteException
     */
    public PlayerShared getPlayer(String token) throws RemoteException {
        return mainScreenProvider.getPlayer(token);
    }
    
    /**
     * Method that returns if there a boolean that represents the matches.
     * If there is more than 0 it returns true.
     * @return a boolean if there is more than zero games.
     * @throws RemoteException 
     */
    public boolean isPossibleSpectate() throws RemoteException {
        return mainScreenProvider.isPossibleSpectate();
    }
}
