package com.bcrusu.gitHubEvents.watcher;

import com.bcrusu.gitHubEvents.core.writer.IEventWriter;
import com.bcrusu.gitHubEvents.watcher.api.GitHubEventSource;

public class Main {
    public static void main(String[] args) throws Exception {
        CommandLineArgs commandLineArgs = CommandLineArgs.parse(args);
        if (commandLineArgs == null) {
            System.err.println("Invalid command line arguments.");
            CommandLineArgs.printHelp();
            System.exit(-1);
            return;
        }

        System.out.println("Watching GitHub events. Press any key to exit...");

        try {
            try (WatcherEngine engine = createWatcherEngine(commandLineArgs)) {
                engine.run();
                System.in.read();
            }
        } catch (Exception e) {
            System.err.println(e);
            System.exit(-2);
        }
    }

    private static WatcherEngine createWatcherEngine(CommandLineArgs args) {
        GitHubEventSource eventSource = createEventSource(args);
        IEventWriter eventWriter = EventWriterFactory.create(args);
        return new WatcherEngine(eventSource, eventWriter);
    }

    private static GitHubEventSource createEventSource(CommandLineArgs args) {
        String oauthToken = args.getOauth2Token();
        String url = args.getUrl();
        return new GitHubEventSource(oauthToken, url);
    }
}
