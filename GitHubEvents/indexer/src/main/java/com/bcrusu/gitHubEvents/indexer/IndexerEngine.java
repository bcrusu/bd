package com.bcrusu.gitHubEvents.indexer;

import com.bcrusu.gitHubEvents.indexer.writer.IEventWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Subscription;

class IndexerEngine implements AutoCloseable {
    private static final Logger _logger = LoggerFactory.getLogger(IndexerEngine.class);

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
    public void close() throws Exception {
        if (_subscription == null || _subscription.isUnsubscribed())
            return;

        _subscription.unsubscribe();
        _eventWriter.close();
    }

    private void processEvent(Event event) {
        _logger.debug("Processing event: {}", event.getId());

        _eventWriter.write(event);
    }
}
