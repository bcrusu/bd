package com.bcrusu.gitHubEvents.streaming.kafkaStreams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger _logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        CommandLineArgs commandLineArgs = CommandLineArgs.parse(args);
        if (commandLineArgs == null) {
            System.err.println("Invalid command line arguments.");
            CommandLineArgs.printHelp();
            System.exit(-1);
            return;
        }

        System.out.println("Press any key to exit...");

        try {
            try (EventsStreamer streamer = createStreamer(commandLineArgs)) {
                _logger.info("running...");
                streamer.start();

                System.in.read();
            }
        } catch (Exception e) {
            _logger.error("Unexpected error", e);
            System.exit(-2);
        }
    }

    private static EventsStreamer createStreamer(CommandLineArgs args) {
        String stateDir = args.getKafkaStreamsStateDir();
        String clientId = args.getId();
        String bootstrapServers = args.getKafkaServer();
        String topic = args.getKafkaTopic();
        boolean seekToBeginning = args.getKafkaSeekToBeginning();

        return new EventsStreamer(stateDir, clientId, bootstrapServers, topic, seekToBeginning);
    }
}
