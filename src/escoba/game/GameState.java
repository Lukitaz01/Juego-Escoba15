package escoba.game;

import escoba.events.GameEvent;
import escoba.model.Card;
import escoba.model.Deck;
import escoba.model.Player;
import framework.observer.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the game state for Escoba de 15.
 * Handles deck, table, players, and game flow.
 */
public class GameState extends Observable {
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

        // Notify observers that game has started
        notifyObservers(GameEvent.GAME_STARTED);
    }

    public void dealCardsToPlayers() {
        for (int i = 0; i < 3; i++) {
            Card card1 = deck.draw();
            if (card1 != null) player1.addCardToHand(card1);

            Card card2 = deck.draw();
            if (card2 != null) player2.addCardToHand(card2);
        }

        // Notify observers that cards were dealt
        notifyObservers(GameEvent.CARDS_DEALT);
    }

    public Player getCurrentPlayer() {
        return currentPlayerNumber == 1 ? player1 : player2;
    }

    public Player getOtherPlayer() {
        return currentPlayerNumber == 1 ? player2 : player1;
    }

    public void switchTurn() {
        currentPlayerNumber = currentPlayerNumber == 1 ? 2 : 1;

        // Notify observers that turn has switched
        notifyObservers(GameEvent.TURN_SWITCHED);
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

        // Notify observers if game is over
        if (gameOver) {
            notifyObservers(GameEvent.GAME_OVER);
        }
    }

    public int getDeckSize() {
        return deck.remainingCards();
    }

    public boolean isDeckEmpty() {
        return deck.isEmpty();
    }

    public void addCardToTable(Card card) {
        table.add(card);

        // Notify observers that table was updated
        notifyObservers(GameEvent.TABLE_UPDATED);
    }

    public void removeCardsFromTable(List<Card> cards) {
        table.removeAll(cards);

        // Notify observers that table was updated
        notifyObservers(GameEvent.TABLE_UPDATED);
    }

    public boolean isTableEmpty() {
        return table.isEmpty();
    }

    /**
     * Intenta jugar una carta en la mesa sin capturar.
     *
     * @param cardIndex Índice de la carta en la mano del jugador actual
     * @return Resultado de la jugada con mensaje apropiado
     */
    public ResultadoJugada jugarCarta(int cardIndex) {
        Player currentPlayer = getCurrentPlayer();

        if (cardIndex < 0 || cardIndex >= currentPlayer.getHandSize()) {
            return ResultadoJugada.error("Índice de carta inválido");
        }

        Card card = currentPlayer.removeCardFromHand(cardIndex);
        String mensaje = "Pusiste " + card + " en la mesa";

        addCardToTable(card);
        notifyObservers(GameEvent.CARD_PLACED_ON_TABLE);

        // Avanzar turno
        boolean continua = nextTurn();

        if (!continua) {
            return ResultadoJugada.exitoFinJuego(mensaje);
        }

        return ResultadoJugada.exitoSimple(mensaje);
    }

    /**
     * Intenta capturar cartas de la mesa.
     *
     * @param cardIndex Índice de la carta en la mano del jugador actual
     * @param tableIndices Índices de las cartas de la mesa a capturar
     * @return Resultado de la jugada con mensaje apropiado
     */
    public ResultadoJugada intentarCaptura(int cardIndex, List<Integer> tableIndices) {
        Player currentPlayer = getCurrentPlayer();

        // Validar índice de carta de mano
        if (cardIndex < 0 || cardIndex >= currentPlayer.getHandSize()) {
            return ResultadoJugada.error("Índice de carta inválido");
        }

        // Validar índices de mesa
        for (int idx : tableIndices) {
            if (idx < 0 || idx >= table.size()) {
                return ResultadoJugada.error("¡Número de carta de mesa inválido! La mesa tiene " + table.size() + " cartas.");
            }
        }

        Card playedCard = currentPlayer.getHand().get(cardIndex);
        int sum = playedCard.getGameValue();
        List<Card> toCapture = new ArrayList<>();

        // Calcular suma
        for (int idx : tableIndices) {
            Card tableCard = table.get(idx);
            sum += tableCard.getGameValue();
            toCapture.add(tableCard);
        }

        // Validar que sume 15
        if (sum != 15) {
            return ResultadoJugada.error("¡Las cartas no suman 15! Tu suma = " + sum);
        }

        // Captura válida - ejecutar
        currentPlayer.removeCardFromHand(cardIndex);
        currentPlayer.addCapturedCard(playedCard);
        currentPlayer.addCapturedCards(toCapture);
        removeCardsFromTable(toCapture);

        String mensaje = "¡Capturado! " + playedCard + " + " + toCapture + " = 15";

        notifyObservers(GameEvent.CARDS_CAPTURED);

        // Verificar escoba
        boolean esEscoba = false;
        if (isTableEmpty()) {
            currentPlayer.incrementEscobas();
            notifyObservers(GameEvent.ESCOBA_SCORED);
            esEscoba = true;
        }

        // Avanzar turno
        boolean continua = nextTurn();

        if (!continua) {
            return ResultadoJugada.exitoFinJuego(mensaje);
        }

        if (esEscoba) {
            return ResultadoJugada.exitoConEscoba(mensaje);
        }

        return ResultadoJugada.exitoSimple(mensaje);
    }

    /**
     * Avanza al siguiente turno, repartiendo cartas si es necesario o terminando el juego.
     *
     * @return true si el juego continúa, false si terminó
     */
    private boolean nextTurn() {
        // Verificar si ambos jugadores necesitan cartas nuevas
        if (!player1.hasCardsInHand() && !player2.hasCardsInHand()) {
            if (isDeckEmpty()) {
                finishGame();
                return false;
            } else {
                dealCardsToPlayers();
            }
        }

        switchTurn();
        return true;
    }

    /**
     * Termina el juego, dando las cartas restantes de la mesa al último jugador.
     */
    private void finishGame() {
        setGameOver(true);

        // El último jugador se lleva las cartas restantes de la mesa
        if (!isTableEmpty()) {
            Player currentPlayer = getCurrentPlayer();
            currentPlayer.addCapturedCards(table);
            table.clear();
        }
    }

    /**
     * Obtiene el resumen del fin del juego con todos los puntajes.
     *
     * @return Array de Strings con las líneas del resumen
     */
    public String[] obtenerResumenFinJuego() {
        int score1 = ScoreCalculator.calculateScore(player1, player2);
        int score2 = ScoreCalculator.calculateScore(player2, player1);

        List<String> lineas = new ArrayList<>();

        lineas.add("");
        lineas.add("=================================");
        lineas.add("        ¡FIN DEL JUEGO!");
        lineas.add("=================================");
        lineas.add("");
        lineas.add(ScoreCalculator.getScoreBreakdown(player1, player2));
        lineas.add("");
        lineas.add(ScoreCalculator.getScoreBreakdown(player2, player1));
        lineas.add("");
        lineas.add("PUNTAJE FINAL:");
        lineas.add("  " + player1.getName() + ": " + score1 + " puntos");
        lineas.add("  " + player2.getName() + ": " + score2 + " puntos");
        lineas.add("");

        if (score1 > score2) {
            lineas.add("*** ¡" + player1.getName().toUpperCase() + " GANA! ***");
        } else if (score2 > score1) {
            lineas.add("*** ¡" + player2.getName().toUpperCase() + " GANA! ***");
        } else {
            lineas.add("*** ¡EMPATE! ***");
        }

        lineas.add("=================================");

        return lineas.toArray(new String[0]);
    }
}
