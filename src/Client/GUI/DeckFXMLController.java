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
public class DeckFXMLController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private StageController sc;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnPlay;

    //Variables for playing sound.
    private final String buttonPressFilePath = "src/Sound/buttonPress.wav";

    //Set the right deck as 'selected' into the database when pressed on 'Play'.
    @FXML
    private void btnPlay_OnClick(ActionEvent event) throws IOException {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);

        String title = "Let the Duel begin!!!";
        String root = "GUIMatch.fxml";
        sc.navigate(root, title);
    }

    @FXML
    private void btnBack_OnClick(ActionEvent event) throws IOException {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);

        String title = "Mighty Duels";
        String root = "MainScreenFXML.fxml";
        sc.navigate(root, title);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}
