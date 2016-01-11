package Shared.Domain;

import java.util.List;
import Shared.Domain.Card;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * An container for Cards methods to pull cards
 */
public class Deck implements Serializable {

    private String name;
    private final ArrayList<Card> cards;

    private int first;
    private int last;
    private int id;

    /**
     * Create a new instance of Deck with no cards
     */
    public Deck() {
        cards = new ArrayList<>();
    }

    /**
     * Create a new instance of Deck with a name
     *
     * @param name the name of the deck
     * @param id the id of the deck
     */
    public Deck(int id, String name) {
        this.id = id;
        this.name = name;
        cards = new ArrayList<>();
    }

    /**
     * Add an card to the deck
     *
     * @param card the card to add
     */
    public void addCard(Card card) {
        first = cards.indexOf(card);
        last = cards.lastIndexOf(card);
        if (first == last) {
            cards.add(card);
        }
    }

    /**
     * Remove a card from the deck
     *
     * @param card the card to remove
     */
    public void removeCard(Card card) {
        cards.remove(card);
    }

    /**
     * Set the name of the deck
     *
     * @param name the deck name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the id of the deck
     *
     * @return the id of the deck
     */
    public int getId() {
        return this.id;
    }

    /**
     * Get the name of the deck
     *
     * @return the name of the deck
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get all cards in the deck
     *
     * @return the arraylist with the cards
     */
    public ArrayList<Card> getCards() {
        return this.cards;
    }
}
