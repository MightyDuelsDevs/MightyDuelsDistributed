/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.GUI;

import Client.Controller.StageController;
import Client.Domain.Game;
import Shared.Domain.Card;
import Client.Domain.GameState;
import Shared.Domain.HeroCard;
import Client.Domain.Match;
import Client.RMI.RMIClient;
import Client.SocketManagerClient.SocketManager;
import Shared.Domain.PlayerShared;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author Matthijs
 */
public class GUIMatchController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private GridPane gridPlayedCards;
    @FXML
    private GridPane gridChooseCard;
    @FXML
    private GridPane gridYourSide;
    @FXML
    private GridPane gridOpponentSide;
    @FXML
    private ImageView buttonEndTurn;
    @FXML
    private ImageView buttonConcede;

    private SocketManager sM = new SocketManager();

    private StageController sc;
    private PlayerShared loggedInPlayer = Game.getInstance().getPlayer();

    private List<HeroCard> cards;
    private List<HeroCardControl> heroCardControls;
    private boolean yourCardPlayed = false;
    private HeroControl yourHero;
    private HeroControl opponentsHero;

    private PlayerShared player_1;
    private PlayerShared player_2;

    private Match match;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        heroCardControls = new ArrayList<>();
        cards = new ArrayList<>();
        InitialiseHeroes();
        match = new Match(player_1, player_2.);

        
        for (Card card : RMIClient.getInstance().getCards()) {
            if (card instanceof HeroCard) {
                cards.add((HeroCard) card);
            }
        }

        drawCards();
    }

    public void InitialiseHeroes() {
        player_1 = loggedInPlayer;
        player_2 = new PlayerShared(2, player_1.getUsername() + "Clone".toUpperCase(), player_1.getIconId(), player_1.getRating(), player_1.getWins(), player_1.getLosses(), player_1.getMatches());// TODO

        Random random = new Random();

        yourHero = new HeroControl(50, player_1);
        gridYourSide.add(yourHero.getHeroControl(), 0, 0);

        opponentsHero = new HeroControl(50, player_2);
        gridOpponentSide.add(opponentsHero.getHeroControl(), 5, 0);

    }

    private void drawCards() {
        Random random = new Random();
        heroCardControls.clear();

        for (int i = 0; i < 3; i++) {
            int count = i;

            boolean added = false;

            while (!added) {
                HeroCardControl control = new HeroCardControl(cards.get(random.nextInt(cards.size())), 180);
                if (!heroCardControls.contains(control)) {
                    heroCardControls.add(control);
                    added = true;
                }
            }

            EventHandler handler = new EventHandler() {
                StackPane pane = heroCardControls.get(count).HeroCardControlPane();

                @FXML
                private void buttonConcede_OnClick(ActionEvent event) throws IOException {
                    sM.connect();
                    sM.concede();
                }

                @FXML
                private void buttonEndTurn_OnClick(ActionEvent event) throws IOException {
                    sM.connect();
                    sM.setFinished(Boolean.TRUE);
                }

                @Override
                public void handle(Event event) {
                    if (!yourCardPlayed) {
                        yourCardPlayed = true;

                        gridYourSide.add(pane, 1, 0);
                        gridYourSide.setAlignment(Pos.BASELINE_LEFT);
                        gridChooseCard.getChildren().clear();
                        match.getHero1().setCardPlayed((Card) heroCardControls.get(count).getHeroCard());
                        match.getHero1().setFinished(true);
                        drawCards();
                    } else if (yourCardPlayed) {

                        yourCardPlayed = false;

                        gridOpponentSide.add(pane, 4, 0);
                        boolean proceed = false;

                        if (gridOpponentSide.getChildren().contains(pane)) {
                            proceed = true;
                        }
                        gridChooseCard.getChildren().clear();

                        match.getHero2().setCardPlayed((Card) heroCardControls.get(count).getHeroCard());
                        match.getHero2().setFinished(true);

                        Thread t = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    match.startTurn();

                                    Platform.runLater(() -> {

                                        yourHero.setHealth(match.getHero1().getHitPoints());
                                        opponentsHero.setHealth(match.getHero2().getHitPoints());

                                    });
                                    sleep(2000);
                                    Platform.runLater(() -> {
                                        gridYourSide.getChildren().remove(1);
                                        gridOpponentSide.getChildren().remove(1);
                                        drawCards();
                                    });

                                    // Determine Gamestate
                                    if (match.getGameState() == GameState.Defined || match.getGameState() == GameState.Tie) {
                                        if (match.getHero1().getHitPoints() <= 0 && match.getHero2().getHitPoints() <= 0) {
                                            Platform.runLater(() -> {
                                                sc.popup("Tie", false, "It's a tie between " + player_1.getUsername() + " and " + player_2.getUsername() + ".");
                                                backToMainScreen();
                                            });

                                        } else if (match.getHero1().getHitPoints() <= 0) {
                                            Platform.runLater(() -> {
                                                sc.popup("Victory", false, player_1.getUsername() + " is victorious!");
                                                backToMainScreen();
                                            });
                                        } else if (match.getHero2().getHitPoints() <= 0) {
                                            Platform.runLater(() -> {
                                                sc.popup("Defeat", false, player_2.getUsername() + " has defeated you");
                                                backToMainScreen();
                                            });

                                        }
                                    }

                                } catch (InterruptedException ex) {
                                    Logger.getLogger(GUIMatchController.class
                                            .getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        };
                        t.start();
                    }
                }
            };
            heroCardControls.get(count).setEventHandler(handler);
            gridChooseCard.add(heroCardControls.get(i).HeroCardControlPane(), i, 0);
        }
    }

    private void backToMainScreen() {
        String title = "Mighty Duels";
        String root = "MainScreenFXML.fxml";
        sc.navigate(root, title);
    }
}