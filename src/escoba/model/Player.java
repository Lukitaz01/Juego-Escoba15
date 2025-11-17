package escoba.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the Escoba game.
 * Manages hand, captured cards, and escobas count.
 */
public class Player {
    private String name;
    private List<Card> hand;
    private List<Card> capturedCards;
    private int escobasCount;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.capturedCards = new ArrayList<>();
        this.escobasCount = 0;
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public List<Card> getCapturedCards() {
        return capturedCards;
    }

    public int getEscobasCount() {
        return escobasCount;
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public Card removeCardFromHand(int index) {
        return hand.remove(index);
    }

    public void addCapturedCard(Card card) {
        capturedCards.add(card);
    }

    public void addCapturedCards(List<Card> cards) {
        capturedCards.addAll(cards);
    }

    public void incrementEscobas() {
        escobasCount++;
    }

    public boolean hasCardsInHand() {
        return !hand.isEmpty();
    }

    public int getHandSize() {
        return hand.size();
    }

    public int getCapturedCount() {
        return capturedCards.size();
    }
}
