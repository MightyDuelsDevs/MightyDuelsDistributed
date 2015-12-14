/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.GUI;

import Client.Controller.SoundController;
import Client.Controller.StageController;
import Client.SocketManagerClient.SocketManager;
import Shared.Domain.Card;
import Shared.Domain.MinionCard;
import Shared.Domain.HeroCard;
import Shared.Domain.PlayerShared;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    @FXML private GridPane gridPlayedCards;
    @FXML private GridPane gridChooseCard;
    @FXML private GridPane gridYourSide;
    @FXML private GridPane gridOpponentSide;
    @FXML private ImageView btnEndTurn;
    @FXML private ImageView btnConcede;
    @FXML private Label lblDamageVisualisation;
    
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client = new SocketManager(this);
        initializeButtons();
        
        try {
            client.Connect("localhost");
        } catch (IOException ex) {
            Logger.getLogger(MatchController.class.getName()).log(Level.SEVERE, null, ex);
            
            //todo back to main screen and error message
        }
        
        client.connect();
        
        if(!client.login(new byte[0])){//todo login hash
            //todo error logging in, show and back to main
        }
        
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
    };
    
    public void setOpponent(String name, int iconId){
        hero2 = new HeroControl(50, new PlayerShared(2, name, iconId, 1, 1, 1, 1));
        gridOpponentSide.add(hero2.getHeroControl(), 5, 0);
    }
    
    public void turnEnd(int cardId){
        //getCard(cardId);
    }
    
    public void addMinion(int id, int boardId){
        CardControl card = null;// = new CardControl(getCard(CardId));
        switch(boardId){
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
    }
    
    public void setHealth(boolean self, boolean hero, int id, int health){
        if(self){
            if(hero){
                hero1.setHealth(health);
            }else{
                if(id == 1){
                    minion1.setHealth(health);
                }else{
                    minion2.setHealth(health);
                }
            }
        }else{
            if(hero){
                hero2.setHealth(health);
            }else{
                if(id == 1){
                    minion3.setHealth(health);
                }else{
                    minion4.setHealth(health);
                }
            }
        }
    }
    
    public void newTurn(int card1, int card2, int card3){
        cardChoice.clear();
        //cardChoice.add(new CardControl(getCard(card1)));
        //cardChoice.add(new CardControl(getCard(card2)));
        //cardChoice.add(new CardControl(getCard(card3)));
        
        drawCards();
    }
    
    private void drawCards(){
        
        for(int i = 0 ; i < 3 ; i++){
            CardControl cardControl = cardChoice.get(i);
            
            cardControl.setEventHandler(pickCard(cardControl));
            
            gridChooseCard.add(cardControl.CardPane(),i,0);
        }
    }
    
    public void win(){
        //todo
        System.out.println("YAY");
    }
    
    public void lose(){
        //todo
        System.out.println("BOE!");
    }
    
    public void tie(){
        //todo
        System.out.println("Meh");
    }
    
    private EventHandler pickCard(CardControl cardControl){
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
                if(cardControl.getCard() instanceof HeroCard){
                    
                    gridChooseCard.getChildren().clear();
                    gridYourSide.add(cardControl.CardPane(), 1, 0);
                }
                else if(cardControl.getCard() instanceof MinionCard){
                    if(yourMinions.size() < 2){
                        
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
    
    private void placeMinionCards(){
        gridYourSide.getChildren().removeAll(yourMinions);
        gridOpponentSide.getChildren().removeAll(opponentsMinions);
        
        for(int i = 0; i < yourMinions.size(); i++){
            gridYourSide.add(yourMinions.get(i).CardPane(), 2 + (i*2), 0);
        }
        
        for(int i = 0; i < opponentsMinions.size(); i++){
            gridOpponentSide.add(opponentsMinions.get(i).CardPane(), 0 + (i*2), 0);
        }
    }
    
    //TODO button functionality
    private void initializeButtons(){
        btnEndTurn.setOnMouseClicked((MouseEvent event) -> {
            client.setFinished(true);
            SoundController.play(SoundController.SoundFile.BUTTONPRESS);
        });
        
        btnConcede.setOnMouseClicked((MouseEvent event) -> {
            client.concede();
            SoundController.play(SoundController.SoundFile.BUTTONPRESS);
        });
        
        lblDamageVisualisation.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                StageController.getInstance().dmgPopup("Damage Visualisation", true, "-", "-");
            }
        });
    }
}
