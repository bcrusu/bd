package com.bcrusu.gitHubEvents.loader.writer;

import com.bcrusu.gitHubEvents.loader.api.GitHubEvent;
import com.bcrusu.gitHubEvents.loader.writer.IEventWriter;

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
