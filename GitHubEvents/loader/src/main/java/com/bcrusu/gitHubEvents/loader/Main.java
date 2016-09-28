package com.bcrusu.gitHubEvents.loader;

import com.bcrusu.gitHubEvents.loader.writer.IEventWriter;
import com.bcrusu.gitHubEvents.loader.api.GitHubEventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger _logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        CommandLineArgs commandLineArgs = CommandLineArgs.parse(args);
        if (commandLineArgs == null) {
            System.err.println("Invalid command line arguments.");
            CommandLineArgs.printHelp();
            System.exit(-1);
            return;
        }

        System.out.println("Loading GitHub events. Press any key to exit...");

        try {
            try (LoaderEngine engine = createLoaderEngine(commandLineArgs)) {
                _logger.info("running...");
                engine.run();

                System.in.read();
            }
        } catch (Exception e) {
            _logger.error("Unexpected error", e);
            System.exit(-2);
        }
    }

    private static LoaderEngine createLoaderEngine(CommandLineArgs args) {
        GitHubEventSource eventSource = createEventSource(args);
        IEventWriter eventWriter = EventWriterFactory.create(args);
        return new LoaderEngine(eventSource, eventWriter);
    }

    private static GitHubEventSource createEventSource(CommandLineArgs args) {
        String oauthToken = args.getOauth2Token();
        String url = args.getUrl();
        return new GitHubEventSource(oauthToken, url);
    }
}
