/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.RMI;

import Server.Controller.CardDeckController;
import Server.Controller.PlayerIconController;
import Shared.Domain.Deck;
import Server.Domain.Player;
import Server.SocketManagerServer.SocketManager;
import Shared.Domain.Card;
import Shared.Domain.Icon;
import Shared.Domain.PlayerShared;
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

    private Registry providerRegistry = null;
    private static final int portNumber = 422;
    private static final String bindingName = "mainScreenProvider";
    private final LoginProvider loginProvider;

    public MainScreenProvider(LoginProvider loginProvider) throws RemoteException {
        this.loginProvider = loginProvider;
        providerRegistry = LocateRegistry.createRegistry(portNumber);
        //TODO
        providerRegistry.rebind(bindingName, this);
    }

    @Override
    public byte[] getNewMatch(String token) {
        PlayerShared player = loginProvider.getPlayerFromToken(token);
        byte[] connToken =  PlayerIconController.hashByteGenerator(token + System.currentTimeMillis());
        SocketManager.getInstance().addPlayer(connToken, (Player)player);
        return connToken;
    }

    @Override
    public List<Card> getCards() {
        return CardDeckController.getAllCards();
    }

    @Override
    public Deck getDeck(String token) {
        PlayerShared player = loginProvider.getPlayerFromToken(token);
        return CardDeckController.getDeckFromPlayer(player.getId());
    }
    
    @Override
    public List<Deck> getDecks(String token) {
        PlayerShared player = loginProvider.getPlayerFromToken(token);
        return CardDeckController.getDecksFromPlayer(player.getId());
    }

    @Override
    public List<Icon> getIcons(String token) {
        PlayerShared player = loginProvider.getPlayerFromToken(token);
        return PlayerIconController.getIcons(player.getRating());
    }

    @Override
    public boolean setIcons(String token, int iconID) {
        PlayerShared player = loginProvider.getPlayerFromToken(token);
        return PlayerIconController.changePlayerIcon(player.getId(), iconID);
    }

    @Override
    public boolean addDeck(String token, List<Card> cards, String name) {
        PlayerShared player = loginProvider.getPlayerFromToken(token);
        return CardDeckController.addDeck(player.getId(), cards, name);
    }

    @Override
    public boolean removeDeck(String token, String name) {
        PlayerShared player = loginProvider.getPlayerFromToken(token);
        return CardDeckController.removeDeck(player.getId(), name);
    }

    @Override
    public PlayerShared getPlayer(String token) {
        return loginProvider.getPlayerFromToken(token);
    }

}
