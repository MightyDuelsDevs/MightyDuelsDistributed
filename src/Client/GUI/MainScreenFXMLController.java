/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.GUI;

import Client.Controller.SoundController;
import Client.Controller.StageController;
import Client.Domain.Game;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author Stan
 */
public class MainScreenFXMLController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button btnDuel;
    @FXML
    private Button btnNewDeck;
    @FXML
    private Button btnAccount;
    @FXML
    private Button btnLogOut;
    @FXML
    private Button btnTutorial;

    private Timer timer;
    private int sec;

    @FXML
    private void btnDuel_OnClick(ActionEvent event) throws IOException {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);

        MatchController.setHash(Game.getInstance().startMatch());

        this.timer = new Timer();
        sec = 5;
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                Platform.runLater(() -> {
                    StageController sc = StageController.getInstance();
                    if (sec > 1) {
                        sc.popup("Searching for a match", false, " Searching for a match: " + sec + " seconds");
                        sec--;
                    } else if (sec == 1) {
                        sc.popup("Searching for a match", false, " Searching for a match: " + sec + " second");
                        sec--;
                    } else if (sec == 0) {
                        sc.popup("Searching for a match", false, " Searching for a match");
                        sec = 5;
                    } 
                    // Check if a match is found
                    if (StageController.matchFound){
                        sc.closePopUp();
                        StageController.matchFound = false;
                        this.cancel();
                    }
                });
            }
        }, 0, 1 * 1000);

        String title = "Let the Duel begin!!!";
        String root = "Match.fxml";
        StageController.getInstance().navigate(root, title);
    }

    @FXML
    private void btnNewDeck_OnClick(ActionEvent event) throws IOException {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);

        String title = "Mighty Duels";
        String root = "DeckFXML.fxml";
        StageController.getInstance().navigate(root, title);
    }

    @FXML
    private void btnLogOut_OnClick(ActionEvent event) throws IOException {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);

        String title = "Mighty Duels";
        String root = "LogOnFXML.fxml";
        StageController.getInstance().navigate(root, title);
    }

    @FXML
    private void btnAccount_OnClick(ActionEvent event) throws IOException {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);

        String title = "Mighty Duels";
        String root = "AccountFXML.fxml";
        StageController.getInstance().navigate(root, title);
    }

    @FXML
    private void btnTutorial_OnClick(ActionEvent event) throws IOException {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);

        String title = "Mighty Duels";
        String root = "TutorialFXML.fxml";
        StageController.getInstance().navigate(root, title);
    }

    @FXML
    private void btnSpectate_OnClick(ActionEvent event) throws IOException {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);

        System.out.println("Test");
        String title = "Mighty Duels";
        String root = "TutorialFXML.fxml";
        StageController.getInstance().navigate(root, title);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
