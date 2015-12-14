/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.GUI;

import Client.Controller.SoundController;
import Client.Controller.StageController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
    private void btnDuel_OnClick(ActionEvent event) throws IOException {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);
        
        MatchController match = new MatchController();
        match.searchingMatch();
//        String title = "Let the Duel begin!!!";
//        String root = "Match.fxml";
//        StageController.getInstance().navigate(root, title);
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
