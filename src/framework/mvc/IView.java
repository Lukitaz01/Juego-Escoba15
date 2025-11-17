package framework.mvc;

/**
 * View interface for MVC pattern.
 * The View is responsible for displaying data to the user and capturing user input.
 *
 * Responsibilities:
 * - Display the current state of the model
 * - Capture and forward user input to the controller
 * - Update the display when notified by the controller
 */
public interface IView {

    /**
     * Sets the controller for this view.
     * The view will forward user actions to this controller.
     *
     * @param controller The controller to handle user actions
     */
    void setController(Controller controller);

    /**
     * Makes the view visible to the user.
     */
    void display();

    /**
     * Hides the view from the user.
     */
    void hide();

    /**
     * Displays a message to the user.
     *
     * @param message The message to display
     */
    void showMessage(String message);

    /**
     * Displays an error message to the user.
     *
     * @param error The error message to display
     */
    void showError(String error);

    /**
     * Updates the view with the current state.
     * This method is called by the controller when the model changes.
     *
     * @param event The event that triggered the update
     */
    void update(Object event);
}
