/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.GUI;

import Client.Controller.SoundController;
import Client.Controller.StageController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Loek
 */
public class TutorialFXMLController implements Initializable {

    /**
     * Creates a number of imageveiws used to controll the tutorial form
     * background
     */
    @FXML
    private ImageView Imv1 = new ImageView("/Client/Resources/Images/Tutorial Screen.png");
    @FXML
    private ImageView Imv2 = new ImageView("/Client/Resources/Images/Tutorial Screen2.png");
    @FXML
    private ImageView Imv3 = new ImageView("/Client/Resources/Images/Tutorial Screen3.png");
    @FXML
    private ImageView Imv4 = new ImageView("/Client/Resources/Images/Tutorial Screen4.png");

    /**
     * Button for the next pages
     */
    @FXML
    private Button btnNextPage;
    @FXML
    private Button btnMainMenu;

    private int pagecounter;

    /**
     * Initializes the coontroller class, sets the opacity of the image views to
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Imv2.setOpacity(0.0f);
        Imv3.setOpacity(0.0f);
        Imv4.setOpacity(0.0f);
        pagecounter = 1;
    }

    /**
     * Sets the opacity of the image view corresponding to the page that is
     * selected
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void btnNextPage_OnClick(ActionEvent event) throws IOException {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);

        pagecounter++;
        if (pagecounter == 5) {
            pagecounter = 1;
        } else {
            switch (pagecounter) {
                case 1:
                    setOpacity();
                    Imv1.setOpacity(1.0f);
                    break;
                case 2:
                    setOpacity();
                    Imv2.setOpacity(1.0f);
                    break;
                case 3:
                    setOpacity();
                    Imv3.setOpacity(1.0f);
                    break;
                case 4:
                    setOpacity();
                    Imv4.setOpacity(1.0f);
                    break;
            }
        }
    }

    @FXML
    public void btnMainMenu_OnClick(ActionEvent event) throws IOException {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);

        String title = "Mighty Duels";
        String root = "MainScreenFXML.fxml";
        StageController.getInstance().navigate(root, title);
    }

    /**
     * Method to set the opactiy inside the switch case of the button event, to
     * minimise redundancy
     */
    private void setOpacity() {
        Imv1.setOpacity(0.0f);
        Imv2.setOpacity(0.0f);
        Imv3.setOpacity(0.0f);
        Imv4.setOpacity(0.0f);
    }
}
