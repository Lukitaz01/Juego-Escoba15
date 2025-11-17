package framework.observer;

/**
 * Observable interface for the Observer pattern.
 * Classes implementing this interface can be observed by IObserver objects.
 */
public interface IObservable {

    /**
     * Adds an observer to the list of observers.
     *
     * @param observer The observer to add
     */
    void addObserver(IObserver observer);

    /**
     * Removes an observer from the list of observers.
     *
     * @param observer The observer to remove
     */
    void removeObserver(IObserver observer);

    /**
     * Notifies all observers of a change.
     *
     * @param event The event that occurred
     */
    void notifyObservers(Object event);
}
