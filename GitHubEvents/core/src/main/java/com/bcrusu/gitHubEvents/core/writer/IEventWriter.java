package com.bcrusu.gitHubEvents.core.writer;

import com.bcrusu.gitHubEvents.core.api.GitHubEvent;

import java.io.Closeable;

public interface IEventWriter extends Closeable {
    void write(GitHubEvent event);
}
