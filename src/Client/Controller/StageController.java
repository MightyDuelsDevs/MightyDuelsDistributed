/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Controller;

import Client.GUI.DamageDisplayFXMLController;
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

    /**
     * The title of the stage.
     */
    public static String title;

    /**
     * The text of the stage.
     */
    public static String text;
    
    /**
     * The physical damage you deal.
     */
    public static String physicalDamageYou;

    /**
     * The physical block you put up.
     */
    public static String physicalBlockYou;

    /**
     * The magical damage you deal.
     */
    public static String magicalDamageYou;

    /**
     * The magical block you put up.
     */
    public static String magicalBlockYou; 

    /**
     * The healing you do.
     */
    public static String healingYou;

    /**
     * The net gain or loss of your life this turn.
     */
    public static String resultYou;

    /**
     * The physical damage done by the enemy.
     */
    public static String physicalDamageEnemy;

    /**
     * The physical block put up by the enemy.
     */
    public static String physicalBlockEnemy;

    /**
     * The magical damage done by the enemy.
     */
    public static String magicalDamageEnemy;

    /**
     * The magical block put up by the enemy.
     */
    public static String magicalBlockEnemy;

    /**
     * The healing done by the enemy.
     */
    public static String healingEnemy;

    /**
     * The net gain or loss of your opponents life this turn.
     */
    public static String resultEnemy;
    
    /**
     * Determines if the pop-up has an "YES/NO" or an "OK" button.
     */
    public static boolean yesNo;

    /**
     * Keeps track if the user pressed "YES" or "NO".
     */
    public static boolean choosen;

    /**
     * Returns the only instance of the StageController.
     * If this does not exist a StageController is created.
     * @return The only instance of the StageController.
     */
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

    /**
     * Method that closes the pop-up.
     */
    public void closePopUp() {
        synchronized (this) {
            this.notify();
        }
        if (popUpStage != null){
            popUpStage.close();
        }
    }

    /**
     * Method that creates a pop-up which contains all the data of the previous turn.
     * @param physicalDamageYou, the physical damage dealt by you.
     * @param physicalBlockYou, the physical block put up by you.
     * @param magicalDamageYou, the magical damage dealt by you.
     * @param magicalBlockYou, the magical block put up by you.
     * @param healingYou, the healing done by you.
     * @param resultYou, the net gain or loss of your life this turn.
     * @param physicalDamageEnemy, the physical damage dealt by your opponent.
     * @param physicalBlockEnemy, the physical block put up by your opponent.
     * @param magicalDamageEnemy, the magical damage dealt by your opponent.
     * @param magicalBlockEnemy, the magical block put up by your opponent.
     * @param healingEnemy, the healing done by your opponent.
     * @param resultEnemy, the net gain or loss of your opponents life this turn.
     */
    public void dmgPopup(String physicalDamageYou, String physicalBlockYou, String magicalDamageYou, String magicalBlockYou, String healingYou, String resultYou, String physicalDamageEnemy, String physicalBlockEnemy, String magicalDamageEnemy, String magicalBlockEnemy, String healingEnemy, String resultEnemy) {
        popUpStage = new Stage();
        StageController.physicalDamageYou = physicalDamageYou;
        StageController.physicalBlockYou = physicalBlockYou;
        StageController.magicalDamageYou = magicalDamageYou;
        StageController.magicalBlockYou = magicalBlockYou;
        StageController.healingYou = healingYou;
        StageController.resultYou = resultYou;
        StageController.physicalDamageEnemy = physicalDamageEnemy;
        StageController.physicalBlockEnemy = physicalBlockEnemy;
        StageController.magicalDamageEnemy = magicalDamageEnemy;
        StageController.magicalBlockEnemy = magicalBlockEnemy;
        StageController.healingEnemy = healingEnemy;
        StageController.resultEnemy = resultEnemy;

        Platform.runLater(() -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../GUI/DamageDisplayFXML.fxml"));
                Scene scene = new Scene(root);

                popUpStage.initStyle(StageStyle.UNDECORATED);
                //popUpStage.initModality(Modality.WINDOW_MODAL);
                popUpStage.initOwner(stage.getScene().getWindow());
                popUpStage.setScene(scene);
                popUpStage.centerOnScreen();
                popUpStage.show();
            } catch (IOException ex) {
                Logger.getLogger(StageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
