/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.RMI;

import Server.Domain.Deck;
import Server.Domain.Player;
import Shared.Domain.Card;
import Shared.Domain.Icon;
import java.rmi.server.UnicastRemoteObject;
import Shared.Interfaces.IMainScreenProvider;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

/**
 *
 * @author Martijn
 */
public class MainScreenProvider extends UnicastRemoteObject implements IMainScreenProvider {

    private Registry providerRegistry;
    private static final int portNumber = 422;
    private static final String bindingName = "mainScreenProvider";
    
    public MainScreenProvider() throws RemoteException{
        providerRegistry = LocateRegistry.createRegistry(portNumber);
        //TODO
        providerRegistry.rebind(bindingName, this);
    }
    
    @Override
    public String getNewMatch(String token) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Card> getCards() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Deck> getDeck(String token) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Icon> getIcons(String token) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean setIcons(String token, int iconID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addDeck(String token, List<Card> cards, String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeDeck(String token, String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Player getPlayer(String token) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
