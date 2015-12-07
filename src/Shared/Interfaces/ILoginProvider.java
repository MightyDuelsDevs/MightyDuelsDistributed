/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared.Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface that shows how to log in and sign up
 * @author Matthijs
 */
public interface ILoginProvider extends Remote {

    /**
     * Function to log in
     * When the log in is validated this function returns the token
     * @param Displayname The username that was used to log in
     * @param Password The password that was used to log in
     * @return Token that describes the current session
     */
    public String loginPlayer(String Displayname, String Password) throws RemoteException;
    
    /**
     * Method to sign up
     * @param email The email that was entered in the sign up form
     * @param displayname The display name that was entered in the sign up form
     * @param password The password that was entered in the sign up form
     * @param passcheck The second password that was entered in the sign up form, this has to be the same as the password
     * @return Returns an integer that has the following values: 
     * 0 = unexpected error. 
     * 1 = password is not the same as the repeated password. 
     * 2 = username already in use. 
     * 3 = successfully created a new player!
     */
    public int signUpPlayer(String email, String displayname, String password, String passcheck) throws RemoteException;
}
