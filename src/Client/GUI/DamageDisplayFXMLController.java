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
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Stan
 */
public class DamageDisplayFXMLController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private Label lblYouPhysicalDamage;
    
    @FXML
    private Label lblYouPhysicalBlock;
    
    @FXML
    private Label lblYouMagicalDamage;
    
    @FXML
    private Label lblYouMagicalBlock;
    
    @FXML
    private Label lblYouHealing;
    
    @FXML
    private Label lblYouResult;
    
    @FXML
    private Label lblEnemyPhysicalDamage;
    
    @FXML
    private Label lblEnemyPhysicalBlock;
    
    @FXML
    private Label lblEnemyMagicalDamage;
    
    @FXML
    private Label lblEnemyMagicalBlock;
    
    @FXML
    private Label lblEnemyHealing;
    
    @FXML
    private Label lblEnemyResult;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
