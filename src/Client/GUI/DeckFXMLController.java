/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.GUI;

import Client.Controller.SoundController;
import Client.Controller.StageController;
import Client.Domain.Game;
import Shared.Domain.Deck;
import Shared.Domain.PlayerShared;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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

    private Game game;
    private List<Deck> decks;
    
    /**
     * Initializes the controller class.
     */

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
        StageController.getInstance().navigate(root, title);
    }

    @FXML
    private void btnBack_OnClick(ActionEvent event) throws IOException {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);

        String title = "Mighty Duels";
        String root = "MainScreenFXML.fxml";
        StageController.getInstance().navigate(root, title);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        game = Game.getInstance();
        decks = game.getDecks(game.getToken());
        for(Deck deck : decks){
            System.out.println(deck.getName());
        }
    }

}
