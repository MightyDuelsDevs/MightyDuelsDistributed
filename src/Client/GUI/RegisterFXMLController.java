/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.GUI;

import Client.Controller.SoundController;
import Client.Controller.StageController;
import Client.Domain.Player;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
public class RegisterFXMLController implements Initializable {

    private StageController sc;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfUserName;

    @FXML
    private TextField tfPassWord;

    @FXML
    private TextField tfPassWordRe;

    @FXML
    private Button btnRegisterAccount;

    @FXML
    private Button btnBack;

    Player player;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    /**
     * Adds the account to the database and goes to the MainScreen if everything
     * is filled in correctly. Fields are correct: If both Fields are filled. If
     * the Username is unique. If the two password fields are the same.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void btnRegisterAccount_OnClick(ActionEvent event) throws IOException {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);

        //Check  if  the UserName already exists
        //If not, Check if the 2 inserted passwords match. If they do, Make the Account.
        if (tfEmail.getText().isEmpty() || tfUserName.getText().isEmpty() || tfPassWord.getText().isEmpty() || tfPassWordRe.getText().isEmpty()) {
            sc.popup("Error", false, "Fill all the fields.");
        } else {
            int result = playerIconController.signUpPlayer(tfEmail.getText(), tfUserName.getText(), tfPassWord.getText(), tfPassWordRe.getText());
            switch (result) {
                case 0:
                    sc.popup("Error", false, "An unexpected error occurred.");
                    break;
                case 1:
                    sc.popup("Error", false, "The two inserted passwords do NOT match.");
                    break;
                case 2:
                    sc.popup("Error", false, "Account already exists.");
                    break;
                case 3:
                    //Add the Player to the database.
                    sc.popup("Error", false, "Account succesfully registered.");     
                    String title = "Mighty Duels";
                    String root = "LogOnFXML.fxml";
                    sc.navigate(root, title);
                    break;
                default:
                    sc.popup("Error", false, "An unexpected error occurred.");
                    break;
            }
        }
    }

    @FXML
    private void btnBack_OnClick(ActionEvent event) throws IOException {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);

        String title = "Mighty Duels";
        String root = "LogOnFXML.fxml";
        sc.navigate(root, title);
    }

}
