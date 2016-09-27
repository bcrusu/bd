package com.bcrusu.gitHubEvents.watcher.writer;

import com.bcrusu.gitHubEvents.core.api.GitHubEvent;
import com.bcrusu.gitHubEvents.core.writer.IEventWriter;

import java.io.IOException;

public class ConsoleEventWriter implements IEventWriter {
    @Override
    public void write(GitHubEvent event) {
        System.out.println(String.format("GitHub Event: id=%s, type=%s", event.getId(), event.getType()));
    }

    @Override
    public void close() throws IOException {
    }
}
