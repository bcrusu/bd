package com.bcrusu.gitHubEvents.indexer.writer;

import com.bcrusu.gitHubEvents.indexer.Event;

import java.io.Closeable;

public interface IEventWriter extends AutoCloseable {
    void write(Event event);
}
