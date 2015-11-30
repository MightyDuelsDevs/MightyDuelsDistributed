/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Run;

import Server.Controller.CardDeckController;
import Server.Controller.PlayerIconController;
import Server.Domain.Game;
import Server.Domain.Player;
import java.awt.SplashScreen;
import java.sql.SQLException;
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
        SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash != null) {
            splash.close();
        }
    }
}
