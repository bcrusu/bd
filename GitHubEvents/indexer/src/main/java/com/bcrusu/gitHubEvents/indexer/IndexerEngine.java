package com.bcrusu.gitHubEvents.indexer;

import com.bcrusu.gitHubEvents.indexer.writer.IEventWriter;
import rx.Observable;
import rx.Subscription;

import java.io.Closeable;
import java.io.IOException;

class IndexerEngine implements Closeable {
    private final KafkaEventSource _eventSource;
    private final IEventWriter _eventWriter;
    private Subscription _subscription;

    public IndexerEngine(KafkaEventSource eventSource, IEventWriter eventWriter) {
        _eventSource = eventSource;
        _eventWriter = eventWriter;
    }

    public void run() {
        if (_subscription != null)
            throw new IllegalStateException("Already running.");

        Observable<Event> observable = _eventSource.getObservable();
        _subscription = observable.subscribe(this::processEvent);
    }

    @Override
    public void close() throws IOException {
        if (_subscription == null || _subscription.isUnsubscribed())
            return;

        _subscription.unsubscribe();
        _eventWriter.close();
    }

    private void processEvent(Event event) {
        _eventWriter.write(event);
    }
}
