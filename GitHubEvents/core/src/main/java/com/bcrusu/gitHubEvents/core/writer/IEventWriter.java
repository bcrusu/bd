package com.bcrusu.gitHubEvents.core.writer;

import com.bcrusu.gitHubEvents.core.api.GitHubEvent;

public interface IEventWriter {
    void write(GitHubEvent event);
}
