package com.bcrusu.gitHubEvents.indexer;

import com.bcrusu.gitHubEvents.indexer.writer.IEventWriter;

public class Main {
    public static void main(String[] args) throws Exception {
        CommandLineArgs commandLineArgs = CommandLineArgs.parse(args);
        if (commandLineArgs == null) {
            System.err.println("Invalid command line arguments.");
            CommandLineArgs.printHelp();
            System.exit(-1);
            return;
        }

        System.out.println("Indexing events. Press any key to exit...");

        try {
            try (IndexerEngine engine = createIndexerEngine(commandLineArgs)) {
                engine.run();
                System.in.read();
            }
        } catch (Exception e) {
            System.err.println(e);
            System.exit(-2);
        }
    }

    private static IndexerEngine createIndexerEngine(CommandLineArgs args) {
        KafkaEventSource eventSource = createEventSource(args);
        IEventWriter eventWriter = EventWriterFactory.create(args);
        return new IndexerEngine(eventSource, eventWriter);
    }

    private static KafkaEventSource createEventSource(CommandLineArgs args) {
        String id = args.getId();
        String bootstrapServers = args.getKafkaServer();
        String topic = args.getKafkaTopic();

        return new KafkaEventSource(id, bootstrapServers, topic);
    }
}
