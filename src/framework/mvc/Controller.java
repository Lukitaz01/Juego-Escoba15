package framework.mvc;

import framework.observer.IObservable;
import framework.observer.IObserver;

/**
 * Base class for Controller in MVC pattern.
 * Implements IObserver to receive notifications from the Model.
 *
 * The Controller:
 * - Receives user input from the View
 * - Updates the Model based on user actions
 * - Receives notifications from Model via Observer pattern
 * - Updates the View to reflect model changes
 *
 * Responsibilities:
 * - Handle user input and translate it to model operations
 * - Listen for model changes and update the view accordingly
 * - Coordinate the flow of data between Model and View
 */
public abstract class Controller implements IObserver {

    protected Model model;
    protected IView view;

    /**
     * Sets the model for this controller and registers as an observer.
     *
     * @param model The model to control
     */
    public void setModel(Model model) {
        // Remove observer from old model if exists
        if (this.model != null) {
            this.model.removeObserver(this);
        }

        this.model = model;

        // Register as observer of the new model
        if (this.model != null) {
            this.model.addObserver(this);
        }
    }

    /**
     * Sets the view for this controller.
     *
     * @param view The view to control
     */
    public void setView(IView view) {
        this.view = view;
        if (this.view != null) {
            this.view.setController(this);
        }
    }

    /**
     * Gets the current model.
     *
     * @return The current model
     */
    public Model getModel() {
        return model;
    }

    /**
     * Gets the current view.
     *
     * @return The current view
     */
    public IView getView() {
        return view;
    }

    /**
     * Called when the model notifies observers of a change.
     * Override this method to handle different events and update the view.
     *
     * @param observable The observable object (usually the model)
     * @param event The event that occurred
     */
    @Override
    public void update(IObservable observable, Object event) {
        if (view != null) {
            view.update(event);
        }
    }

    /**
     * Initialize the controller.
     * Override this method to perform initialization tasks.
     */
    public abstract void initialize();
}
