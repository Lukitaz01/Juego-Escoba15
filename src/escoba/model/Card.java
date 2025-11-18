package escoba.model;

/**
 * CLASE: Card (Carta)
 *
 * Representa una carta de la baraja española.
 * La baraja española tiene 40 cartas: 1-7, Sota, Caballo, Rey en 4 palos.
 *
 * VALORES IMPORTANTES PARA LA ESCOBA DE 15:
 * - Cartas 1-7: valen su número (1, 2, 3, 4, 5, 6, 7)
 * - Sota: vale 8 puntos para sumar 15
 * - Caballo: vale 9 puntos para sumar 15
 * - Rey: vale 10 puntos para sumar 15
 *
 * CÓMO MODIFICAR:
 * - Para cambiar valores de las figuras, modificar el método getGameValue()
 * - Para cambiar nombres de cartas, modificar getValueName()
 * - Para cambiar palos, modificar en la clase Deck
 */
public class Card {

    private int cardNumber;// Número interno de la carta (1-7, 10-12)


    private String suit;// Palo de la carta (Oro, Copa, Espada, Basto)

    /**
     * Constructor de la carta.
     *
     * @param cardNumber Número de la carta (1-7 para numéricas, 10=Sota, 11=Caballo, 12=Rey)
     * @param suit Palo de la carta (Oro, Copa, Espada, Basto)
     */
    public Card(int cardNumber, String suit) {
        this.cardNumber = cardNumber;
        this.suit = suit;
    }

    /**
     * Obtiene el valor de juego de la carta (usado para sumar 15).
     *
     * IMPORTANTE: Este es el valor que se usa para el juego.
     * - Cartas 1-7: valen su número
     * - Sota (10): vale 8
     * - Caballo (11): vale 9
     * - Rey (12): vale 10
     *
     * CÓMO CAMBIAR: Modifica los valores en el switch si quieres
     * cambiar las reglas del juego.
     *
     * @return Valor de la carta para el juego
     */
    public int getGameValue() {
        switch (cardNumber) {
            case 10: return 8;  // Sota vale 8
            case 11: return 9;  // Caballo vale 9
            case 12: return 10; // Rey vale 10
            default: return cardNumber; // Las demás valen su número
        }
    }

    /**
     * Obtiene el palo de la carta.
     *
     * @return Palo (Oro, Copa, Espada, Basto)
     */
    public String getSuit() {
        return suit;
    }

    /**
     * Obtiene el número interno de la carta (1-7, 10-12).
     * Útil para comparaciones y lógica interna.
     *
     * @return Número de carta
     */
    public int getCardNumber() {
        return cardNumber;
    }

    /**
     * Obtiene el nombre de la carta en español.
     *
     * CÓMO CAMBIAR: Modifica los casos del switch para
     * cambiar los nombres de las cartas.
     *
     * @return Nombre de la carta (As, Sota, Caballo, Rey, o número)
     */
    public String getValueName() {
        switch (cardNumber) {
            case 1: return "As";
            case 10: return "Sota";
            case 11: return "Caballo";
            case 12: return "Rey";
            default: return String.valueOf(cardNumber);
        }
    }

    /**
     * Representación completa de la carta en texto.
     * Ejemplo: "7 de Oro", "Sota de Copa"
     *
     * @return Texto descriptivo de la carta
     */
    @Override
    public String toString() {
        return getValueName() + " de " + suit;
    }

    /**
     * Representación corta de la carta.
     * Útil para mostrar en espacios reducidos.
     * Ejemplo: "7O", "SC" (Sota de Copa)
     *
     * @return Texto corto de la carta
     */
    public String toShortString() {
        String valueName;
        switch (cardNumber) {
            case 1: valueName = "A"; break;
            case 10: valueName = "S"; break;
            case 11: valueName = "C"; break;
            case 12: valueName = "R"; break;
            default: valueName = String.valueOf(cardNumber);
        }
        return valueName + suit.charAt(0);
    }

    /**
     * Compara si dos cartas son iguales.
     * Dos cartas son iguales si tienen el mismo número y palo.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return cardNumber == card.cardNumber && suit.equals(card.suit);
    }

    /**
     * Genera un código hash para la carta.
     * Necesario para usar cartas en colecciones como HashSet.
     */
    @Override
    public int hashCode() {
        return 31 * cardNumber + suit.hashCode();
    }
}
