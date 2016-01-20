/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.GUI;

import Shared.Domain.Card;
import Shared.Domain.HeroCard;
import Shared.Domain.MinionCard;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author Matthijs
 */
public class CardControl {

    private final MinionCard minionCard;
    private final HeroCard heroCard;
    private final int width = 180;
    private EventHandler handler;
    private Label lHealth;
    private StackPane pane;
    private Label lTarget;

    /**
     * Method that checks what kind of card is being player. This can either be
     * a HeroCard or a MinionCard.
     *
     * @param card, the card that is being checked.
     */
    public CardControl(Card card) {
        if (card instanceof MinionCard) {
            this.minionCard = (MinionCard) card;
            this.heroCard = null;
        } else if (card instanceof HeroCard) {
            this.heroCard = (HeroCard) card;
            this.minionCard = null;
        } else {
            this.heroCard = null;
            this.minionCard = null;
        }
    }

    /**
     * Method for giving the CardControl an EventHandler.
     *
     * @param handler, the EventHandler that will be associated with this
     * CardControl.
     */
    public void setEventHandler(EventHandler handler) {
        this.handler = handler;
    }

    /**
     * Method that returns the card that this CardControl represents. This can
     * either be a HeroCard or a MinionCard.
     *
     * @return the card that this CardControl represents.
     */
    public Card getCard() {
        if (minionCard != null) {
            return minionCard;
        } else if (heroCard != null) {
            return heroCard;
        } else {
            return null;
        }
    }

    public StackPane oldCardPane() {
        return pane;
    }

    /**
     * Method that creates the visual of the card. The card will look different
     * depending on what type of card it is.
     *
     * @return A StackPane that represents the card of the CardControl.
     */
    public StackPane CardPane() {
        AnchorPane pane = new AnchorPane();
        Font font = new Font("Matura MT Script Capitals", 18.0);
        Font fontBig = new Font("Matura MT Script Capitals", 22.0);
        ImageView imOverlay = new ImageView(), imNic = new ImageView();

        if (minionCard != null) {
            imOverlay.setImage(new Image("/Client/Resources/Images/MinionCard Template.png"));
            imNic.setImage(new Image("/Client/Resources/Images/Cards/C" + minionCard.getId() + ".jpg"));

        }
        if (heroCard != null) {
            imOverlay.setImage(new Image("/Client/Resources/Images/Card Template.png"));
            imNic.setImage(new Image("/Client/Resources/Images/Cards/C" + heroCard.getId() + ".jpg"));
        }

        imOverlay.setPreserveRatio(true);
        imNic.setPreserveRatio(true);
        imOverlay.setFitWidth(width);
        imNic.setFitWidth(width - 40);
        imNic.setLayoutX(20);
        Label lName = new Label();
        lName.setFont(fontBig);
        lName.setLayoutX(70);
        lName.setLayoutY(140);
        lName.setAlignment(Pos.CENTER);
        lName.setPrefWidth(width / 6 * 5);
        lName.setTextAlignment(TextAlignment.CENTER);

        Group grImg = new Group(imNic, imOverlay);

        if (minionCard != null) {
            pane.setOnMouseEntered((event) -> {
                Logger.getLogger(CardControl.class.getName()).log(Level.INFO, "OnMouseEntered", event);
                int depth = 70; //Setting the uniform variable for the glow width and height

                DropShadow borderGlow = new DropShadow();
                borderGlow.setOffsetY(0f);
                borderGlow.setOffsetX(0f);
                borderGlow.setColor(Color.RED);
                borderGlow.setWidth(depth);
                borderGlow.setHeight(depth);

                grImg.setEffect(borderGlow); //Apply the borderGlow effect to the JavaFX node
            });

            //Name
            lName.setText(minionCard.getName());//Name

            //Sword x=58 y=360
            Label lPAttack = new Label(minionCard.getPhysicalDamage() + "");
            lPAttack.setFont(font);
            lPAttack.setLayoutX(88);
            lPAttack.setLayoutY(240);
            lPAttack.setTextFill(Color.web("#FFFFFF"));

            //MAttack x=235 y=366
            Label lMAttack = new Label(minionCard.getMagicalDamage() + "");
            lMAttack.setFont(font);
            lMAttack.setLayoutX(200);
            lMAttack.setLayoutY(240);
            lMAttack.setTextFill(Color.web("#FFFFFF"));

            //Hart x=148, y=441
            lHealth = new Label(minionCard.getHitPoints() + "");
            lHealth.setFont(font);
            lHealth.setLayoutX(145);
            lHealth.setLayoutY(200);
            lHealth.setTextFill(Color.web("#FFFFFF"));

            //Target
            lTarget = new Label();
            lTarget.setFont(new Font(18.0));
            lTarget.setLayoutX(65);
            lTarget.setLayoutY(-32);
            lTarget.setAlignment(Pos.CENTER);
            lTarget.setTextAlignment(TextAlignment.CENTER);
            lTarget.setTextFill(Color.web("#FFFF00"));
            pane.getChildren().addAll(lName, lPAttack, lHealth, lMAttack, lTarget);
            
        }

        if (heroCard != null) {
            pane.setOnMouseEntered((event) -> {
                Logger.getLogger(CardControl.class.getName()).log(Level.INFO, "OnMouseEntered", event);
                int depth = 70; //Setting the uniform variable for the glow width and height

                DropShadow borderGlow = new DropShadow();
                borderGlow.setOffsetY(0f);
                borderGlow.setOffsetX(0f);
                borderGlow.setColor(Color.BLUE);
                borderGlow.setWidth(depth);
                borderGlow.setHeight(depth);

                grImg.setEffect(borderGlow); //Apply the borderGlow effect to the JavaFX node
            });
            //Name
            lName.setText(heroCard.getName());

            //Sword x=58 y=360
            Label lPAttack = new Label(heroCard.getPhysicalDamage() + "");
            lPAttack.setFont(font);
            lPAttack.setLayoutX(88);
            lPAttack.setLayoutY(200);
            lPAttack.setTextFill(Color.web("#FFFFFF"));

            //Shield x=52 y=436
            Label lPDefence = new Label(heroCard.getPhysicalBlock() + "");
            lPDefence.setFont(font);
            lPDefence.setLayoutX(88);
            lPDefence.setLayoutY(242);
            lPDefence.setTextFill(Color.web("#FFFFFF"));

            //MAttack x=235 y=366
            Label lMAttack = new Label(heroCard.getMagicalDamage() + "");
            lMAttack.setFont(font);
            lMAttack.setLayoutX(200);
            lMAttack.setLayoutY(200);
            lMAttack.setTextFill(Color.web("#FFFFFF"));

            //MShield x=238 y=442
            Label lMDefence = new Label(heroCard.getMagicalBlock() + "");
            lMDefence.setFont(font);
            lMDefence.setLayoutX(200);
            lMDefence.setLayoutY(240);
            lMDefence.setTextFill(Color.web("#FFFFFF"));

            //Hart x=148, y=441
            Label lHeal = new Label(heroCard.getHealValue() + "");
            lHeal.setFont(font);
            lHeal.setLayoutX(145);
            lHeal.setLayoutY(220);
            lHeal.setTextFill(Color.web("#000000"));

            pane.getChildren().addAll(lName, lPAttack, lPDefence, lMAttack, lMDefence, lHeal);
        }

        pane.setMinWidth(300);
        if (pane.getOnMouseClicked() == null) {
            pane.setPickOnBounds(true);
            pane.setOnMouseClicked(handler);
        }

        pane.setOnMouseExited((event) -> {
            Logger.getLogger(CardControl.class.getName()).log(Level.INFO, "OnMouseExited", event);
            grImg.setEffect(null);
        });

        StackPane root = new StackPane();
        root.getChildren().add(grImg);
        root.getChildren().add(pane);
        this.pane = root;

        return root;
    }

    /**
     * Method that sets the health of the minion.
     *
     * @param hp, the current health of the minion.
     */
    public void setHealth(int hp) {
        lHealth.setText(hp + "");
    }

    public void setTarget(int target) {
        System.out.println("setTarget: " + target);
        Platform.runLater(() -> {
            String stringTarget = "";
            if (target == 1) {
                stringTarget = "Target an enemy";
            } else if (target == 2) {
                stringTarget = "Target an enemy";
            } else if (target == 3) {
                stringTarget = "Left minion targeted";
            } else if (target == 4) {
                stringTarget = "Right minion targeted";
            } else if (target == 5) {
                stringTarget = "Enemy hero targeted";
            }
            lTarget.setText(stringTarget);
        });
    }
}
