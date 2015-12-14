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

    private static final String ipAdress = "127.0.0.1";

    private static RMIClient instance;

    public RMIClient() {
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

    public synchronized static RMIClient getInstance() {
        if (instance == null) {
            instance = new RMIClient();
        }

        return instance;
    }

    //Methods from LoginProvider
    public String loginPlayer(String Displayname, String Password) throws RemoteException {
        return loginProvider.loginPlayer(Displayname, Password);
    }

    public int signUpPlayer(String email, String displayname, String password, String passcheck) throws RemoteException {
        return loginProvider.signUpPlayer(email, displayname, password, passcheck);
    }

    //Methods from MainScreenProvider
    public byte[] getNewMatch(String token) throws RemoteException {
        return mainScreenProvider.getNewMatch(token);
    }

    public List<Card> getCards() throws RemoteException {
        return mainScreenProvider.getCards();
    }

    public List<Deck> getDecks(String token) throws RemoteException {
        return mainScreenProvider.getDecks(token);
    }

    public Deck getDeck(String token) throws RemoteException {
        return mainScreenProvider.getDeck(token);
    }

    public List<Icon> getIcons(String token) throws RemoteException {
        return mainScreenProvider.getIcons(token);
    }

    public boolean setIcons(String token, int iconID) throws RemoteException {
        return mainScreenProvider.setIcons(token, iconID);
    }

    public boolean addDeck(String token, String name) throws RemoteException {
        return mainScreenProvider.addDeck(token, name);
    }

    public boolean removeDeck(String token, int id) throws RemoteException {
        return mainScreenProvider.removeDeck(token, id);
    }

    public boolean setSelectedDeck(String token, int deckId) throws RemoteException {
        return mainScreenProvider.setSelectedDeck(token, deckId);
    }

    public PlayerShared getPlayer(String token) throws RemoteException {
        return mainScreenProvider.getPlayer(token);
    }
}
