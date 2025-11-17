package escoba;

import escoba.controller.GameController;
import escoba.game.GameState;
import escoba.view.PlayerView;

import javax.swing.*;

/**
 * Main entry point for La Escoba de 15 game.
 * Sets up the game with two player windows and starts the game.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create game state
            GameState gameState = new GameState();

            // Create views for both players
            PlayerView view1 = new PlayerView("Player 1", 100, 100);
            PlayerView view2 = new PlayerView("Player 2", 750, 100);

            // Create game controller
            GameController controller = new GameController(gameState, view1, view2);

            // Set up input listeners
            view1.setInputListener(e -> {
                String input = view1.getInput();
                view1.clearInput();
                controller.processPlayerInput(1, input);
            });

            view2.setInputListener(e -> {
                String input = view2.getInput();
                view2.clearInput();
                controller.processPlayerInput(2, input);
            });

            // Show windows and start game
            view1.show();
            view2.show();
            controller.startGame();
        });
    }
}
