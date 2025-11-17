package example;

/**
 * Main class for the example game.
 * Demonstrates how to set up and run a game using the MVC framework.
 *
 * This example shows:
 * 1. How to create a Model (SimpleGameModel)
 * 2. How to create a View (SimpleGameView)
 * 3. How to create a Controller (SimpleGameController)
 * 4. How to wire them together
 * 5. How the Observer pattern works (Model -> Controller -> View)
 */
public class Main {

    public static void main(String[] args) {
        // Create MVC components
        SimpleGameModel model = new SimpleGameModel(1, 100);
        SimpleGameView view = new SimpleGameView();
        SimpleGameController controller = new SimpleGameController();

        // Wire them together
        // Controller observes Model and controls View
        controller.setModel(model);
        controller.setView(view);

        // Initialize and start the game
        controller.initialize();
        view.display();
    }
}
