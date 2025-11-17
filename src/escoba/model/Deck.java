package escoba.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CLASE: Deck (Mazo)
 *
 * Representa un mazo de cartas españolas de 40 cartas.
 * Contiene cartas del 1-7 y Sota(10), Caballo(11), Rey(12) en 4 palos.
 *
 * PALOS DE LA BARAJA ESPAÑOLA:
 * - Oro: Representa monedas/riqueza
 * - Copa: Representa copas/celebración
 * - Espada: Representa armas/fuerza
 * - Basto: Representa palos/bastones
 *
 * CÓMO MODIFICAR:
 * - Para cambiar los palos: modificar el array 'suits' en initialize()
 * - Para agregar/quitar cartas: modificar los loops en initialize()
 * - Para cambiar la cantidad de cartas: modificar la lógica del constructor
 */
public class Deck {
    // Lista de cartas en el mazo
    private List<Card> cards;

    /**
     * Constructor del mazo.
     * Crea un mazo completo de 40 cartas españolas.
     */
    public Deck() {
        cards = new ArrayList<>();
        initialize();
    }

    /**
     * Inicializa el mazo con todas las cartas.
     *
     * CÓMO FUNCIONA:
     * 1. Para cada palo (Oro, Copa, Espada, Basto)
     * 2. Agrega cartas del 1 al 7
     * 3. Agrega las figuras: Sota(10), Caballo(11), Rey(12)
     *
     * CÓMO MODIFICAR:
     * - Para cambiar los palos: modificar el array 'suits'
     * - Para agregar más cartas numéricas: cambiar el límite del loop (1 <= value <= 7)
     * - Para agregar/quitar figuras: modificar las líneas que agregan cartas 10, 11, 12
     */
    private void initialize() {
        // Define los 4 palos de la baraja española
        String[] suits = {"Oro", "Copa", "Espada", "Basto"};

        // Para cada palo, crear todas las cartas
        for (String suit : suits) {
            // Agregar cartas numéricas (1-7)
            for (int value = 1; value <= 7; value++) {
                cards.add(new Card(value, suit));
            }

            // Agregar figuras (Sota, Caballo, Rey)
            cards.add(new Card(10, suit));  // Sota
            cards.add(new Card(11, suit));  // Caballo
            cards.add(new Card(12, suit));  // Rey
        }
    }

    /**
     * Mezcla las cartas del mazo aleatoriamente.
     *
     * CUÁNDO USAR:
     * - Al inicio de cada partida nueva
     * - Después de crear un mazo nuevo
     *
     * CÓMO FUNCIONA:
     * Usa Collections.shuffle() para ordenar las cartas aleatoriamente.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Saca una carta del mazo.
     *
     * IMPORTANTE: Esta carta se REMUEVE del mazo permanentemente.
     * No se puede devolver al mazo.
     *
     * @return La carta sacada, o null si el mazo está vacío
     *
     * EJEMPLO DE USO:
     * Card card = deck.draw();
     * if (card != null) {
     *     player.addCardToHand(card);
     * }
     */
    public Card draw() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(0);  // Saca la primera carta
    }

    /**
     * Verifica si el mazo está vacío.
     *
     * @return true si no quedan cartas, false si quedan cartas
     *
     * CUÁNDO USAR:
     * - Antes de repartir cartas
     * - Para verificar si el juego debe terminar
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Obtiene la cantidad de cartas que quedan en el mazo.
     *
     * @return Número de cartas restantes
     *
     * ÚTIL PARA:
     * - Mostrar al jugador cuántas cartas quedan
     * - Decidir si se pueden repartir más cartas
     */
    public int remainingCards() {
        return cards.size();
    }
}
