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
import javafx.scene.Node;
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
public class MatchController implements Initializable {

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

        if (!client.login(loginHash)) {
            System.out.println("Error can't login!");
            //todo error logging in, show and back to main
        }
        System.out.println("OK!");
        cardChoice = new ArrayList<>();
        yourMinions = new ArrayList<>();
        opponentsMinions = new ArrayList<>();
        hero1 = new HeroControl(50, Game.getInstance().getPlayer());//todo own settings

        gridYourSide.add(hero1.getHeroControl(), 0, 0);
        btSendMessage.setOnAction((evt) -> {
            TextInputDialog dialog = new TextInputDialog("message");
            dialog.setTitle("New message");
            dialog.setHeaderText("Send message to opponend");
            dialog.setContentText("Enter a message to send to the opponend");

            dialog.showAndWait().ifPresent((message) -> client.sendMessage(message));

        });
    }

    /**
     * Method that adds an opponent to the match.
     *
     * @param name, the name of the opponent.
     * @param iconId, the icon ID of the opponent.
     */
    public void setOpponent(String name, int iconId) {
        LOG.info("Start match: " + name + " icon: " + iconId);
        hero2 = new HeroControl(50, new PlayerShared(2, name, iconId, 1, 1, 1, 1));
        Platform.runLater(() -> gridOpponentSide.add(hero2.getHeroControl(), 5, 0));
    }

    /**
     * Method that processes the turn with the card played.
     *
     * @param cardId, The id of the card played.
     */
    public void turnEnd(int cardId) {
        cardID = cardId;
        Optional<Card> card;
        card = allCards.stream().filter((c) -> c.getId() == cardID).findFirst();
        if (!card.isPresent()) {
            //todo error
        }
        CardControl cc = new CardControl(card.get());

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
            Platform.runLater(() -> gridOpponentSide.add(cc.CardPane(), 4, 0));
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
                if (minion1 != null) {
                    Platform.runLater(() -> gridYourSide.getChildren().remove(2, 2));
                    yourMinions.remove(minion1);
                }
                minion1 = card;
                minion1.setEventHandler((event) -> attackTarget(1));
                yourMinions.add(card);
                Node minionNode1 = (Node) minion1.CardPane();
                minionNode1.toBack();
                Platform.runLater(() -> gridYourSide.add(minionNode1, 2, 0));
                break;
            case 2:
                if (minion2 != null) {
                    Platform.runLater(() -> gridYourSide.getChildren().remove(4, 4));
                    yourMinions.remove(minion2);
                }
                minion2 = card;
                minion2.setEventHandler((event) -> attackTarget(2));
                yourMinions.add(card);
                Node minionNode2 = (Node) minion2.CardPane();
                minionNode2.toBack();
                Platform.runLater(() -> gridYourSide.add(minionNode2, 4, 0));
                break;
            case 3:
                if (minion3 != null) {
                    Platform.runLater(() -> gridOpponentSide.getChildren().remove(0, 0));
                    opponentsMinions.remove(minion3);
                }
                minion3 = card;
                minion3.setEventHandler((event) -> attackTarget(3));
                opponentsMinions.add(card);
                Node minionNode3 = (Node) minion3.CardPane();
                minionNode3.toBack();
                Platform.runLater(() -> gridOpponentSide.add(minionNode3, 0, 0));
                break;
            case 4:
                if (minion4 != null) {
                    Platform.runLater(() -> gridOpponentSide.getChildren().remove(2, 2));
                    opponentsMinions.remove(minion4);
                }
                minion4 = card;
                minion4.setEventHandler((event) -> attackTarget(4));
                opponentsMinions.add(card);
                Node minionNode4 = (Node) minion4.CardPane();
                minionNode4.toBack();
                Platform.runLater(() -> gridOpponentSide.add(minionNode4, 2, 0));
                break;
            default:
                //todo error
                break;
        }
        //placeMinionCards();
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
                 if(health<1){
                        
                        Platform.runLater(()->{gridYourSide.getChildren().remove(minion1.oldCardPane());minion1 = null;});
                        yourMinions.remove(minion1);
                        if(minion2!=null){
                            
                            Platform.runLater(()->{
                                minion1=minion2;
                                gridYourSide.getChildren().remove(minion2.oldCardPane());
                                minion2=null;
                                gridYourSide.add(minion1.oldCardPane(), 2, 0);
                            });
                        }
                        
                    }else{
                        Platform.runLater(() -> minion1.setHealth(health));
                    }
                } else {
                    if(health<1){
                        Platform.runLater(()->{gridYourSide.getChildren().remove(minion2.oldCardPane());minion2=null;});
                        yourMinions.remove(minion2);
                    }else{
                        Platform.runLater(() -> minion2.setHealth(health));
                    }
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
                 if(health <1){
                        Platform.runLater(()->{gridOpponentSide.getChildren().remove(minion3.oldCardPane());minion3=null;});
                        opponentsMinions.remove(minion3);
                        if(minion4!=null){
                            
                            Platform.runLater(()->{
                                minion3=minion4;
                                gridOpponentSide.getChildren().remove(minion3.oldCardPane());
                                minion4=null;
                                gridOpponentSide.add(minion3.oldCardPane(), 0, 0);
                            });
                        }
                    }else{
                        Platform.runLater(() -> minion3.setHealth(health));
                    }
                } else {
                    if(health <1){
                        Platform.runLater(()->{gridOpponentSide.getChildren().remove(minion4.oldCardPane());minion4=null;});
                        opponentsMinions.remove(minion4);
                    }else{
                        Platform.runLater(() -> minion4.setHealth(health));
                    }
                }
            }
        }
    }

    /**
     * Method that prepares three cards for the player to choose from.
     *
     * @param card1, first card to be picked from.
     * @param card2, second card to be picked from.
     * @param card3, third card to be picked from.
     */
    public void newTurn(int card1, int card2, int card3) {
        LOG.info("New cards " + card1 + " " + card2 + " " + card3 + " ");
        cardChoice.clear();
        Optional<Card> cardO1, cardO2, cardO3;
        cardO1 = allCards.stream().filter((c) -> c.getId() == card1).findFirst();
        cardO2 = allCards.stream().filter((c) -> c.getId() == card2).findFirst();
        cardO3 = allCards.stream().filter((c) -> c.getId() == card3).findFirst();

        if (!cardO1.isPresent() || !cardO2.isPresent() || !cardO3.isPresent()) {
            //groot probleem!
            System.exit(error);
        }

        cardChoice.add(new CardControl(cardO1.get()));
        cardChoice.add(new CardControl(cardO2.get()));
        cardChoice.add(new CardControl(cardO3.get()));

        Platform.runLater(() -> drawCards());
    }

    /**
     * Method that adds EventHandlers to the cards and adds them to the pane.
     */
    private void drawCards() {
        for (int i = 0; i < 3; i++) {
            CardControl cardControl = cardChoice.get(i);
            cardControl.setEventHandler(openCard(cardControl));
            gridCardHolder.add(cardControl.CardPane(), i, 0);
        }
    }

    public void receiveMessage(String message) {
        LOG.log(Level.INFO, "Received Message: {0}", message);
        Platform.runLater(() -> {
            StageController.getInstance().popup("You received a message from your opponent.", false, hero1.getName() + ": " + message);
        });
    }

    /**
     * Method that is called when the player won. This will show a pop-up to
     * tell the player he won.
     */
    public void win() {
        LOG.log(Level.INFO, "victory");
        Platform.runLater(() -> {
            StageController.getInstance().popup("Whoho!", false, "You have won!");
        });
        StageController.getInstance().navigate("MainScreenFXML.fxml", "Mighty Duels");
        client.nonFatalDisconnect();
    }

    /**
     * Method that is called when the player lost. This will show a pop-up to
     * tell the player he lost.
     */
    public void lose() {
        LOG.log(Level.INFO, "Defeat");
        Platform.runLater(() -> {
            StageController.getInstance().popup("Awh!", false, "You have lost!");
        });
        StageController.getInstance().navigate("MainScreenFXML.fxml", "Mighty Duels");
        client.nonFatalDisconnect();
    }

    /**
     * Method that is called when the player tied. This will show a pop-up to
     * tell the player he tied.
     */
    public void tie() {
        LOG.log(Level.INFO, "Tie");
        Platform.runLater(() -> {
            StageController.getInstance().popup("Hey!", false, "You played Tie!");
        });
        StageController.getInstance().navigate("MainScreenFXML.fxml", "Mighty Duels");
        client.nonFatalDisconnect();
    }
    
    public void selectHero() {
        LOG.log(Level.INFO, "Hero Selected");
        attackTarget(5);
    }

    @FXML
    private void attackTarget(int id) {
        switch (id) {
            case 1:
                ownMinion = 1;
                break;
            case 2:
                ownMinion = 2;
                break;
            case 3:
                if (ownMinion != -1) {
                    opponentMinion = 1;
                    client.setTarget(ownMinion, opponentMinion);
                    ownMinion = -1;
                    opponentMinion = -1;
                }
                break;
            case 4:
                if (ownMinion != -1) {
                    opponentMinion = 2;
                    client.setTarget(ownMinion, opponentMinion);
                    ownMinion = -1;
                    opponentMinion = -1;
                }
                break;
            case 5:
                if (ownMinion != -1) {
                    opponentMinion = 0;
                    client.setTarget(ownMinion, opponentMinion);
                    ownMinion = -1;
                    opponentMinion = -1;
                }
                break;
        }

    }

    private EventHandler openCard(CardControl cardControl) {
        EventHandler handler = new EventHandler() {
            @Override
            public void handle(Event event) {
                for (int i = 0; i < 3; i++) {
                    gridCardHolder.getChildren().clear();
                    CardControl cardControl = cardChoice.get(i);
                    cardControl.setEventHandler(pickCard(cardControl));
                    gridChooseCard.add(cardControl.CardPane(), i, 0);
                }
            }
        };
        return handler;
    }

    private EventHandler pickCard(CardControl cardControl) {
        EventHandler handler = new EventHandler() {

            @Override
            public void handle(Event event) {

//                Platform.runLater(() -> gridYourSide.getChildren().remove(1, 2));
//                Platform.runLater(() -> gridOpponentSide.getChildren().remove(4, 5));

                System.out.println(cardControl.getCard().getName());
                try {
                    client.setCard(cardControl.getCard().getId());
                } catch (Exception ex) {
                    Logger.getLogger(MatchController.class.getName()).log(Level.SEVERE, null, ex);
                    //todo fatal?
                }
                gridChooseCard.getChildren().clear();
                if (cardControl.getCard() instanceof HeroCard) {
                    myHeroCard = (HeroCard) cardControl.getCard();

                    cardControl.setEventHandler(null);
                    gridYourSide.add(cardControl.CardPane(), 1, 0);
                } else if (cardControl.getCard() instanceof MinionCard) {
                    if (yourMinions.size() < 2) {
                        gridYourSide.add(cardControl.CardPane(), 1, 0);
                        //placeMinionCards();
                    }
                }
                //drawCards();
            }
        };
        return handler;
    }

//    private void placeMinionCards() {
//        Platform.runLater(() -> {
//            gridYourSide.getChildren().removeAll(yourMinions);
//            gridOpponentSide.getChildren().removeAll(opponentsMinions);
//
//            for (int i = 0; i < yourMinions.size(); i++) {
//                Node minionNode = (Node)yourMinions.get(i).CardPane();
//                minionNode.toBack();
//                gridYourSide.add(minionNode, 2 + (i * 2), 0);
//            }
//
//            for (int i = 0; i < opponentsMinions.size(); i++) {
//                Node minionNode = (Node)opponentsMinions.get(i).CardPane();
//                minionNode.toBack();
//                gridOpponentSide.add(minionNode, (i * 2), 0);
//            }
//        });
//    }
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
            client.concede();
        } else if (result.get() == buttonTypeTwo) {
            alert.close();
        } else {
            alert.close();
        }
    }

    //TODO button functionality
    private void initializeButtons() {
        btnEndTurn.setOnMouseClicked((MouseEvent event) -> {
            client.setFinished(true);
            SoundController.play(SoundController.SoundFile.BUTTONPRESS);
        });

        btnConcede.setOnMouseClicked((MouseEvent event) -> {
            Mbox("Concede", "Concede screen", "Are you sure you wish to concede?");
        });

        lblDamageVisualisation.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Optional<Card> card;
                card = allCards.stream().filter((c) -> c.getId() == cardID).findFirst();
                if (card.isPresent()) {
                    CardControl cc = new CardControl(card.get());
                    if (cc.getCard() instanceof HeroCard) {
                        HeroCard enemyCard = (HeroCard) cc.getCard();

                        StageController.getInstance().dmgPopup(myHeroCard.getPhysicalDamage() + "", myHeroCard.getPhysicalBlock() + "", myHeroCard.getMagicalDamage() + "", myHeroCard.getMagicalBlock() + "", myHeroCard.getHealValue() + "", hero1Hp + "", enemyCard.getPhysicalDamage() + "", enemyCard.getPhysicalBlock() + "", enemyCard.getMagicalDamage() + "", enemyCard.getMagicalBlock() + "", enemyCard.getHealValue() + "", hero2Hp + "");
                    }
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
