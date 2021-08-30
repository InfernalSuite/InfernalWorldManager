package com.infernalsuite.worldmanager.api.world.property;

public interface SlimeProperty<T> extends SlimePropertyIdentifier<T> {

    T getValue();

    void setValue(T value);
}
