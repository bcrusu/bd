package com.bcrusu.gitHubEvents.loader.writer;

import com.bcrusu.gitHubEvents.loader.api.GitHubEvent;

import java.io.Closeable;

public interface IEventWriter extends Closeable {
    void write(GitHubEvent event);
}
