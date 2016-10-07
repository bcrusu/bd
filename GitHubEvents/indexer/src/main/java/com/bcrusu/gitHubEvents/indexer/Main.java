package com.bcrusu.gitHubEvents.indexer;

import com.bcrusu.gitHubEvents.indexer.writer.IEventWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger _logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        IndexerProperties properties = IndexerProperties.parse(args);
        if (!properties.validate()) {
            System.err.println("Invalid command line arguments.");
            System.exit(-1);
            return;
        }

        System.out.println("Indexing events. Press any key to exit...");

        try {
            try (IndexerEngine engine = createIndexerEngine(properties)) {
                _logger.info("running...");
                engine.run();

                System.in.read();
            }
        } catch (Exception e) {
            _logger.error("Unexpected error", e);
            System.exit(-2);
        }
    }

    private static IndexerEngine createIndexerEngine(IndexerProperties properties) {
        KafkaEventSource eventSource = createEventSource(properties);
        IEventWriter eventWriter = EventWriterFactory.create(properties);
        return new IndexerEngine(eventSource, eventWriter);
    }

    private static KafkaEventSource createEventSource(IndexerProperties properties) {
        String id = properties.getId();
        return new KafkaEventSource(id, properties.kafka, false);
    }
}
