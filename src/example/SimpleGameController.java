package example;

import framework.mvc.Controller;

/**
 * Controller for the simple number guessing game.
 * Handles user input and updates the model.
 */
public class SimpleGameController extends Controller {

    @Override
    public void initialize() {
        if (model instanceof SimpleGameModel) {
            model.initialize();
        }
    }

    /**
     * Processes a command from the view.
     *
     * @param command The command to process
     */
    public void processCommand(String command) {
        if (command == null || command.trim().isEmpty()) {
            return;
        }

        command = command.trim().toLowerCase();

        // Check for special commands
        if (command.equals("quit") || command.equals("q")) {
            if (view != null) {
                view.showMessage("Thanks for playing! Goodbye!");
            }
            System.exit(0);
            return;
        }

        if (command.equals("reset") || command.equals("r")) {
            if (model instanceof SimpleGameModel) {
                model.reset();
            }
            return;
        }

        // Try to parse as a number
        try {
            int guess = Integer.parseInt(command);
            if (model instanceof SimpleGameModel) {
                SimpleGameModel gameModel = (SimpleGameModel) model;

                // Validate range
                if (guess < gameModel.getMinNumber() || guess > gameModel.getMaxNumber()) {
                    if (view != null) {
                        view.showError("Please enter a number between "
                            + gameModel.getMinNumber() + " and " + gameModel.getMaxNumber());
                    }
                    return;
                }

                gameModel.makeGuess(guess);
            }
        } catch (NumberFormatException e) {
            // Not a number
            if (model instanceof SimpleGameModel) {
                ((SimpleGameModel) model).invalidInput();
            }
        }
    }

    /**
     * Makes a guess (alternative method for programmatic access).
     *
     * @param guess The guess to make
     */
    public void makeGuess(int guess) {
        if (model instanceof SimpleGameModel) {
            ((SimpleGameModel) model).makeGuess(guess);
        }
    }

    /**
     * Resets the game.
     */
    public void resetGame() {
        if (model != null) {
            model.reset();
        }
    }
}
