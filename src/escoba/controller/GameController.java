package escoba.controller;

import escoba.game.GameState;
import escoba.game.ScoreCalculator;
import escoba.model.Card;
import escoba.model.Player;
import escoba.view.PlayerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Controls the game flow and logic for Escoba de 15.
 * Handles player actions, validates moves, and updates views.
 */
public class GameController {
    private GameState gameState;
    private PlayerView view1;
    private PlayerView view2;

    public GameController(GameState gameState, PlayerView view1, PlayerView view2) {
        this.gameState = gameState;
        this.view1 = view1;
        this.view2 = view2;
    }

    public void startGame() {
        gameState.startNewGame();
        updateBothViews();
    }

    public void processPlayerInput(int playerNumber, String input) {
        if (gameState.isGameOver()) {
            return;
        }

        if (playerNumber != gameState.getCurrentPlayerNumber()) {
            getView(playerNumber).displayError("¡No es tu turno!");
            return;
        }

        input = input.toLowerCase().trim();

        if (input.isEmpty()) {
            return;
        }

        if (input.equals("salir") || input.equals("quit")) {
            System.exit(0);
            return;
        }

        if (input.equals("ayuda") || input.equals("help")) {
            getView(playerNumber).displayHelp();
            return;
        }

        if (input.startsWith("jugar ") || input.startsWith("play ")) {
            handlePlayCommand(playerNumber, input);
        } else {
            getView(playerNumber).displayError("Comando desconocido. Escribe 'ayuda' para ver instrucciones.");
        }
    }

    private void handlePlayCommand(int playerNumber, String input) {
        Player currentPlayer = gameState.getCurrentPlayer();
        PlayerView currentView = getView(playerNumber);

        String[] parts = input.split(" ");

        if (parts.length < 2) {
            currentView.displayError("Uso: jugar <carta#> [llevar <mesa#> ...]");
            return;
        }

        try {
            int cardIndex = Integer.parseInt(parts[1]) - 1;

            if (cardIndex < 0 || cardIndex >= currentPlayer.getHandSize()) {
                currentView.displayError("¡Número de carta inválido! Tienes " + currentPlayer.getHandSize() + " cartas.");
                return;
            }

            // Check if capturing cards
            if (parts.length > 2 && (parts[2].equals("llevar") || parts[2].equals("take"))) {
                List<Integer> tableIndices = new ArrayList<>();
                for (int i = 3; i < parts.length; i++) {
                    tableIndices.add(Integer.parseInt(parts[i]) - 1);
                }
                attemptCapture(playerNumber, cardIndex, tableIndices);
            } else {
                // Just place card on table
                placeCardOnTable(playerNumber, cardIndex);
            }

        } catch (NumberFormatException e) {
            currentView.displayError("¡Comando inválido! Usa: jugar <carta#> [llevar <mesa#> ...]");
        }
    }

    private void placeCardOnTable(int playerNumber, int cardIndex) {
        Player currentPlayer = gameState.getCurrentPlayer();
        PlayerView currentView = getView(playerNumber);

        Card card = currentPlayer.removeCardFromHand(cardIndex);
        gameState.addCardToTable(card);

        currentView.displayMessage("Pusiste " + card + " en la mesa");
        nextTurn();
    }

    private void attemptCapture(int playerNumber, int cardIndex, List<Integer> tableIndices) {
        Player currentPlayer = gameState.getCurrentPlayer();
        PlayerView currentView = getView(playerNumber);
        List<Card> table = gameState.getTable();

        Card playedCard = currentPlayer.getHand().get(cardIndex);
        int sum = playedCard.getGameValue();
        List<Card> toCapture = new ArrayList<>();

        // Validate table indices and calculate sum
        for (int idx : tableIndices) {
            if (idx < 0 || idx >= table.size()) {
                currentView.displayError("¡Número de carta de mesa inválido! La mesa tiene " + table.size() + " cartas.");
                return;
            }
            Card tableCard = table.get(idx);
            sum += tableCard.getGameValue();
            toCapture.add(tableCard);
        }

        // Check if sum equals 15
        if (sum != 15) {
            currentView.displayError("¡Las cartas no suman 15! Tu suma = " + sum);
            return;
        }

        // Valid capture!
        currentPlayer.removeCardFromHand(cardIndex);
        currentPlayer.addCapturedCard(playedCard);
        currentPlayer.addCapturedCards(toCapture);
        gameState.removeCardsFromTable(toCapture);

        currentView.displayMessage("¡Capturado! " + playedCard + " + " + toCapture + " = 15");

        // Check for escoba
        if (gameState.isTableEmpty()) {
            currentPlayer.incrementEscobas();
            currentView.displayMessage("*** ¡ESCOBA! ***");
        }

        nextTurn();
    }

    private void nextTurn() {
        // Check if both players need new cards
        if (!gameState.getPlayer1().hasCardsInHand() && !gameState.getPlayer2().hasCardsInHand()) {
            if (gameState.isDeckEmpty()) {
                endGame();
                return;
            } else {
                gameState.dealCardsToPlayers();
                view1.displayMessage("--- Nuevas cartas repartidas ---");
                view2.displayMessage("--- Nuevas cartas repartidas ---");
            }
        }

        gameState.switchTurn();
        updateBothViews();
    }

    private void endGame() {
        gameState.setGameOver(true);

        // Last player takes remaining table cards
        if (!gameState.isTableEmpty()) {
            Player currentPlayer = gameState.getCurrentPlayer();
            currentPlayer.addCapturedCards(gameState.getTable());
            gameState.getTable().clear();
        }

        // Calculate scores
        Player p1 = gameState.getPlayer1();
        Player p2 = gameState.getPlayer2();
        int score1 = ScoreCalculator.calculateScore(p1, p2);
        int score2 = ScoreCalculator.calculateScore(p2, p1);

        // Display results to both players
        displayGameOver(view1, p1, p2, score1, score2);
        displayGameOver(view2, p1, p2, score1, score2);
    }

    private void displayGameOver(PlayerView view, Player p1, Player p2, int score1, int score2) {
        view.displayMessage("\n");
        view.displayMessage("=================================");
        view.displayMessage("        ¡FIN DEL JUEGO!");
        view.displayMessage("=================================");
        view.displayMessage("");
        view.displayMessage(ScoreCalculator.getScoreBreakdown(p1, p2));
        view.displayMessage("");
        view.displayMessage(ScoreCalculator.getScoreBreakdown(p2, p1));
        view.displayMessage("");
        view.displayMessage("PUNTAJE FINAL:");
        view.displayMessage("  " + p1.getName() + ": " + score1 + " puntos");
        view.displayMessage("  " + p2.getName() + ": " + score2 + " puntos");
        view.displayMessage("");

        if (score1 > score2) {
            view.displayMessage("*** ¡" + p1.getName().toUpperCase() + " GANA! ***");
        } else if (score2 > score1) {
            view.displayMessage("*** ¡" + p2.getName().toUpperCase() + " GANA! ***");
        } else {
            view.displayMessage("*** ¡EMPATE! ***");
        }

        view.displayMessage("=================================");
    }

    private void updateBothViews() {
        updatePlayerView(1);
        updatePlayerView(2);
    }

    private void updatePlayerView(int playerNumber) {
        PlayerView view = getView(playerNumber);
        Player player = (playerNumber == 1) ? gameState.getPlayer1() : gameState.getPlayer2();
        Player opponent = (playerNumber == 1) ? gameState.getPlayer2() : gameState.getPlayer1();
        boolean isCurrentPlayer = gameState.getCurrentPlayerNumber() == playerNumber;

        view.displayGameState(gameState.getTable(), player, opponent,
                            gameState.getDeckSize(), isCurrentPlayer);
    }

    private PlayerView getView(int playerNumber) {
        return playerNumber == 1 ? view1 : view2;
    }
}
