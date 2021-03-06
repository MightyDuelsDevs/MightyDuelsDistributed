/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.RMI;

import Server.Controller.CardDeckController;
import Server.Controller.PlayerIconController;
import java.rmi.server.UnicastRemoteObject;
import Shared.Interfaces.ILoginProvider;
import Server.Domain.Player;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import Shared.Domain.PlayerShared;
import java.util.HashMap;

/**
 *
 * @author Martijn
 */
public class LoginProvider extends UnicastRemoteObject implements ILoginProvider {
    private final Map<String, PlayerShared> mapTokenPlayer;

    private Registry providerRegistry = null;
    private static final int portNumber = 421;
    private static final String bindingName = "loginProvider";
    
    public LoginProvider() throws RemoteException{
        this.mapTokenPlayer = new HashMap<>();
        providerRegistry = LocateRegistry.createRegistry(portNumber);
        //TODO
        providerRegistry.rebind(bindingName, this);
    }
    
    @Override
    public String loginPlayer(String Displayname, String Password) throws RemoteException {
        Player player = PlayerIconController.logInPlayer(Displayname, Password);
        String token = "";
        if (player != null){
            token = PlayerIconController.hashGenerator(Displayname + System.currentTimeMillis());
            mapTokenPlayer.put(token, (PlayerShared)player);
        }
        return token;
    }

    @Override
    public int signUpPlayer(String email, String displayname, String password, String passcheck) throws RemoteException {
        int signUp = PlayerIconController.signUpPlayer(email, displayname, password, passcheck);
        if(signUp == 3){
            Player p = PlayerIconController.createPlayer(displayname.toUpperCase());
            if(!CardDeckController.addDeck(p.getId(), "default")){
                return 1;
            }
            CardDeckController.setSelectedDeck(p.getId(), CardDeckController.getDecksFromPlayer(p.getId()).get(0).getId());
        }
        return signUp;
    }
    
    public PlayerShared getPlayerFromToken(String token) {
        PlayerShared updatedPlayer = PlayerIconController.createPlayer(mapTokenPlayer.get(token).getUsername());
        mapTokenPlayer.replace(token, updatedPlayer);
        return updatedPlayer;
    }
}
