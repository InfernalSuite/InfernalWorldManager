package com.infernalsuite.iwm.common.event;

import com.infernalsuite.iwm.api.event.EventSubscription;
import com.infernalsuite.iwm.api.event.IWMEvent;
import lombok.RequiredArgsConstructor;
import net.kyori.event.EventSubscriber;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class IWMEventSubscription<T extends IWMEvent> implements EventSubscription<T>, EventSubscriber<T> {

    private final @NonNull AbstractEventBus<?> eventBus;

    private final @NonNull Class<T> eventClass;

    private final @NonNull Consumer<? super T> handler;

    private final @Nullable Object instance;

    private final AtomicBoolean active = new AtomicBoolean(true);

    @Override
    public boolean isActive() {
        return this.active.get();
    }

    @Override
    public void close() {
        if (!this.active.getAndSet(false)) {
            return;
        }
        this.eventBus.unregisterHandler(this);
    }

    @Override
    public void invoke(@NonNull T event) throws Throwable {
        try {
            this.handler.accept(event);
        } catch (Throwable t) {
            // TODO: Logging
            t.printStackTrace();
        }
    }

    @Override
    public @NonNull Class<T> getEventClass() {
        return eventClass;
    }

    @Override
    public @NonNull Consumer<? super T> getHandler() {
        return handler;
    }

    public @Nullable Object getInstance() { return instance; }
}
