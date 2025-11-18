package escoba.events;

/**
 * Enum representing different game events in Escoba de 15.
 * Used with the Observer pattern to notify views of game state changes.
 */
public enum GameEvent {
    // Game flow events
    GAME_STARTED,           // Game has started/reset
    GAME_OVER,              // Game has ended
    TURN_SWITCHED,          // Turn has changed to other player
    CARDS_DEALT,            // New cards dealt to players

    // Player action events
    CARD_PLACED_ON_TABLE,   // Player placed a card on the table
    CARDS_CAPTURED,         // Player captured cards from the table
    ESCOBA_SCORED,          // Player made an escoba (cleared the table)

    // State change events
    TABLE_UPDATED,          // Table cards changed
    PLAYER_HAND_UPDATED,    // Player's hand changed
    SCORE_UPDATED           // Score/captured cards updated
}
