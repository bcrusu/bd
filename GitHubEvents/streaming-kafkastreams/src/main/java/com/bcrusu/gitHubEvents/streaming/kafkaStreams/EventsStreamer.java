package com.bcrusu.gitHubEvents.streaming.kafkaStreams;

import com.bcrusu.gitHubEvents.common.gitHub.GitHubEvent;
import com.bcrusu.gitHubEvents.common.kafka.GitHubEventSerdeFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

class EventsStreamer implements AutoCloseable {
    private static final Logger _logger = LoggerFactory.getLogger(EventsStreamer.class);

    private final String _clientId;
    private final String _bootstrapServers;
    private final String _topic;
    private final String _stateDir;

    private KafkaStreams _streams;

    public EventsStreamer(String stateDir, String clientId, String bootstrapServers, String topic) {
        if (stateDir == null) throw new IllegalArgumentException("stateDir");
        if (clientId == null) throw new IllegalArgumentException("clientId");
        if (bootstrapServers == null) throw new IllegalArgumentException("bootstrapServers");
        if (topic == null) throw new IllegalArgumentException("topic");

        _stateDir = stateDir;
        _clientId = clientId;
        _bootstrapServers = bootstrapServers;
        _topic = topic;
    }

    public void start() {
        if (_streams != null)
            throw new IllegalStateException("Start already called.");

        KStreamBuilder builder = createStreamBuilder();
        Properties props = createStreamsProperties();

        _streams = new KafkaStreams(builder, props);
        _streams.start();
    }

    @Override
    public void close() throws Exception {
        if (_streams == null)
            return;

        _streams.close();
        _streams.cleanUp();
    }

    private KStreamBuilder createStreamBuilder() {
        Serde<GitHubEvent> serde = GitHubEventSerdeFactory.getJsonSerde();
        KStreamBuilder builder = new KStreamBuilder();

        KStream<String, GitHubEvent> source = builder.stream(Serdes.String(), serde, _topic);

        TimeWindows timeWindows = TimeWindows.of("countBy5Seconds", 5 * 1000L);

        KStream<WindowedEventType, Long> countsByKey = source
                .filter(Predicates::valueNotNull)
                .map(EventsStreamer::mapEventType)
                .countByKey(timeWindows)
                .toStream()
                .map((windowedKey, value) -> {
                    WindowedEventType newKey = new WindowedEventType();
                    newKey.windowStart = windowedKey.window().start();
                    newKey.eventType = windowedKey.key();

                    return new KeyValue<>(newKey, value);
                });

        //TODO: ingest to cassandra/redis
        countsByKey.foreach((key, value) -> {
            System.out.println(String.format("Window: %d\tKey: %s\tCount: %d", key.windowStart, key.eventType, value));
        });

        return builder;
    }

    private static KeyValue<String, Long> mapEventType(String eventId, GitHubEvent event) {
        return new KeyValue<>(event.type, 1L);
    }

    private Properties createStreamsProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "gitHubEvents-streaming-kafkastreams");
        props.put(StreamsConfig.CLIENT_ID_CONFIG, "gitHubEvents-streaming-kafkastreams-" + _clientId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, _bootstrapServers);
        props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.STATE_DIR_CONFIG, _stateDir);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(StreamsConfig.TIMESTAMP_EXTRACTOR_CLASS_CONFIG, GitHubEventTimestampExtractor.class);

        return props;
    }

    private static class WindowedEventType {
        long windowStart;
        String eventType;
    }
}
