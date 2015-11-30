/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.GUI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Loek
 */
public class TutorialFXMLController implements Initializable {

    @FXML 
    private ImageView Imv1 = new ImageView("@../Resources/Images/Tutorial%20Screen.png");
    @FXML 
    private ImageView Imv2 = new ImageView("@../Resources/Images/Tutorial%20Screen2.png");
    @FXML 
    private ImageView Imv3 = new ImageView("@../Resources/Images/Tutorial%20Screen3.png");
    @FXML 
    private ImageView Imv4 = new ImageView("@../Resources/Images/Tutorial%20Screen4.png");
    
  
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Imv1.setOpacity(0);
        Imv2.setOpacity(0);
        Imv3.setOpacity(0);
        Imv4.setOpacity(0);
    }    
    
}
