package com.infernalsuite.iwm.api.event;

/**
 * Represents the order an event will be posted to a listener method, relative to other listeners to the same event.
 */
public enum PostOrder {

    FIRST, EARLY, NORMAL, LATE, LAST

}
