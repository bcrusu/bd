package com.bcrusu.gitHubEvents.streaming.kafkaStreams;

import com.bcrusu.gitHubEvents.common.store.IEventStoreWriter;
import com.bcrusu.gitHubEvents.store.cassandra.CassandraStoreWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    private static final Logger _logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        EventStreamerProperties properties = EventStreamerProperties.parse(args);
        if (!properties.validate()) {
            System.err.println("Invalid command line arguments.");
            System.exit(-1);
            return;
        }

        System.out.println("Press any key to exit...");

        try {
            try (EventsStreamer streamer = createStreamer(properties)) {
                _logger.info("running...");
                streamer.start();

                System.in.read();
            }
        } catch (Exception e) {
            _logger.error("Unexpected error", e);
            System.exit(-2);
        }
    }

    private static EventsStreamer createStreamer(EventStreamerProperties properties) {
        String stateDir = properties.getStateDir();
        String clientId = properties.getId();
        IEventStoreWriter writer = new CassandraStoreWriter(properties.cassandra);
        return new EventsStreamer(stateDir, clientId, properties.kafka, writer);
    }
}
