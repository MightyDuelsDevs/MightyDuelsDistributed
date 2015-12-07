/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.GUI;

import Client.Controller.SoundController;
import Client.Controller.StageController;
import Client.Domain.Game;
import Shared.Domain.PlayerShared;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Stan
 */
public class LogOnFXMLController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private Game game = Game.getInstance();
    private PlayerShared loggedInPlayer = game.getPlayer();

    @FXML
    private TextField tfUserName;

    @FXML
    private TextField tfPassWord;

    @FXML
    private Button btnLogOn;

    @FXML
    private Button btnRegister;

    //Variables for playing sound.
    private final String buttonPressFilePath = "src/Sound/buttonPress.wav";

    /**
     * If the Username already exists, give a message. If the Username and
     * Password do not match. give a message. When The username and password
     * match with the ones in de database, Go to the Mainscreen.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void btnLogOn_OnClick(ActionEvent event) {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);

        login();
    }

    /**
     * Go to the 'Register'-page to register an account.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void btnRegister_OnClick(ActionEvent event) throws IOException {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);

        String title = "Mighty Duels";
        String root = "RegisterFXML.fxml";
        
            StageController.getInstance().navigate(root, title);
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfUserName.setOnAction((evt) -> {
            login();
        });
        tfPassWord.setOnAction((evt) -> {
            login();
        });
    }

    private void login() {
        if (tfUserName.getText().isEmpty() || tfPassWord.getText().isEmpty()) {
            StageController.getInstance().popup("Error", false, "Fill both fields.");
        } else {
            PlayerShared player = game.loginPlayer(tfUserName.getText(), tfPassWord.getText());
            if (player == null) {
                StageController.getInstance().popup("Error", false, "Username & Password do not match.");
                tfPassWord.setText("");
            } else {
                loggedInPlayer = player;
                //Give the player to the next page;
                String title = "Mighty Duels Welcome: " + player.getUsername();
                String root = "MainScreenFXML.fxml";
                StageController.getInstance().navigate(root, title);
            }
        }
    }

}
