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
import Client.RMI.RMIClient;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;


/**
 *
 * @author Ramòn Janssen
 */
public class MightyDuelsClient {

    private static final Logger log = Logger.getLogger(MightyDuelsClient.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        log.info("Starting MightyDuels client application");
        
        try {
            FileInputStream in = new FileInputStream("client.properties");
        
            Properties p = new Properties(System.getProperties());
            
            p.load(in);
            
            System.setProperties(p);
            
            System.getProperties().list(System.out);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MightyDuelsClient.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-10000);
        } catch (IOException ex) {
            Logger.getLogger(MightyDuelsClient.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-10001);
        }
        log.info("Starting main application");
        SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash != null) {
            splash.close();
        }
        JFXPanel jfxp = new JFXPanel();
        StageController.getInstance().start();
    }
}
