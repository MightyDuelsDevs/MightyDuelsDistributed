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
import javafx.scene.image.Image;
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
    private ImageView ImageViewTutorial;

    private Image tutorialImage1 = new Image("/Client/Resources/Images/Tutorial Screen.png");
    private Image tutorialImage2 = new Image("/Client/Resources/Images/Tutorial Screen2.png");
    private Image tutorialImage3 = new Image("/Client/Resources/Images/Tutorial Screen3.png");
    private Image tutorialImage4 = new Image("/Client/Resources/Images/Tutorial Screen4.png");
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
        ImageViewTutorial.setImage(tutorialImage1);
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
        }

        switch (pagecounter) {
            case 1:
                ImageViewTutorial.setImage(tutorialImage1);
                break;
            case 2:
                ImageViewTutorial.setImage(tutorialImage2);
                break;
            case 3:
                ImageViewTutorial.setImage(tutorialImage3);
                break;
            case 4:
                ImageViewTutorial.setImage(tutorialImage4);
                break;

        }
    }

    @FXML
    private void btnMainMenu_OnClick(ActionEvent event) throws IOException {
        SoundController.play(SoundController.SoundFile.BUTTONPRESS);

        String title = "Mighty Duels";
        String root = "MainScreenFXML.fxml";
        StageController.getInstance().navigate(root, title);
    }
}
