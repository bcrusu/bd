package com.bcrusu.gitHubEvents.loader;

import com.bcrusu.gitHubEvents.loader.writer.ConsoleEventWriter;
import com.bcrusu.gitHubEvents.loader.writer.IEventWriter;
import com.bcrusu.gitHubEvents.loader.writer.KafkaEventWriter;

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
