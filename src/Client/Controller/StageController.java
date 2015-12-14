/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Controller;

import Client.GUI.PopUpController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Ramòn Janssen
 */
public class StageController {

    private Stage stage, popUpStage, oldPopUpStage;
    private static StageController instance;

    public static String title, text;
    public static boolean yesNo, choosen;

    public static StageController getInstance() {
        if (instance == null) {
            instance = new StageController();
        }
        return instance;
    }

    /**
     * Returns the main stage
     *
     * @return
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Sets the main stage
     *
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Starts the main stage
     */
    public void start() {
        Platform.runLater(() -> {
            try {
                this.setStage(new Stage());
                Parent root = FXMLLoader.load(getClass().getResource("../GUI/LogOnFXML.fxml"));
                Scene scene = new Scene(root);
                this.stage.setScene(scene);
                this.stage.show();
                this.stage.setTitle("Mighty Duels - Login");
                this.stage.setResizable(false);
                Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                this.stage.setX((primScreenBounds.getWidth() - this.stage.getWidth()) / 2);
                this.stage.setY((primScreenBounds.getHeight() - this.stage.getHeight()) / 2);
            } catch (IOException ex) {
                Logger.getLogger(StageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     * Navigate to another scene
     *
     * @param root the root scene
     * @param title the window title
     */
    public void navigate(String root, String title) {
        title = title == null ? "Mighty Duels" : title;
        this.stage.setTitle(title);
        Platform.runLater(() -> {
            try {
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../GUI/" + root)));
                this.stage.setScene(scene);
                Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                this.stage.setX((primScreenBounds.getWidth() - this.stage.getWidth()) / 2);
                this.stage.setY((primScreenBounds.getHeight() - this.stage.getHeight()) / 2);
                this.stage.show();
            } catch (IOException ex) {
                Logger.getLogger(StageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     * Create popup with the given valeus
     *
     * @param title title of the popup
     * @param yesNo true = yes & no button, false = ok button
     * @param text text of the popup
     */
    public void popup(String title, boolean yesNo, String text) {
        title = title == null ? "Mighty Duels" : title;
        StageController.title = title;
        StageController.text = text;
        StageController.yesNo = yesNo;

        oldPopUpStage = popUpStage;
        popUpStage = new Stage();
        popUpStage.setTitle(title);

        Platform.runLater(() -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../GUI/PopUp.fxml"));
                Scene scene = new Scene(root);
                popUpStage.initStyle(StageStyle.UNDECORATED);
                popUpStage.initModality(Modality.WINDOW_MODAL);
                popUpStage.initOwner(stage.getScene().getWindow());
                popUpStage.setScene(scene);
                popUpStage.centerOnScreen();
                popUpStage.show();
                if (oldPopUpStage != null) {
                    oldPopUpStage.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(StageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public void closePopUp() {
        popUpStage.close();
    }

    public void dmgPopup(String title, boolean show, String a, String b) {
        title = title == null ? "Mighty Duels" : title;

        Stage popUpStage = new Stage();
        popUpStage.setTitle(title);

        Platform.runLater(() -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../GUI/DamageDisplayFXML.fxml"));
                Scene scene = new Scene(root);

                //popUpStage.initStyle(StageStyle.UNDECORATED);
                //popUpStage.initModality(Modality.WINDOW_MODAL);
                popUpStage.initOwner(stage.getScene().getWindow());
                popUpStage.setScene(scene);
                popUpStage.centerOnScreen();
                if (show) {
                    popUpStage.show();
                } else {
                    popUpStage.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(StageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
