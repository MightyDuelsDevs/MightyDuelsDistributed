/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Run;

import Client.GUI.MainScreenFXMLController;
import Server.Controller.CardDeckController;
import Server.Controller.PlayerIconController;
import Server.Domain.Game;
import Server.Domain.Player;
import Server.RMI.LoginProvider;
import Server.RMI.MainScreenProvider;
import java.awt.SplashScreen;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramòn Janssen
 */
public class MightyDuelsServer {

    private static final Logger log = Logger.getLogger(MightyDuelsServer.class.getName());

    public static Player loggedInPlayer = null; //TODO <- not this
    public static Game game;//TODO <- not this

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        log.info("Starting MightyDuels server application");
        log.info("Initialising database");
        Server.Database.Database.openConnection();
        try {
            if (!Server.Database.Database.checkConnection()) {
                log.severe("Connection was not opened!");
                log.severe("Exiting...");
                System.exit(1000);
            }
        } catch (SQLException ex) {
            log.severe("Database threw an exception!");
            log.severe(ex.toString());
            log.severe("Exiting....");
            System.exit(1001);
        }
        log.info("Creating Game instance");
        game = new Game();
        log.info("Creating CardDeckController");
        CardDeckController.cardDeckControllerInit();
        log.info("Creating PlayerIconController");
        PlayerIconController.playerIconControllerInit();
        log.info("Starting main application");
//        SplashScreen splash = SplashScreen.getSplashScreen();
//        if (splash != null) {
//            splash.close();
//        }
        
        try {
            LoginProvider loginProvider = new LoginProvider();
            MainScreenProvider mainScreenProvider = new MainScreenProvider(loginProvider);
            boolean running = true;
            Scanner s = new Scanner(System.in);
            
            Runtime runtime = Runtime.getRuntime();
            NumberFormat format = NumberFormat.getInstance();
            
            while (running){
                String input = s.nextLine();
                if(input.startsWith("help")){
                    if(input.equals("help")){
                        //print generic help
                    }else{
                        if(input.contains("stats")){
                            System.out.println("This application print's the system statistics and the some player sttistics.");
                        }
                        if(input.contains("quit")){
                            System.out.println("This command stops the server.");
                        }
                    }
                }
                if(input.startsWith("commands")){
                    System.out.println("stats    print the application statistics");
                    System.out.println("help     print the help of this application");
                    System.out.println("quit     stop the server");
                    //todo print all known commands
                }
                if(input.startsWith("stats")){
                    System.out.println("System Usage: ");

                    long maxMemory = runtime.maxMemory();
                    long allocatedMemory = runtime.totalMemory();
                    long freeMemory = runtime.freeMemory();

                    System.out.println("free memory: " + format.format(freeMemory / 1024));
                    System.out.println("allocated memory: " + format.format(allocatedMemory / 1024));
                    System.out.println("max memory: " + format.format(maxMemory / 1024));
                    System.out.println("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
                    System.out.println();
                    
                    System.out.println("Players online: " + "N/A");
                }
                if(input.startsWith("quit")){
                    System.out.print("Are you sure?[Y/N] ");
                    if(s.nextLine().equalsIgnoreCase("Y")){
                        System.out.println();
                        System.out.println("Stopping....");
                        //todo stop all services and matches
                        Server.SocketManagerServer.SocketManager.getInstance().stop();
                        running=false;
                    }else{
                        System.out.println();
                    }
                }
            }
        } catch (RemoteException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }
}
