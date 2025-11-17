package escoba.game;

import escoba.model.Card;
import escoba.model.Deck;
import escoba.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the game state for Escoba de 15.
 * Handles deck, table, players, and game flow.
 */
public class GameState {
    private Deck deck;
    private List<Card> table;
    private Player player1;
    private Player player2;
    private int currentPlayerNumber; // 1 or 2
    private boolean gameOver;

    public GameState() {
        this.deck = new Deck();
        this.table = new ArrayList<>();
        this.player1 = new Player("Player 1");
        this.player2 = new Player("Player 2");
        this.currentPlayerNumber = 1;
        this.gameOver = false;
    }

    public void startNewGame() {
        deck = new Deck();
        deck.shuffle();
        table.clear();

        player1 = new Player("Player 1");
        player2 = new Player("Player 2");

        currentPlayerNumber = 1;
        gameOver = false;

        // Deal 4 cards to table
        for (int i = 0; i < 4; i++) {
            Card card = deck.draw();
            if (card != null) {
                table.add(card);
            }
        }

        // Deal 3 cards to each player
        dealCardsToPlayers();
    }

    public void dealCardsToPlayers() {
        for (int i = 0; i < 3; i++) {
            Card card1 = deck.draw();
            if (card1 != null) player1.addCardToHand(card1);

            Card card2 = deck.draw();
            if (card2 != null) player2.addCardToHand(card2);
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayerNumber == 1 ? player1 : player2;
    }

    public Player getOtherPlayer() {
        return currentPlayerNumber == 1 ? player2 : player1;
    }

    public void switchTurn() {
        currentPlayerNumber = currentPlayerNumber == 1 ? 2 : 1;
    }

    public List<Card> getTable() {
        return table;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getCurrentPlayerNumber() {
        return currentPlayerNumber;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public int getDeckSize() {
        return deck.remainingCards();
    }

    public boolean isDeckEmpty() {
        return deck.isEmpty();
    }

    public void addCardToTable(Card card) {
        table.add(card);
    }

    public void removeCardsFromTable(List<Card> cards) {
        table.removeAll(cards);
    }

    public boolean isTableEmpty() {
        return table.isEmpty();
    }
}
