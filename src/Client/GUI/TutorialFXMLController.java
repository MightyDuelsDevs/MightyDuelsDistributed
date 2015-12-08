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
    private ImageView Imv1 = new ImageView("@../Resources/Images/Tutorial%20Screen.png");
    @FXML
    private ImageView Imv2 = new ImageView("@../Resources/Images/Tutorial%20Screen2.png");
    @FXML
    private ImageView Imv3 = new ImageView("@../Resources/Images/Tutorial%20Screen3.png");
    @FXML
    private ImageView Imv4 = new ImageView("@../Resources/Images/Tutorial%20Screen4.png");

    /**
     * Button for the next pages
     */
    @FXML
    private Button btnnextpage = new Button();

    @FXML
    private Button btnmainmenu = new Button();

    int pagecounter = 1;
    /**
     * stagecontroller to switch back to the main menu if selected
     */

    private StageController sc;

    /**
     * Initializes the coontroller class, sets the opacity of the image views to
     * 0
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Imv2.setOpacity(0);
        Imv3.setOpacity(0);
        Imv4.setOpacity(0);
    }

    /**
     * Sets the opacity of the image view corresponding to the page that is
     * selected
     *
     * @param event
     * @throws IOException
     */
    
    public void btnnextpage_OnClick(ActionEvent event) throws IOException {
        pagecounter += pagecounter;
        if (pagecounter == 4) {
            pagecounter = 1;
        } else {
            switch (pagecounter) {
                case 1:
                    setOpacity();
                    Imv1.setOpacity(100);
                case 2:
                    setOpacity();
                    Imv2.setOpacity(100);
                case 3:
                    setOpacity();
                    Imv3.setOpacity(100);
                case 4:
                    setOpacity();
                    Imv4.setOpacity(100);

            }
        }
    }

    public void btnmainmenu_OnClick(ActionEvent event) throws IOException {

        String title = "Mighty Duels";
        String root = "MainScreenFXML.fxml";
        sc.navigate(root, title);
    }

    /**
     * Method to set the opactiy inside the switch case of the button event, to
     * minimise redundancy
     */
    private void setOpacity() {
        Imv1.setOpacity(0);
        Imv2.setOpacity(0);
        Imv3.setOpacity(0);
        Imv4.setOpacity(0);
    }
}
