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
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Stan
 */
public class DeckFXMLController implements Initializable {

    private Game game;
    private static List<Deck> decks;
    private static Deck selectedDeck;

    /**
     * Initialises the controller class.
     */
    @FXML
    private Button btnBack;

    @FXML
    private Button btnPlay;

    @FXML
    private GridPane gpDecks;

    @FXML
    private Button btnCreateDeck;

    @FXML
    private Button btnRemoveDeck;

    @FXML
    private Label lblSelectedDeck;

    @FXML
    private TextField tfDeckName;

    //Set the right deck as 'selected' into the database when pressed on 'Play'.
    @FXML
    private void btnPlay_OnClick(ActionEvent event) throws IOException {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);

        String title = "Let the Duel begin!!!";
        String root = "Match.fxml";
        StageController.getInstance().navigate(root, title);
    }

    @FXML
    private void btnBack_OnClick(ActionEvent event) throws IOException {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);

        String title = "Mighty Duels";
        String root = "MainScreenFXML.fxml";
        StageController.getInstance().navigate(root, title);
    }

    @FXML
    private void btnCreateDeck_OnClick(ActionEvent event) throws IOException {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);

        if(decks.size() < 4){
            game.addDeck(game.getToken(), tfDeckName.getText());
        }
        //TODO pop-up for to much decks.
        String title = "Mighty Duels";
        String root = "DeckFXML.fxml";
        StageController.getInstance().navigate(root, title);
    }

    @FXML
    private void btnRemoveDeck_OnClick(ActionEvent event) throws IOException {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);

        game.removeDeck(game.getToken(), selectedDeck.getId());

        String title = "Mighty Duels";
        String root = "DeckFXML.fxml";
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

        selectedDeck = decks.get(0);
        lblSelectedDeck.setText("Selected Deck: " + selectedDeck.getName());

        for (Deck deck : decks) {
            // Icon Image
            Image image = new Image("/Client/Resources/Images/card stack.png", 160, 230, false, false);
            ImageView ivDeck = new ImageView(image);
            Label deckNameLabel = new Label(deck.getName());
            deckNameLabel.setTextFill(Color.web("#FFFFFF"));
            ivDeck.setId("" + deck.getName());
            ivDeck.setOnMouseClicked((javafx.scene.input.MouseEvent event) -> {
                ImageView iv = (ImageView) event.getSource();
                if (selectedDeck.getName() != iv.getId()) {
                    //TODO sc.popup("Error", false, "You have selected Icon number: " + iv.getId() + ".", "Icon selected");
                }
                DeckFXMLController.selectDeck(iv.getId());
                lblSelectedDeck.setText("Selected Deck: " + selectedDeck.getName());
                game.setSelectedDeck(game.getToken(), selectedDeck.getId());
            });
            gpDecks.setHalignment(ivDeck, HPos.CENTER);
            gpDecks.add(ivDeck, i, j);

            gpDecks.setHalignment(deckNameLabel, HPos.CENTER);
            gpDecks.setValignment(deckNameLabel, VPos.BOTTOM);
            gpDecks.add(deckNameLabel, i, j);

            i++;
            l++;

            if (i == 2) {
                i = 0;
                j = 1;
            }
        }

    }

    static public void selectDeck(String name) {
        for (Deck deck : decks) {
            if (deck.getName() == null ? name == null : deck.getName().equals(name)) {
                selectedDeck = deck;
            }
        }
    }
}
