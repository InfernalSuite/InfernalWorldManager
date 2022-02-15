package com.infernalsuite.iwm.api.event;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;
import java.util.function.Consumer;

/**
 * The IWM Event Bus.
 *
 * <p>Used to subscribe to IWM events.</p>
 */
public interface EventBus {

    /**
     * Registers a new subscription to the given event.
     *
     * <p>The returned {@link EventSubscription} instance encapsulates the subscription state. It has methods which can
     * be used to terminate the subscription, or get information about the nature of subscription.</p>
     *
     * @param eventClass    the event class
     * @param handler       the event handler
     * @param <T>           the event type
     * @return an {@link EventSubscription} representing this subscription
     */
    <T extends IWMEvent> @NonNull EventSubscription<T> subscribe(@NonNull Class<T> eventClass, @NonNull Consumer<? super T> handler);

    /**
     * Registers a new subscription to the given event.
     *
     * <p>The returned {@link EventSubscription} instance encapsulates the subscription state. It has methods which can
     * be used to terminate the subscription, or get information about the nature of subscription.</p>
     *
     * <p>In contrast to {@link #subscribe(Class, Consumer)}, this method takes an additional parameter for {@code instance}.
     * This object must be an instance of the running environment of the current platform, and is used to automatically
     * {@link EventSubscription#close() unregister} the subscription when the running environment is shutdown.</p>
     *
     * @param instance      the instance of the environment IWM is running in (e.g. the plugin instance)
     * @param eventClass    the event class
     * @param handler       the event handler
     * @param <T>           the event type
     * @return an {@link EventSubscription} representing this subscription
     */
    <T extends IWMEvent> @NonNull EventSubscription<T> subscribe(Object instance, @NonNull Class<T> eventClass, @NonNull Consumer<? super T> handler);

    /**
     * Gets a set of all registered subscriptions for a given event.
     *
     * @param eventClass    the event to find subscriptions for
     * @param <T>           the event type
     * @return an immutable set of event subscriptions
     */
    <T extends IWMEvent> @NonNull @Unmodifiable Set<EventSubscription<T>> getSubscriptions(@NonNull Class<T> eventClass);

}
