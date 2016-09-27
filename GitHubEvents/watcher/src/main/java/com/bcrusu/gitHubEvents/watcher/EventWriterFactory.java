package com.bcrusu.gitHubEvents.watcher;

import com.bcrusu.gitHubEvents.core.writer.IEventWriter;
import com.bcrusu.gitHubEvents.watcher.writer.ConsoleEventWriter;
import com.bcrusu.gitHubEvents.writer.kafka.KafkaEventWriter;


class EventWriterFactory {
    public final static String EVENT_WRITER_TYPE_KAFKA = "kafka";
    public final static String EVENT_WRITER_TYPE_CONSOLE = "console";

    public static IEventWriter create(CommandLineArgs args) {
        String eventWriterType = args.getEventWriterType();

        switch (eventWriterType.toLowerCase()) {
            case EVENT_WRITER_TYPE_KAFKA:
                String bootstrapServers = args.getKafkaServer();
                String topic = args.getKafkaTopic();
                return new KafkaEventWriter(bootstrapServers, topic);
            case EVENT_WRITER_TYPE_CONSOLE:
                return new ConsoleEventWriter();
            default:
                throw new IllegalArgumentException("Unrecognized event writer type: " + eventWriterType);
        }
    }
}
