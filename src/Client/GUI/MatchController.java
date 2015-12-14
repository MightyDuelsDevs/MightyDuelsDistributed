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
import java.util.Timer;
import java.util.TimerTask;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author Matthijs
 */
public class MatchController implements Initializable {
    private static final int JE_MOEDER = -1000000000;
    

    private int ownMinion = -1;
    private int opponentMinion = -1;
    private static byte[] loginHash;
    private static final Logger LOG = Logger.getLogger(MatchController.class.getName());
    
    private static byte[] loginHash;

    public static void setHash(byte[] hash) {
        loginHash = hash;
    }

    @FXML
    private GridPane gridPlayedCards;
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
    private Timer timer;
    private int sec;
    
    private List<Card> allCards;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        allCards = Game.getInstance().getCards();
        
        client = new SocketManager(this);
        initializeButtons();

        System.out.println("Connecting to LocalHost");

        try {
            client.Connect("localhost");
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
        hero1 = new HeroControl(50, new PlayerShared(1, "Loek", 1, 1, 1, 1, 1));//todo own settings

        gridYourSide.add(hero1.getHeroControl(), 0, 0);

//        minion1 = new MinionCard(1, "test1", "", "", 1, 0, 10);
//        minion2 = new MinionCard(2, "test2", "", "", 0, 1, 10);
//        minion3 = new MinionCard(3, "test3", "", "", 1, 0, 10);
//        minion4 = new MinionCard(4, "test4", "", "", 0, 1, 10);
//        heroCard = new HeroCard(1, "spell", "", "", 1, 1, 1, 1, 0);
//        cardChoice.add(new CardControl(minion1));
//        cardChoice.add(new CardControl(minion2));
//        cardChoice.add(new CardControl(minion3));
//        cardChoice.add(new CardControl(minion4));
//        cardChoice.add(new CardControl(heroCard));
//        cardChoice.get(0).setEventHandler(pickCard(cardChoice.get(0)));
//        cardChoice.get(1).setEventHandler(pickCard(cardChoice.get(1)));
//        cardChoice.get(2).setEventHandler(pickCard(cardChoice.get(2)));
//        cardChoice.get(3).setEventHandler(pickCard(cardChoice.get(3)));
//        yourMinions.add(cardChoice.get(0));
//        yourMinions.add(cardChoice.get(1));
//        opponentsMinions.add(cardChoice.get(2));
//        opponentsMinions.add(cardChoice.get(3));
//        placeMinionCards();        
//        opponentsMinions.set(1, new CardControl(new MinionCard(3, "veranderd", "", "", 1, 1, 8)));
//        placeMinionCards();
        //drawCards();
    }

    ;
    
    public void searchingMatch() {
        this.timer = new Timer();
        sec = 10;
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                Platform.runLater(() -> {
                    StageController sc = StageController.getInstance();
                    if (sec == 10) {
                        sc.popup("Searching for a match", false, " next try in: " + sec + " seconds");
                        sec--;
                    } else if (sec > 1) {
                        sc.popup("Searching for a match", false, " next try in: " + sec + " seconds");
                        sec--;
                    } else if (sec == 1) {
                        sc.popup("Searching for a match", false, " next try in: " + sec + " second");
                        sec--;
                    } else if (sec == 0) {
                        sc.popup("Searching for a match", false, " next try in: now");
                        sec--;
                    } else if (sec < 0) {
                        sc.closePopUp();
                        sec = 10;
                        this.cancel();
                    }
                });
            }
        }, 0, 1 * 1000);
    }

    public void setOpponent(String name, int iconId) {
        LOG.info("Start match: " + name + " icon: " + iconId);
        hero2 = new HeroControl(50, new PlayerShared(2, name, iconId, 1, 1, 1, 1));
        Platform.runLater(() -> gridOpponentSide.add(hero2.getHeroControl(), 5, 0));
    }

    public void turnEnd(int cardId) {
        //getCard(cardId);
    }

    public void addMinion(int id, int boardId) {
        CardControl card = null;// = new CardControl(getCard(CardId));
        switch (boardId) {
            case 1:
                minion1 = card;
                Platform.runLater(() -> yourMinions.add(card));
                break;
            case 2:
                minion2 = card;
                Platform.runLater(() -> yourMinions.add(card));
                break;
            case 3:
                minion3 = card;
                Platform.runLater(() -> opponentsMinions.add(card));
                break;
            case 4:
                minion4 = card;
                Platform.runLater(() -> opponentsMinions.add(card));
                break;
            default:
                //todo error
                break;
        }
    }

    public void setHealth(boolean self, boolean hero, int id, int health) {
        if (self) {
            if (hero) {
                Platform.runLater(() -> hero1.setHealth(health));
            } else {
                if (id == 1) {
                    Platform.runLater(() -> minion1.setHealth(health));
                } else {
                    Platform.runLater(() -> minion2.setHealth(health));
                }
            }
        } else {
            if (hero) {
                Platform.runLater(() -> hero2.setHealth(health));
            } else {
                if (id == 1) {
                    Platform.runLater(() -> minion3.setHealth(health));
                } else {
                    Platform.runLater(() -> minion4.setHealth(health));
                }
            }
        }
    }

    public void newTurn(int card1, int card2, int card3) {
        LOG.info("New cards " + card1 + " "+ card2 + " "+ card3 + " ");
        Platform.runLater(()->cardChoice.clear());
        Optional<Card> cardO1,cardO2,cardO3;
        cardO1 = allCards.stream().filter((c)->c.getId()==card1).findFirst();
        cardO2 = allCards.stream().filter((c)->c.getId()==card2).findFirst();
        cardO3 = allCards.stream().filter((c)->c.getId()==card3).findFirst();
        
        if(!cardO1.isPresent() || !cardO2.isPresent() || !cardO3.isPresent()){
            //groot probleem!
            System.exit(JE_MOEDER);
        }
        
        cardChoice.add(new CardControl(cardO1.get()));
        cardChoice.add(new CardControl(cardO2.get()));
        cardChoice.add(new CardControl(cardO3.get()));
        
        Platform.runLater(()->drawCards());
    }

    private void drawCards() {

        for (int i = 0; i < 3; i++) {
            CardControl cardControl = cardChoice.get(i);

            cardControl.setEventHandler(pickCard(cardControl));

            gridChooseCard.add(cardControl.CardPane(), i, 0);
        }
    }

    public void win() {
        //todo
        System.out.println("YAY");
    }

    public void lose() {
        //todo
        System.out.println("BOE!");
    }

    public void tie() {
        //todo
        System.out.println("Meh");
    }

    @FXML
    private void attackTarget(ActionEvent event) throws IOException {
        Button x = (Button) event.getSource();
        String id = x.getId();
        switch (id) {
            case "btnYourSide1":
                ownMinion = 1;
                break;
            case "btnYourSide2":
                ownMinion = 2;
                break;
            case "btnOpponentSide1":
                if (ownMinion != -1) {
                    opponentMinion = 1;
                }
                break;
            case "btnOpponentSide2":
                if (ownMinion != -1) {
                    opponentMinion = 2;
                    client.setTarget(ownMinion, opponentMinion);
                    ownMinion = -1;
                    opponentMinion = -1;
                }
                break;
            case "btnHero":
                if (ownMinion != -1) {
                    opponentMinion = 0;
                    client.setTarget(ownMinion, opponentMinion);
                    ownMinion = -1;
                    opponentMinion = -1;
                }
                break;
        }

    }

    private EventHandler pickCard(CardControl cardControl) {
        EventHandler handler = new EventHandler() {

            @Override
            public void handle(Event event) {

                System.out.println(cardControl.getCard().getName());
                try {
                    client.setCard(cardControl.getCard().getId());
                } catch (Exception ex) {
                    Logger.getLogger(MatchController.class.getName()).log(Level.SEVERE, null, ex);
                    //todo fatal?
                }
                if (cardControl.getCard() instanceof HeroCard) {

                    gridChooseCard.getChildren().clear();
                    gridYourSide.add(cardControl.CardPane(), 1, 0);
                } else if (cardControl.getCard() instanceof MinionCard) {
                    if (yourMinions.size() < 2) {

                        gridChooseCard.getChildren().clear();
                        //yourMinions.add(cardControl);
                        placeMinionCards();
                    }
                }
                //drawCards();
            }
        };
        return handler;
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
                StageController.getInstance().dmgPopup("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
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
