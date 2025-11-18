package escoba.game;

import escoba.model.Card;
import escoba.model.Player;

import java.util.List;

/**
 * Calcula las puntuaciones para Escoba de 15.
 * Reglas de puntuación:
 *  * - 1 punto por cada escoba (levantar todas las cartas)
 *  * - 1 punto por la mayoría de las cartas capturadas
 *  * - 1 punto por la mayoría de las cartas de Oro
 *  * - 1 punto por tener el 7 de Oro
 *  * - 1 punto por la mayoría de los 7
 */
public class ScoreCalculator {

    public static int calculateScore(Player player, Player opponent) {
        int score = 0;

        // Points from escobas
        score += player.getEscobasCount();

        // Most cards (1 point)
        if (player.getCapturedCount() > opponent.getCapturedCount()) {
            score++;
        }

        // Most golds (1 point)
        int playerGolds = countSuit(player.getCapturedCards(), "Oro");
        int opponentGolds = countSuit(opponent.getCapturedCards(), "Oro");
        if (playerGolds > opponentGolds) {
            score++;
        }

        // 7 of golds (1 point)
        if (hasCard(player.getCapturedCards(), 7, "Oro")) {
            score++;
        }

        // Most 7s (1 point)
        int playerSevens = countValue(player.getCapturedCards(), 7);
        int opponentSevens = countValue(opponent.getCapturedCards(), 7);
        if (playerSevens > opponentSevens) {
            score++;
        }

        return score;
    }

    private static int countSuit(List<Card> cards, String suit) {
        int count = 0;
        for (Card card : cards) {
            if (card.getSuit().equals(suit)) {
                count++;
            }
        }
        return count;
    }

    private static int countValue(List<Card> cards, int cardNumber) {
        int count = 0;
        for (Card card : cards) {
            if (card.getCardNumber() == cardNumber) {
                count++;
            }
        }
        return count;
    }

    private static boolean hasCard(List<Card> cards, int cardNumber, String suit) {
        for (Card card : cards) {
            if (card.getCardNumber() == cardNumber && card.getSuit().equals(suit)) {
                return true;
            }
        }
        return false;
    }

    public static String getScoreBreakdown(Player player, Player opponent) {
        StringBuilder sb = new StringBuilder();
        sb.append(player.getName() + " - Desglose de Puntos:\n");

        int escobasPoints = player.getEscobasCount();
        sb.append("  Escobas: " + player.getEscobasCount() + " x 1 = " + escobasPoints + " pts\n");

        int cardsPoint = player.getCapturedCount() > opponent.getCapturedCount() ? 1 : 0;
        sb.append("  Más cartas: " + player.getCapturedCount() + " vs " + opponent.getCapturedCount() + " = " + cardsPoint + " pt\n");

        int playerGolds = countSuit(player.getCapturedCards(), "Oro");
        int opponentGolds = countSuit(opponent.getCapturedCards(), "Oro");
        int goldsPoint = playerGolds > opponentGolds ? 1 : 0;
        sb.append("  Más Oros: " + playerGolds + " vs " + opponentGolds + " = " + goldsPoint + " pt\n");

        int goldSevenPoint = hasCard(player.getCapturedCards(), 7, "Oro") ? 1 : 0;
        sb.append("  7 de Oro: " + (goldSevenPoint == 1 ? "Sí" : "No") + " = " + goldSevenPoint + " pt\n");

        int playerSevens = countValue(player.getCapturedCards(), 7);
        int opponentSevens = countValue(opponent.getCapturedCards(), 7);
        int sevensPoint = playerSevens > opponentSevens ? 1 : 0;
        sb.append("  Más 7s: " + playerSevens + " vs " + opponentSevens + " = " + sevensPoint + " pt\n");

        int total = escobasPoints + cardsPoint + goldsPoint + goldSevenPoint + sevensPoint;
        sb.append("  TOTAL: " + total + " puntos\n");

        return sb.toString();
    }
}
