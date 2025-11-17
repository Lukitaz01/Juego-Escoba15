package framework.observer;

/**
 * Observer interface for the Observer pattern.
 * Classes implementing this interface can observe changes in Observable objects.
 */
public interface IObserver {

    /**
     * Called when the observed object changes.
     *
     * @param observable The observable object that changed
     * @param event The event that occurred (can be an enum, String, or custom event object)
     */
    void update(IObservable observable, Object event);
}
