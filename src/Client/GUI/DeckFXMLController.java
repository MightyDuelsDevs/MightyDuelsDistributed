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
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author Stan
 */
public class DeckFXMLController implements Initializable {

    private Game game;
    private List<Deck> decks;
    private static String selectedDeck = "";
    
    /**
     * Initialises the controller class.
     */

    @FXML
    private Button btnBack;

    @FXML
    private Button btnPlay;

    @FXML
    private GridPane gpDecks;
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
        
        final ToggleGroup tg = new ToggleGroup();
        int l = 1; // ID
        int i = 0; // Collomn
        int j = 0; // Row
        for (Deck deck : decks) {
            // Icon Image
            Image image = new Image("/Client/Resources/Images/card stack.png", 160, 230, false, false);
            ImageView ivDeck = new ImageView(image);
            Label deckNameLabel = new Label(deck.getName());
            ivDeck.setId("" + deck.getName());
            ivDeck.setOnMouseClicked((javafx.scene.input.MouseEvent event) -> {
                ImageView iv = (ImageView) event.getSource();
                if (selectedDeck != iv.getId()) {
                    //TODO sc.popup("Error", false, "You have selected Icon number: " + iv.getId() + ".", "Icon selected");
                }
                selectedDeck = iv.getId();
                System.out.println(selectedDeck);
            });
            gpDecks.setHalignment(ivDeck, HPos.CENTER);
            gpDecks.add(ivDeck, i, j);
            
            gpDecks.setHalignment(deckNameLabel, HPos.CENTER);
            gpDecks.setValignment(deckNameLabel, VPos.BOTTOM);
            gpDecks.add(deckNameLabel, i, j);

            i++;
            l++;
            
            if(i == 2){
                i = 0;
                j = 1;
            }
        }
        
    }
}
