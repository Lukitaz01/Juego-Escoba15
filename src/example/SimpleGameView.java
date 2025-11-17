package example;

import framework.view.SwingConsoleView;

/**
 * View for the simple number guessing game.
 * Extends SwingConsoleView to provide a console interface.
 */
public class SimpleGameView extends SwingConsoleView {

    public SimpleGameView() {
        super("Number Guessing Game", 600, 400);
    }

    @Override
    protected void handleInput(String input) {
        if (controller == null) {
            showError("Controller not set!");
            return;
        }

        // Forward input to controller
        if (controller instanceof SimpleGameController) {
            ((SimpleGameController) controller).processCommand(input);
        }
    }

    @Override
    public void update(Object event) {
        if (!(event instanceof GameEvent)) {
            return;
        }

        GameEvent gameEvent = (GameEvent) event;
        SimpleGameModel model = (SimpleGameModel) ((SimpleGameController) controller).getModel();

        switch (gameEvent) {
            case GAME_STARTED:
                clear();
                showInfo("========================================");
                showInfo("   WELCOME TO NUMBER GUESSING GAME!");
                showInfo("========================================");
                showMessage("");
                showMessage("I'm thinking of a number between "
                    + model.getMinNumber() + " and " + model.getMaxNumber() + ".");
                showMessage("Can you guess it?");
                showMessage("");
                showMessage("Commands:");
                showMessage("  - Enter a number to make a guess");
                showMessage("  - 'reset' or 'r' to start a new game");
                showMessage("  - 'quit' or 'q' to exit");
                showMessage("");
                break;

            case GUESS_TOO_LOW:
                showMessage("Your guess: " + model.getLastGuess());
                showInfo("Too low! Try a higher number.");
                showMessage("Attempts: " + model.getAttempts());
                showMessage("");
                break;

            case GUESS_TOO_HIGH:
                showMessage("Your guess: " + model.getLastGuess());
                showInfo("Too high! Try a lower number.");
                showMessage("Attempts: " + model.getAttempts());
                showMessage("");
                break;

            case GUESS_CORRECT:
                showMessage("Your guess: " + model.getLastGuess());
                showSuccess("========================================");
                showSuccess("  CONGRATULATIONS! You guessed it!");
                showSuccess("========================================");
                showSuccess("The number was: " + model.getSecretNumber());
                showSuccess("It took you " + model.getAttempts() + " attempts!");
                showMessage("");
                showMessage("Type 'reset' to play again or 'quit' to exit.");
                showMessage("");
                break;

            case GAME_RESET:
                showInfo("Game reset! New number generated.");
                showMessage("");
                break;

            case INVALID_INPUT:
                showError("Invalid input! Please enter a number, 'reset', or 'quit'.");
                showMessage("");
                break;
        }
    }

    @Override
    protected void onWindowClosing() {
        showMessage("Thanks for playing!");
    }
}
