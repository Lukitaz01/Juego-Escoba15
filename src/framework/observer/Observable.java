package framework.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for observable objects.
 * Provides default implementation of the Observer pattern.
 * Extend this class for your model objects that need to notify observers.
 */
public abstract class Observable implements IObservable {

    private final List<IObserver> observers;

    public Observable() {
        this.observers = new ArrayList<>();
    }

    @Override
    public void addObserver(IObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Object event) {
        for (IObserver observer : new ArrayList<>(observers)) {
            observer.update(this, event);
        }
    }

    /**
     * Helper method to get the number of observers.
     *
     * @return The number of observers
     */
    protected int getObserverCount() {
        return observers.size();
    }
}
