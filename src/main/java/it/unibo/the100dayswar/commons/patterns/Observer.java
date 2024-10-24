package it.unibo.the100dayswar.commons.patterns;

import it.unibo.the100dayswar.commons.utilities.impl.GameEvent;

/**
 * An interface for a simple Observer pattern.
 *
 * @param <T> the type of the source of a possible event
 */
public interface Observer<T> {
    /**
     * Notify the observer that the events it observes was caused by
     * source.
     *
     * @param source the source of the event
     * @param event the event that occurred
     */
    void update(GameEvent event, T source);
}
