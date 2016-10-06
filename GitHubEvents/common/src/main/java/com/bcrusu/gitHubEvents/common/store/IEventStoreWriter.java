package com.bcrusu.gitHubEvents.common.store;

public interface IEventStoreWriter extends AutoCloseable {
    void writeEventsPerSecond(WindowedEventType windowedEventType, long count);

    void writeEventsPerMinute(WindowedEventType windowedEventType, long count);
}
