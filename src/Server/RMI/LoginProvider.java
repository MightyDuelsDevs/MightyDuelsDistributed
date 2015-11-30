/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.RMI;

import java.rmi.server.UnicastRemoteObject;
import Shared.Interfaces.ILoginProvider;
import Server.Domain.Player;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

/**
 *
 * @author Martijn
 */
public class LoginProvider extends UnicastRemoteObject implements ILoginProvider {
    Map<String, Player> mapTokenPlayer;

    private Registry providerRegistry;
    private static final int portNumber = 421;
    private static final String bindingName = "loginProvider";
    
    public LoginProvider() throws RemoteException{
        providerRegistry = LocateRegistry.createRegistry(portNumber);
        //TODO
        providerRegistry.rebind(bindingName, this);
    }
    
    @Override
    public String loginPlayer(String Displayname, String Password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int signUpPlayer(String email, String displayname, String password, String passcheck) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
