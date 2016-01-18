/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.GUI;

import Client.Controller.SoundController;
import Client.Controller.StageController;
import Client.Domain.Game;
import Client.SocketManagerClient.SocketManager;
import Shared.Domain.Card;
import Shared.Domain.MinionCard;
import Shared.Domain.HeroCard;
import Shared.Domain.PlayerShared;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author Matthijs
 */
public class SpectateController implements Initializable {

    private static final int error = -1000000000;

    private int ownMinion = -1;
    private int opponentMinion = -1;
    private static byte[] loginHash;
    private static final Logger LOG = Logger.getLogger(MatchController.class.getName());

    /**
     * Method that sets the hash of the match.
     *
     * @param hash, the hash that identifies the match.
     */
    public static void setHash(byte[] hash) {
        loginHash = hash;
    }

    @FXML
    private GridPane gridCardHolder;
    @FXML
    private GridPane gridChooseCard;
    @FXML
    private GridPane gridYourSide;
    @FXML
    private GridPane gridOpponentSide;
    @FXML
    private ImageView btnEndTurn;
    @FXML
    private ImageView btnConcede;
    @FXML
    private Label lblDamageVisualisation;
    @FXML
    private Button btSendMessage;

    private CardControl minion1;
    private CardControl minion2;
    private CardControl minion3;
    private CardControl minion4;
    private HeroCard heroCard;

    private HeroControl hero1;
    private HeroControl hero2;

    private ArrayList<CardControl> cardChoice;
    private ArrayList<CardControl> yourMinions;
    private ArrayList<CardControl> opponentsMinions;

    private SocketManager client;

    private int cardID;
    private int hero1Hp, hero2Hp;
    private HeroCard myHeroCard;

    private List<Card> allCards;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        allCards = Game.getInstance().getCards();

        client = new SocketManager(this);
        initializeButtons();

        System.out.println("Connecting to LocalHost");

        try {
            client.Connect(System.getProperty("MightyDuels.SocketServer"));
        } catch (IOException ex) {
            Logger.getLogger(MatchController.class.getName()).log(Level.SEVERE, null, ex);

            //todo back to main screen and error message
        }

        System.out.println("Identifying");

        client.connect();

        System.out.println("Sending Hash...");

        if (!client.loginSpectate(loginHash)) {
            System.out.println("Error can't login!");
            //todo error logging in, show and back to main
        }
        System.out.println("OK!");
        cardChoice = new ArrayList<>();
        yourMinions = new ArrayList<>();
        opponentsMinions = new ArrayList<>();
        
    }
    
    public void spectateNewMatch(String player1, String player2, int icon1, int icon2){
        LOG.log(Level.INFO, "Start match between {0} and {1}", new Object[]{player1, player2});
        hero1 = new HeroControl(50, new PlayerShared(2, player1, icon1, 1, 1, 1, 1));
        Platform.runLater(()->gridYourSide.add(hero1.getHeroControl(), 0, 0));
        hero2 = new HeroControl(50, new PlayerShared(2, player2, icon2, 1, 1, 1, 1));
        Platform.runLater(() -> gridOpponentSide.add(hero2.getHeroControl(), 5, 0));
    }

    /**
     * Method that processes the turn with the card played.
     *
     * @param cardId, The id of the card played.
     */
    public void turnEnd(int cardId1, int cardId2) {
        Optional<Card> card;
        card = allCards.stream().filter((c) -> c.getId() == cardId1).findFirst();
        if (!card.isPresent()) {
            //todo error
            return;
        }
        Optional<Card> card2;
        card2 = allCards.stream().filter((c) -> c.getId() == cardId2).findFirst();
        if (!card2.isPresent()) {
            //todo error
            return;
        }
        CardControl cc = new CardControl(card.get());
        CardControl cc2 = new CardControl(card2.get());

        if (cc.getCard() instanceof HeroCard) {
            HeroCard playerCard = (HeroCard) cc.getCard();

            int maxValue = Math.max(Math.max(Math.max(Math.max(playerCard.getPhysicalDamage(), playerCard.getPhysicalBlock()), playerCard.getMagicalDamage()), playerCard.getMagicalBlock()), playerCard.getHealValue());

            if (maxValue == playerCard.getPhysicalDamage()) {
                SoundController.play(SoundController.SoundFile.PHYSICALATTACK);
            } else if (maxValue == playerCard.getMagicalBlock()) {
                SoundController.play(SoundController.SoundFile.PHYSICALBLOCK);
            } else if (maxValue == playerCard.getMagicalDamage()) {
                SoundController.play(SoundController.SoundFile.MAGICALATTACK);
            } else if (maxValue == playerCard.getMagicalBlock()) {
                SoundController.play(SoundController.SoundFile.MAGICALBLOCK);
            } else if (maxValue == playerCard.getHealValue()) {
                SoundController.play(SoundController.SoundFile.HEAL);
            }
            Platform.runLater(() -> gridYourSide.add(cc.CardPane(), 1, 0));
        }
        if (cc2.getCard() instanceof HeroCard) {
            HeroCard playerCard = (HeroCard) cc2.getCard();

            int maxValue = Math.max(Math.max(Math.max(Math.max(playerCard.getPhysicalDamage(), playerCard.getPhysicalBlock()), playerCard.getMagicalDamage()), playerCard.getMagicalBlock()), playerCard.getHealValue());

            if (maxValue == playerCard.getPhysicalDamage()) {
                SoundController.play(SoundController.SoundFile.PHYSICALATTACK);
            } else if (maxValue == playerCard.getMagicalBlock()) {
                SoundController.play(SoundController.SoundFile.PHYSICALBLOCK);
            } else if (maxValue == playerCard.getMagicalDamage()) {
                SoundController.play(SoundController.SoundFile.MAGICALATTACK);
            } else if (maxValue == playerCard.getMagicalBlock()) {
                SoundController.play(SoundController.SoundFile.MAGICALBLOCK);
            } else if (maxValue == playerCard.getHealValue()) {
                SoundController.play(SoundController.SoundFile.HEAL);
            }
            Platform.runLater(() -> gridOpponentSide.add(cc2.CardPane(), 4, 0));
        }
    }

    /**
     * Method for adding a minion to the board.
     *
     * @param id, The ID of the minion.
     * @param boardId, the ID of the side of the board.
     */
    public void addMinion(int id, int boardId) {
        Optional<Card> crd = allCards.stream().filter((c) -> c.getId() == id).findFirst();
        if (!crd.isPresent()) {
            LOG.warning(id + " was not found!");
            return;
        }
        if (!(crd.get() instanceof MinionCard)) {
            LOG.warning(id + " is not an minion card!");
            return;
        }
        CardControl card = new CardControl(crd.get());
        switch (boardId) {
            case 1:
                minion1 = card;
                yourMinions.add(card);
                break;
            case 2:
                minion2 = card;
                yourMinions.add(card);
                break;
            case 3:
                minion3 = card;
                opponentsMinions.add(card);
                break;
            case 4:
                minion4 = card;
                opponentsMinions.add(card);
                break;
            default:
                //todo error
                break;
        }
        placeMinionCards();
    }

    /**
     * Method that sets the health of a hero or a minion.
     *
     * @param self, determines if the one that the health has been set is either
     * your own(true) or your opponents(false).
     * @param hero, if it is a hero that is healed this will be true.
     * @param id, the id of the minion of which the health will be set.
     * @param health, the health value it will be changed to.
     */
    public void setHealth(boolean self, boolean hero, int id, int health) {
        if (self) {
            if (hero) {
                Platform.runLater(() -> {
                    hero1.setHealth(health);
                    hero1Hp = health;
                });

            } else {
                if (id == 1) {
                    Platform.runLater(() -> minion1.setHealth(health));
                } else {
                    Platform.runLater(() -> minion2.setHealth(health));
                }
            }
        } else {
            if (hero) {
                Platform.runLater(() -> {
                    hero2.setHealth(health);
                    hero2Hp = health;
                });
            } else {
                if (id == 1) {
                    Platform.runLater(() -> minion3.setHealth(health));
                } else {
                    Platform.runLater(() -> minion4.setHealth(health));
                }
            }
        }
    }

//    /**
//     * Method that prepares three cards for the player to choose from.
//     *
//     * @param card1, first card to be picked from.
//     * @param card2, second card to be picked from.
//     * @param card3, third card to be picked from.
//     */
//    public void newTurn(int card1, int card2, int card3) {
//        cardChoice.clear();
//    }

    public void receiveMessage(String message) {
        LOG.log(Level.INFO, "Received Message: {0}", message);
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Received an message");
            alert.setHeaderText("You Received an message from opponend");
            alert.setContentText(message);

            ButtonType type = new ButtonType("Ok");
            alert.getButtonTypes().setAll(type);

            alert.show();
        });
    }

    /**
     * Method that is called when the player won. This will show a pop-up to
     * tell the player he won.
     */
    public void win() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("You Won!");
        alert.setHeaderText("Whoho!");
        alert.setContentText("You have won!");

        ButtonType buttonTypeOne = new ButtonType("OK");

        alert.getButtonTypes().setAll(buttonTypeOne);

        Optional<ButtonType> result = alert.showAndWait();

        StageController.getInstance().navigate("MainScreenFXML.fxml", "Mighty Duels");
        client.nonFatalDisconnect();

        //todo
        System.out.println("YAY");
    }

    /**
     * Method that is called when the player lost. This will show a pop-up to
     * tell the player he lost.
     */
    public void lose() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("You Lost!");
        alert.setHeaderText("Awh!");
        alert.setContentText("You have lost!");

        ButtonType buttonTypeOne = new ButtonType("OK");

        alert.getButtonTypes().setAll(buttonTypeOne);

        Optional<ButtonType> result = alert.showAndWait();

        StageController.getInstance().navigate("MainScreenFXML.fxml", "Mighty Duels");
        client.nonFatalDisconnect();
        System.out.println("BOE!");
    }

    /**
     * Method that is called when the player tied. This will show a pop-up to
     * tell the player he tied.
     */
    public void tie() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("You played Tie!");
        alert.setHeaderText("Whoho!");
        alert.setContentText("You have played tie!");

        ButtonType buttonTypeOne = new ButtonType("OK");

        alert.getButtonTypes().setAll(buttonTypeOne);

        Optional<ButtonType> result = alert.showAndWait();

        StageController.getInstance().navigate("MainScreenFXML.fxml", "Mighty Duels");
        client.nonFatalDisconnect();
        System.out.println("Meh");
    }

    private void placeMinionCards() {
        gridYourSide.getChildren().removeAll(yourMinions);
        gridOpponentSide.getChildren().removeAll(opponentsMinions);

        for (int i = 0; i < yourMinions.size(); i++) {
            gridYourSide.add(yourMinions.get(i).CardPane(), 2 + (i * 2), 0);
        }

        for (int i = 0; i < opponentsMinions.size(); i++) {
            gridOpponentSide.add(opponentsMinions.get(i).CardPane(), 0 + (i * 2), 0);
        }
    }

    private void Mbox(String title, String header, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        ButtonType buttonTypeOne = new ButtonType("Yes");
        ButtonType buttonTypeTwo = new ButtonType("No");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            SoundController.play(SoundController.SoundFile.BUTTONPRESS);

            String titlescreen = "Mighty Duels";
            String root = "MainScreenFXML.fxml";
            StageController.getInstance().navigate(root, titlescreen);
        } else if (result.get() == buttonTypeTwo) {
            alert.close();
        } else {
            alert.close();
        }
    }

    //TODO button functionality
    private void initializeButtons() {
        btnConcede.setOnMouseClicked((MouseEvent event) -> {
            Mbox("Main Menu", "Menu screen", "Are you sure you wish to go to main menu?");
        });

        lblDamageVisualisation.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Optional<Card> card;
                card = allCards.stream().filter((c) -> c.getId() == cardID).findFirst();
                if (card.isPresent()) {
                    CardControl cc = new CardControl(card.get());
                    HeroCard enemyCard = (HeroCard) cc.getCard();

                    StageController.getInstance().dmgPopup(myHeroCard.getPhysicalDamage() + "", myHeroCard.getPhysicalBlock() + "", myHeroCard.getMagicalDamage() + "", myHeroCard.getMagicalBlock() + "", myHeroCard.getHealValue() + "", hero1Hp + "", enemyCard.getPhysicalDamage() + "", enemyCard.getPhysicalBlock() + "", enemyCard.getMagicalDamage() + "", enemyCard.getMagicalBlock() + "", enemyCard.getHealValue() + "", hero2Hp + "");
                }
            }
        });

        lblDamageVisualisation.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Platform.runLater(() -> {
                    StageController.getInstance().closePopUp();
                });
            }
        });
    }
}
