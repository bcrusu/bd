package com.bcrusu.gitHubEvents.indexer.writer;

import com.bcrusu.gitHubEvents.indexer.Event;

import java.io.Closeable;

public interface IEventWriter extends Closeable {
    void write(Event event);
}
