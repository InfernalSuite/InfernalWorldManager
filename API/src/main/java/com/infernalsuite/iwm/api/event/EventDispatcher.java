package com.infernalsuite.iwm.api.event;

import com.infernalsuite.iwm.api.event.type.Cancellable;
import com.infernalsuite.iwm.api.event.type.ResultEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The IWM Event Dispatcher.
 *
 * <p>Used to dispatch events from IWM.</p>
 */
public interface EventDispatcher {

    /**
     * Posts an {@link IWMEvent event} to the {@link EventBus} asynchronously to the dispatching thread.
     *
     * @param eventClass the class representing the event to be posted
     * @param params the event parameters
     *
     * @apiNote This method should <strong>never</strong> be called for {@link Cancellable} or {@link ResultEvent} event types.
     * These types of events <strong>must</strong> run synchronously to the dispatching thread for obtaining the event result.
     *
     * @throws RuntimeException if the given event class is either {@link Cancellable} or {@link ResultEvent}
     *
     * @see #postSync(Class, Object...) postSync
     * @see #postCancellable(Class, Object...) postCancellable
     */
    void postAsync(@NonNull Class<? extends IWMEvent> eventClass, Object... params);

    /**
     * Posts an {@link IWMEvent event} to the {@link EventBus} synchronously to the dispatching thread.
     *
     * @param eventClass the class representing the event to be posted
     * @param params the event parameters
     * @apiNote This method will block the dispatching thread until all handlers have completed.
     * This method should be called for {@link ResultEvent} event types.
     */
    void postSync(@NonNull Class<? extends IWMEvent> eventClass, Object... params);

    /**
     * Posts a {@link Cancellable} {@link IWMEvent event} to the {@link EventBus}.
     *
     * <p><strong>Important:</strong> the first argument of the parameter array <strong>must</strong> be a
     * boolean representing the initial cancellation state of the event.</p>
     *
     * @apiNote This method will block the dispatching thread until all handlers have completed. This method
     * should be called for {@link Cancellable} event types.
     *
     * @throws RuntimeException if the given event class is not {@link Cancellable}
     *
     * @param eventClass the class representing the event to be posted
     * @param params the event parameters
     * @return {@code true} if the event was cancelled
     */
    boolean postCancellable(@NonNull Class<? extends IWMEvent> eventClass, Object... params);

}
