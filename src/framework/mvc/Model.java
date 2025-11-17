package framework.mvc;

import framework.observer.Observable;

/**
 * Base class for Model in MVC pattern.
 * Extends Observable to notify observers (usually Controllers) of changes.
 *
 * The Model contains:
 * - Game/application state
 * - Business logic
 * - Data manipulation methods
 *
 * When the model's state changes, it should call notifyObservers(event)
 * to inform all registered observers.
 */
public abstract class Model extends Observable {

    /**
     * Initialize the model with default state.
     * Override this method to set up initial state for your game/application.
     */
    public abstract void initialize();

    /**
     * Reset the model to its initial state.
     * Override this method to implement reset logic.
     */
    public abstract void reset();
}
