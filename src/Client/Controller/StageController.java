/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author Ramòn Janssen
 */
public class StageController {

    private Stage stage;

    /**
     *
     * @return
     */
    public Stage getStage() {
        return stage;
    }

    /**
     *
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     *
     */
    public void start() {
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
    }

    /**
     * Navigate to another scene
     *
     * @param root the root scene
     * @param title the window title
     */
    public void navigate(String root, String title) {
        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../GUI/" + root)));
            this.stage.setScene(scene);
            title = title == null ? "Mighty Duels" : title;
            this.stage.setTitle(title);
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            this.stage.setX((primScreenBounds.getWidth() - this.stage.getWidth()) / 2);
            this.stage.setY((primScreenBounds.getHeight() - this.stage.getHeight()) / 2);
            this.stage.show();
        } catch (IOException ex) {
            Logger.getLogger(StageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param title
     * @param yesNo
     * @param text
     */
    public void popup(String title, boolean yesNo, String text) {
        try {
            Stage popUpStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../GUI/PopUp.fxml"));
            Scene scene = new Scene(root);
            popUpStage.setScene(scene);
            title = title == null ? "Mighty Duels" : title;
            popUpStage.setTitle(title);
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            popUpStage.setX((primScreenBounds.getWidth() - popUpStage.getWidth()) / 2);
            popUpStage.setY((primScreenBounds.getHeight() - popUpStage.getHeight()) / 2);
            popUpStage.show();
        } catch (IOException ex) {
            Logger.getLogger(StageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}