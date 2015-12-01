/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.RMI;

import Server.Controller.PlayerIconController;
import java.rmi.server.UnicastRemoteObject;
import Shared.Interfaces.ILoginProvider;
import Server.Domain.Player;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import Shared.Domain.PlayerShared;

/**
 *
 * @author Martijn
 */
public class LoginProvider extends UnicastRemoteObject implements ILoginProvider {
    private Map<String, Player> mapTokenPlayer;

    private Registry providerRegistry = null;
    private static final int portNumber = 421;
    private static final String bindingName = "loginProvider";
    
    public LoginProvider() throws RemoteException{
        providerRegistry = LocateRegistry.createRegistry(portNumber);
        //TODO
        providerRegistry.rebind(bindingName, this);
    }
    
    @Override
    public String loginPlayer(String Displayname, String Password) {
        Player player = PlayerIconController.logInPlayer(Displayname, Password);
        String token = "";
        if (player != null){
            token = PlayerIconController.hashGenerator(Displayname + System.currentTimeMillis());
            mapTokenPlayer.put(token, player);
        }
        return token;
    }

    @Override
    public int signUpPlayer(String email, String displayname, String password, String passcheck) {
        return PlayerIconController.signUpPlayer(email, displayname, password, passcheck);
    }
    
    public Player getPlayerFromToken(String token) {
        return mapTokenPlayer.get(token);
    }
}
