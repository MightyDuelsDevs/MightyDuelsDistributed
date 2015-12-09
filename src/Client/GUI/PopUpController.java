/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

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
    private Button btOk;
    @FXML
    private Button btYes;
    @FXML
    private Button btNo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setContent(String title, boolean yesNo, String text) {
       // tfTitel.setText(title);
        //tfText.setText(text);
        if (yesNo) {
            btYes.setVisible(true);
            btNo.setVisible(true);
            btOk.setVisible(false);
        } else {
            btYes.setVisible(false);
            btNo.setVisible(false);
            btOk.setVisible(true);
        }
    }

    @FXML
    private void btnOk_OnClick(ActionEvent event) throws IOException {

    }

    @FXML
    private void btnYes_OnClick(ActionEvent event) throws IOException {

    }

    @FXML
    private void btnNo_OnClick(ActionEvent event) throws IOException {

    }
}
