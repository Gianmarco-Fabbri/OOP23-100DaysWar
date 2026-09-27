package it.unibo.the100dayswar.controller.events;

import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.core.Observable;

/**
 * EventBus using RxJava to handle messaging between components.
 */
public class EventBus {
    private final PublishSubject<Object> bus = PublishSubject.create();

    /**
     * Publishes an event to the bus.
     * @param event the event to publish
     */
    public void publish(final Object event) {
        bus.onNext(event);
    }

    /**
     * Subscribes to events of the given type.
     * @param eventType the class of the event
     * @param <T> the type of the event
     * @return an Observable emitting events of the specified type
     */
    public <T> Observable<T> on(final Class<T> eventType) {
        return bus.ofType(eventType);
    }
}
