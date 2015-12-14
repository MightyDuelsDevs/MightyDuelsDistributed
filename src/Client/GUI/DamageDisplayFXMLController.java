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
import javafx.scene.control.Label;
import javafx.stage.Stage;

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
        this.lblYouPhysicalDamage.setText(StageController.physicalDamageYou);
        this.lblYouPhysicalBlock.setText(StageController.physicalBlockYou);
        this.lblYouMagicalDamage.setText(StageController.magicalDamageYou);
        this.lblYouMagicalBlock.setText(StageController.magicalBlockYou);
        this.lblYouHealing.setText(StageController.healingYou);
        this.lblYouResult.setText(StageController.resultYou);
        this.lblEnemyPhysicalDamage.setText(StageController.physicalDamageEnemy);
        this.lblEnemyPhysicalBlock.setText(StageController.physicalBlockEnemy);
        this.lblEnemyMagicalDamage.setText(StageController.magicalDamageEnemy);
        this.lblEnemyMagicalBlock.setText(StageController.magicalBlockEnemy);
        this.lblEnemyHealing.setText(StageController.healingEnemy);
        this.lblEnemyResult.setText(StageController.resultEnemy);
    }

}
