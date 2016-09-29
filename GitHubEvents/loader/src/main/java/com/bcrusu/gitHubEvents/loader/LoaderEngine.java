package com.bcrusu.gitHubEvents.loader;

import com.bcrusu.gitHubEvents.loader.api.GitHubEvent;
import com.bcrusu.gitHubEvents.loader.api.GitHubEventSource;
import com.bcrusu.gitHubEvents.loader.writer.IEventWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Subscription;

class LoaderEngine implements AutoCloseable {
    private static final Logger _logger = LoggerFactory.getLogger(LoaderEngine.class);

    private final GitHubEventSource _eventSource;
    private final IEventWriter _eventWriter;
    private Subscription _subscription;

    public LoaderEngine(GitHubEventSource eventSource, IEventWriter eventWriter) {
        _eventSource = eventSource;
        _eventWriter = eventWriter;
    }

    public void run() {
        if (_subscription != null)
            throw new IllegalStateException("Already running.");

        Observable<GitHubEvent> observable = _eventSource.getObservable();
        _subscription = observable.subscribe(this::processEvent);
    }

    @Override
    public void close() throws Exception {
        if (_subscription == null || _subscription.isUnsubscribed())
            return;

        _subscription.unsubscribe();
        _eventWriter.close();
    }

    private void processEvent(GitHubEvent event) {
        _logger.debug("Processing event: {}", event.getId());

        _eventWriter.write(event);
    }
}
