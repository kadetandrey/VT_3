package by.bsuir.client.observable;

import java.util.ArrayList;
import java.util.List;

public class Observable<V>{

    public interface Callback<V> {
        void call(V value);
    }

    private final List<Callback<V>> subscribers = new ArrayList<>();
    private final List<Callback<Exception>> catchers = new ArrayList<>();
    private boolean isPresent = false;
    private boolean isThrown = false;
    private V value;
    private Exception thrown;

    public void setValue(V value) {
        if(isThrown) return;
        isPresent = true;
        this.value = value;
        notifySubscribers();
    }

    public void throwError(Exception e) {
        isThrown = true;
        thrown = e;
        notifyCatchers();
    }

    public void subscribe(Callback<V> callback) {
        subscribers.add(callback);
        if(isPresent) callback.call(value);
    }

    public void subscribe(Callback<V> callback, Callback<Exception> catchCallback) {
        catchers.add(catchCallback);
        if(isThrown) catchCallback.call(thrown);

        subscribers.add(callback);
        if(isPresent) callback.call(value);
    }

    public void unsubscribe(Callback<V> callback) {
        subscribers.remove(callback);
    }

    public void unsubscribe(Callback<V> callback, Callback<Exception> catchCallback) {
        unsubscribe(callback);
        catchers.remove(catchCallback);
    }

    private void notifySubscribers() {
        subscribers.forEach(subscriber -> subscriber.call(value));
    }

    private void notifyCatchers() {
        catchers.forEach(subscriber -> subscriber.call(thrown));
    }
}
