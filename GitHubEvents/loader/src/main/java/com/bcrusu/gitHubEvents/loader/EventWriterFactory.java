package com.bcrusu.gitHubEvents.loader;

import com.bcrusu.gitHubEvents.loader.writer.ConsoleEventWriter;
import com.bcrusu.gitHubEvents.loader.writer.IEventWriter;
import com.bcrusu.gitHubEvents.loader.writer.KafkaEventWriter;

class EventWriterFactory {
    public final static String EVENT_WRITER_TYPE_KAFKA = "kafka";
    public final static String EVENT_WRITER_TYPE_CONSOLE = "console";

    public static IEventWriter create(LoaderProperties properties) {
        String eventWriterType = properties.getEventWriterType();

        switch (eventWriterType.toLowerCase()) {
            case EVENT_WRITER_TYPE_KAFKA:
                String bootstrapServers = properties.kafka.getBootstrapServers();
                String topic = properties.kafka.getTopic();
                return new KafkaEventWriter(bootstrapServers, topic);
            case EVENT_WRITER_TYPE_CONSOLE:
                return new ConsoleEventWriter();
            default:
                throw new IllegalArgumentException("Unrecognized event writer type: " + eventWriterType);
        }
    }
}
