package com.bcrusu.gitHubEvents.loader.writer;

import com.bcrusu.gitHubEvents.common.gitHub.GitHubEvent;

public interface IEventWriter extends AutoCloseable {
    void write(GitHubEvent event);
}
