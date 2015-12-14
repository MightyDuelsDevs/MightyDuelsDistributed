/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.GUI;

import Client.Controller.SoundController;
import Client.Controller.StageController;
import Shared.Domain.Card;
import Shared.Domain.MinionCard;
import Shared.Domain.HeroCard;
import Shared.Domain.PlayerShared;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
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
    
    private MinionCard minion1;
    private MinionCard minion2;
    private MinionCard minion3;
    private MinionCard minion4;
    private HeroCard heroCard;
    
    private HeroControl hero1;
    private HeroControl hero2;
    
    private ArrayList<CardControl> cardChoice;
    private ArrayList<CardControl> yourMinions;
    private ArrayList<CardControl> opponentsMinions;
    
    private Timer timer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeButtons();
        
        this.timer = new Timer();
        
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                //
            }
        }, 1*1000, 1*1000);
        
        cardChoice = new ArrayList<>();
        yourMinions = new ArrayList<>();
        opponentsMinions = new ArrayList<>();
        hero1 = new HeroControl(50, new PlayerShared(1, "Loek", 1, 1, 1, 1, 1));
        hero2 = new HeroControl(50, new PlayerShared(2, "Rick", 1, 1, 1, 1, 1));
        gridYourSide.add(hero1.getHeroControl(), 0, 0);
        //gridOpponentSide.add(hero2.getHeroControl(), 5, 0);
        
        minion1 = new MinionCard(1, "test1", "", "", 1, 0, 10);
        minion2 = new MinionCard(2, "test2", "", "", 0, 1, 10);
        minion3 = new MinionCard(3, "test3", "", "", 1, 0, 10);
        minion4 = new MinionCard(4, "test4", "", "", 0, 1, 10);
        heroCard = new HeroCard(1, "spell", "", "", 1, 1, 1, 1, 0);
        
        cardChoice.add(new CardControl(minion1));
        cardChoice.add(new CardControl(minion2));
        cardChoice.add(new CardControl(minion3));
        cardChoice.add(new CardControl(minion4));
        cardChoice.add(new CardControl(heroCard));
        
        cardChoice.get(0).setEventHandler(pickCard(cardChoice.get(0)));
        cardChoice.get(1).setEventHandler(pickCard(cardChoice.get(1)));
        cardChoice.get(2).setEventHandler(pickCard(cardChoice.get(2)));
        cardChoice.get(3).setEventHandler(pickCard(cardChoice.get(3)));
        
        yourMinions.add(cardChoice.get(0));
        yourMinions.add(cardChoice.get(1));
        opponentsMinions.add(cardChoice.get(2));
        opponentsMinions.add(cardChoice.get(3));
        
        placeMinionCards();        
        
        opponentsMinions.set(1, new CardControl(new MinionCard(3, "veranderd", "", "", 1, 1, 8)));
        placeMinionCards();
        //drawCards();
    };
    
    private void drawCards(){
        
        for(int i = 0 ; i < 3 ; i++){
            CardControl cardControl = cardChoice.get(i);
            
            cardControl.setEventHandler(pickCard(cardControl));
            
            gridChooseCard.add(cardControl.CardPane(),i,0);
        }
    }
    
    private EventHandler pickCard(CardControl cardControl){
        EventHandler handler = new EventHandler() {

            @Override
            public void handle(Event event) {  
                System.out.println(cardControl.getCard().getName());
                if(cardControl.getCard() instanceof HeroCard){
                    
                    gridChooseCard.getChildren().clear();
                    gridYourSide.add(cardControl.CardPane(), 1, 0);
                }
                else if(cardControl.getCard() instanceof MinionCard){
                    if(yourMinions.size() < 2){
                        
                        gridChooseCard.getChildren().clear();
                        yourMinions.add(cardControl);
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
        
        gridOpponentSide.getChildren().clear();
        for(int i = 0; i < opponentsMinions.size(); i++){
            gridOpponentSide.add(opponentsMinions.get(i).CardPane(), 0 + (i*2), 0);
        }
    }
    
    //TODO button functionality
    private void initializeButtons(){
        btnEndTurn.setOnMouseClicked((MouseEvent event) -> {
            //Todo  implement end turn button
            SoundController.play(SoundController.SoundFile.BUTTONPRESS);
            System.out.println("1");
        });
        
        btnConcede.setOnMouseClicked((MouseEvent event) -> {
            //Todo  implement concede button
            SoundController.play(SoundController.SoundFile.BUTTONPRESS);
            System.out.println("2");
        });
        
        lblDamageVisualisation.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                StageController.getInstance().dmgPopup("Damage Visualisation", true, "-", "-");
            }
        });
    }
}
