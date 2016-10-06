package com.bcrusu.gitHubEvents.common.store;

public class WindowedEventType {
    public final long windowStart;
    public final String eventType;

    public WindowedEventType(long windowStart, String eventType) {
        this.windowStart = windowStart;
        this.eventType = eventType;
    }
}
