/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.RMI;

import Shared.Domain.Deck;
import Server.Domain.Player;
import Shared.Domain.Card;
import Shared.Domain.Icon;
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
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (loginRegistry != null) {
            try {
                loginProvider = (ILoginProvider) loginRegistry.lookup("loginProvider");
            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (mainScreenRegistry != null) {
            try {
                mainScreenProvider = (IMainScreenProvider) mainScreenRegistry.lookup("mainScreenProvider");
            } catch (RemoteException | NotBoundException ex) {
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
    public String loginPlayer(String Displayname, String Password) {
        return loginProvider.loginPlayer(Displayname, Password);
    }
    
    public int signUpPlayer(String email, String displayname, String password, String passcheck) {
        return loginProvider.signUpPlayer(email, displayname, password, passcheck);
    }

    //Methods from MainScreenProvider
    public String getNewMatch(String token) {
        return mainScreenProvider.getNewMatch(token);
    }
    
    public List<Card> getCards() {
        return mainScreenProvider.getCards();
    }
    
    public Deck getDeck(String token) {
        return mainScreenProvider.getDeck(token);
    }
    
    public List<Icon> getIcons(String token) {
        return mainScreenProvider.getIcons(token);
    }
    
    public boolean setIcons(String token, int iconID) {
        return mainScreenProvider.setIcons(token, iconID);
    }
    
    public boolean addDeck(String token, List<Card> cards, String name) {
        return mainScreenProvider.addDeck(token, cards, name);
    }
    
    public boolean removeDeck(String token, String name) {
        return mainScreenProvider.removeDeck(token, name);
    }
    
    public Player getPlayer(String token) {
        return mainScreenProvider.getPlayer(token);
    }
}
