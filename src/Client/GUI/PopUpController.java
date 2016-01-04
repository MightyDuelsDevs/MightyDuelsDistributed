/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.GUI;

import Client.Controller.StageController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ramòn Janssen
 */
public class PopUpController implements Initializable {

    @FXML
    private Text tfTitel;
    @FXML
    private Text tfText;
    @FXML
    private Button btnOk;
    @FXML
    private Button btnYes;
    @FXML
    private Button btnNo;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.tfTitel.setText(StageController.title);
        this.tfText.setText(StageController.text);

        btnOk.setVisible(!StageController.yesNo);
        btnYes.setVisible(StageController.yesNo);
        btnNo.setVisible(StageController.yesNo);
    }

    @FXML
    private void btnOk_OnClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) btnOk.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnYes_OnClick(ActionEvent event) throws IOException {
        StageController.choosen = true;
        Stage stage = (Stage) btnYes.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnNo_OnClick(ActionEvent event) throws IOException {
        StageController.choosen = false;
        Stage stage = (Stage) btnNo.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Method that reloads the pop-up.
     */
    public void reload(){
        this.tfTitel.setText(StageController.title);
        this.tfText.setText(StageController.text);

        btnOk.setVisible(!StageController.yesNo);
        btnYes.setVisible(StageController.yesNo);
        btnNo.setVisible(StageController.yesNo);
    }
}
