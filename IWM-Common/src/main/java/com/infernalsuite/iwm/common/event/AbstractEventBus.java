package com.infernalsuite.iwm.common.event;

import com.infernalsuite.iwm.api.event.EventBus;
import com.infernalsuite.iwm.api.event.EventSubscription;
import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.common.api.IWMApi;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.event.EventSubscriber;
import net.kyori.event.SimpleEventBus;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class AbstractEventBus<I> implements EventBus, AutoCloseable {

    @Getter
    private final IWMApi api;

    private final Bus bus = new Bus();

    /**
     * Checks that the given instance object is a valid instance for the platform IWM is running on.
     *
     * @param instance the object to check
     * @return the instance object, in the platform's type
     * @throws IllegalArgumentException if the given instance is invalid
     */
    protected abstract I checkInstance(@NonNull Object instance) throws IllegalArgumentException;

    public void post(@NonNull IWMEvent event) {
        this.bus.post(event);
    }

    // No IDEA, be quiet please
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean shouldPost(@NonNull Class<? extends IWMEvent> eventClass) {
        return this.bus.hasSubscribers(eventClass);
    }

    @Override
    public @NonNull <T extends IWMEvent> EventSubscription<T> subscribe(@NonNull Class<T> eventClass, @NonNull Consumer<? super T> handler) {
        Objects.requireNonNull(eventClass, "EventClass cannot be null");
        Objects.requireNonNull(handler, "Handler cannot be null");
        return registerSubscription(eventClass, handler, null);
    }

    @Override
    public @NonNull <T extends IWMEvent> EventSubscription<T> subscribe(Object instance, @NonNull Class<T> eventClass, @NonNull Consumer<? super T> handler) {
        Objects.requireNonNull(instance, "Given instance cannot be null");
        Objects.requireNonNull(eventClass, "EventClass cannot be null");
        Objects.requireNonNull(handler, "Handler cannot be null");
        return registerSubscription(eventClass, handler, instance);
    }

    private <T extends IWMEvent> EventSubscription<T> registerSubscription(@NonNull Class<T> eventClass, @NonNull Consumer<? super T> handler, @Nullable Object instance) {
        if (!eventClass.isInterface()) {
            throw new IllegalArgumentException("Class " + eventClass + " is not an interface!");
        }
        if (!IWMEvent.class.isAssignableFrom(eventClass)) {
            throw new IllegalArgumentException("Class " + eventClass.getName() + " does not implement IWMEvent!");
        }

        IWMEventSubscription<T> eventSubscription = new IWMEventSubscription<>(this, eventClass, handler, instance);
        this.bus.register(eventClass, eventSubscription);

        return eventSubscription;
    }

    @Override
    public @NonNull @Unmodifiable <T extends IWMEvent> Set<EventSubscription<T>> getSubscriptions(@NonNull Class<T> eventClass) {
        return this.bus.getHandlers(eventClass);
    }

    public void unregisterHandler(IWMEventSubscription<?> handler) { this.bus.unregister(handler); }

    protected void unregisterHandlers(I instance) {
        this.bus.unregister(s -> ((IWMEventSubscription<?>) s).getInstance() == instance);
    }

    @Override
    public void close() {
        this.bus.unregisterAll();
    }

    private static final class Bus extends SimpleEventBus<IWMEvent> {

        Bus() { super(IWMEvent.class); }

        @Override
        protected boolean shouldPost(@NonNull IWMEvent event, @NonNull EventSubscriber<?> subscriber) {
            return true;
        }

        public <T extends IWMEvent> Set<EventSubscription<T>> getHandlers(Class<T> eventClass) {
            //noinspection unchecked
            return super.subscribers().values().stream()
                    .filter(s -> s instanceof EventSubscription && ((EventSubscription<?>) s).getEventClass().isAssignableFrom(eventClass))
                    .map(s -> (EventSubscription<T>) s)
                    .collect(Collectors.toSet());
        }
    }

}
