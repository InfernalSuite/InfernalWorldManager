package com.infernalsuite.iwm.common.event.gen;

import com.infernalsuite.iwm.api.InfernalWorldManager;
import com.infernalsuite.iwm.api.event.IWMEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.invoke.MethodHandles;

public abstract class AbstractEvent implements IWMEvent {

    private final InfernalWorldManager api;

    protected AbstractEvent(InfernalWorldManager api) { this.api = api; }

    @Override
    public @NonNull InfernalWorldManager getIWM() {
        return api;
    }

    // Overridden by the subclass.
    public MethodHandles.Lookup mhl() { throw new UnsupportedOperationException(); }
}
