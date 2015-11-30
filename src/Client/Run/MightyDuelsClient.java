/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Run;

import Client.Domain.Game;
import java.awt.SplashScreen;
import java.util.logging.Logger;
import Client.Controller.StageController;


/**
 *
 * @author Ramòn Janssen
 */
public class MightyDuelsClient {

    private static final Logger log = Logger.getLogger(MightyDuelsClient.class.getName());
    public static Game game;//TODO <- not this

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        log.info("Starting MightyDuels client application");
        log.info("Starting main application");
        SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash != null) {
            splash.close();
        }
        StageController sc = new StageController();
        sc.start();
    }
}
