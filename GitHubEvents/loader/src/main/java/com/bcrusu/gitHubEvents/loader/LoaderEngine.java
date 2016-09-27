package com.bcrusu.gitHubEvents.loader;

import com.bcrusu.gitHubEvents.loader.api.GitHubEvent;
import com.bcrusu.gitHubEvents.loader.writer.IEventWriter;
import com.bcrusu.gitHubEvents.loader.api.GitHubEventSource;
import rx.Observable;
import rx.Subscription;

import java.io.Closeable;
import java.io.IOException;

class LoaderEngine implements Closeable {
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
    public void close() throws IOException {
        if (_subscription == null || _subscription.isUnsubscribed())
            return;

        _subscription.unsubscribe();
        _eventWriter.close();
    }

    private void processEvent(GitHubEvent event) {
        _eventWriter.write(event);
    }
}
