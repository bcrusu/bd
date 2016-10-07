package com.bcrusu.gitHubEvents.loader;

import com.bcrusu.gitHubEvents.loader.api.GitHubEventSource;
import com.bcrusu.gitHubEvents.loader.writer.IEventWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger _logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        LoaderProperties properties = LoaderProperties.parse(args);
        if (!properties.validate()) {
            System.err.println("Invalid command line arguments.");
            System.exit(-1);
            return;
        }

        System.out.println("Loading GitHub events. Press any key to exit...");

        try {
            try (LoaderEngine engine = createLoaderEngine(properties)) {
                _logger.info("running...");
                engine.run();

                System.in.read();
            }
        } catch (Exception e) {
            _logger.error("Unexpected error", e);
            System.exit(-2);
        }
    }

    private static LoaderEngine createLoaderEngine(LoaderProperties properties) {
        GitHubEventSource eventSource = new GitHubEventSource(properties.gitHub);
        IEventWriter eventWriter = EventWriterFactory.create(properties);
        return new LoaderEngine(eventSource, eventWriter);
    }
}
