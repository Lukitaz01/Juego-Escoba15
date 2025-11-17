package example;

/**
 * Events that can occur in the example game.
 * These events are passed to observers when the model state changes.
 */
public enum GameEvent {
    GAME_STARTED,       // Game has started with a new number
    GUESS_TOO_LOW,      // Player's guess was too low
    GUESS_TOO_HIGH,     // Player's guess was too high
    GUESS_CORRECT,      // Player guessed correctly
    GAME_RESET,         // Game was reset
    INVALID_INPUT       // Player entered invalid input
}
