package example;

import framework.mvc.Model;

import java.util.Random;

/**
 * Simple number guessing game model.
 * Demonstrates how to extend the Model base class.
 *
 * Game rules:
 * - System picks a random number between 1 and 100
 * - Player tries to guess the number
 * - System gives hints (too high, too low)
 * - Game tracks number of attempts
 */
public class SimpleGameModel extends Model {

    private int secretNumber;
    private int attempts;
    private int lastGuess;
    private boolean gameWon;
    private final Random random;
    private final int minNumber;
    private final int maxNumber;

    public SimpleGameModel() {
        this(1, 100);
    }

    public SimpleGameModel(int minNumber, int maxNumber) {
        this.minNumber = minNumber;
        this.maxNumber = maxNumber;
        this.random = new Random();
    }

    @Override
    public void initialize() {
        secretNumber = random.nextInt(maxNumber - minNumber + 1) + minNumber;
        attempts = 0;
        lastGuess = 0;
        gameWon = false;
        notifyObservers(GameEvent.GAME_STARTED);
    }

    @Override
    public void reset() {
        initialize();
        notifyObservers(GameEvent.GAME_RESET);
    }

    /**
     * Makes a guess.
     *
     * @param guess The player's guess
     */
    public void makeGuess(int guess) {
        if (gameWon) {
            return; // Game already won
        }

        lastGuess = guess;
        attempts++;

        if (guess < secretNumber) {
            notifyObservers(GameEvent.GUESS_TOO_LOW);
        } else if (guess > secretNumber) {
            notifyObservers(GameEvent.GUESS_TOO_HIGH);
        } else {
            gameWon = true;
            notifyObservers(GameEvent.GUESS_CORRECT);
        }
    }

    /**
     * Handles invalid input.
     */
    public void invalidInput() {
        notifyObservers(GameEvent.INVALID_INPUT);
    }

    // Getters

    public int getSecretNumber() {
        return secretNumber;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getLastGuess() {
        return lastGuess;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public int getMinNumber() {
        return minNumber;
    }

    public int getMaxNumber() {
        return maxNumber;
    }
}
