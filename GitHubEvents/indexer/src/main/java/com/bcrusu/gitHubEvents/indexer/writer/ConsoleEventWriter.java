package com.bcrusu.gitHubEvents.indexer.writer;

import com.bcrusu.gitHubEvents.indexer.Event;

public class ConsoleEventWriter implements IEventWriter {
    @Override
    public void write(Event event) {
        System.out.println(String.format("Event: id=%s", event.getId()));
    }

    @Override
    public void close() throws Exception {
    }
}
