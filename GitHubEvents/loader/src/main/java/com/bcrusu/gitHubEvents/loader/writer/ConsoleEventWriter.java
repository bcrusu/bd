package com.bcrusu.gitHubEvents.loader.writer;


import com.bcrusu.gitHubEvents.common.gitHub.GitHubEvent;

public class ConsoleEventWriter implements IEventWriter {
    @Override
    public void write(GitHubEvent event) {
        System.out.println(String.format("GitHub Event: id=%s, type=%s", event.id, event.type));
    }

    @Override
    public void close() throws Exception {
    }
}
