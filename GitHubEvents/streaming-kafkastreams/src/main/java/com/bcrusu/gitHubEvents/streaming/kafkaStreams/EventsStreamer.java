package com.bcrusu.gitHubEvents.streaming.kafkaStreams;

import com.bcrusu.gitHubEvents.common.cli.KafkaProperties;
import com.bcrusu.gitHubEvents.common.gitHub.GitHubEvent;
import com.bcrusu.gitHubEvents.common.kafka.GitHubEventSerde;
import com.bcrusu.gitHubEvents.common.store.IEventStoreWriter;
import com.bcrusu.gitHubEvents.common.store.WindowedEventType;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.TimeWindows;

import java.util.Properties;

class EventsStreamer implements AutoCloseable {
    private final String _clientId;
    private final String _stateDir;
    private final KafkaProperties _kafkaProperties;
    private final IEventStoreWriter _eventStoreWriter;

    private KafkaStreams _streams;

    public EventsStreamer(String stateDir, String clientId, KafkaProperties kafkaProperties, IEventStoreWriter eventStoreWriter) {
        if (stateDir == null) throw new IllegalArgumentException("stateDir");
        if (clientId == null) throw new IllegalArgumentException("clientId");
        if (kafkaProperties == null) throw new IllegalArgumentException("kafkaProperties");
        if (eventStoreWriter == null) throw new IllegalArgumentException("eventStoreWriter");

        _stateDir = stateDir;
        _clientId = clientId;
        _kafkaProperties = kafkaProperties;
        _eventStoreWriter = eventStoreWriter;
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

        // TODO: switch ctor. dependency to provider implementation. Should not close not-owned resource here
        _eventStoreWriter.close();
    }

    private KStreamBuilder createStreamBuilder() {
        Serde<GitHubEvent> valuesSerde = new GitHubEventSerde();
        KStreamBuilder builder = new KStreamBuilder();

        KStream<String, GitHubEvent> source = builder.stream(Serdes.String(), valuesSerde, _kafkaProperties.getTopic());

        TimeWindows eventsPerSecond = TimeWindows.of("eventsPerSecond", 1000L);
        TimeWindows eventsPerMinute = TimeWindows.of("eventsPerMinute", 60 * 1000L);

        addEventCounterStream(source, eventsPerSecond, _eventStoreWriter::writeEventsPerSecond);
        addEventCounterStream(source, eventsPerMinute, _eventStoreWriter::writeEventsPerMinute);

        return builder;
    }

    private static KeyValue<String, Long> mapEventType(String eventId, GitHubEvent event) {
        return new KeyValue<>(event.type, 1L);
    }

    private Properties createStreamsProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "gitHubEvents-streaming-kafkastreams");
        props.put(StreamsConfig.CLIENT_ID_CONFIG, "gitHubEvents-streaming-kafkastreams-" + _clientId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, _kafkaProperties.getBootstrapServers());
        props.put(StreamsConfig.STATE_DIR_CONFIG, _stateDir);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(StreamsConfig.TIMESTAMP_EXTRACTOR_CLASS_CONFIG, GitHubEventTimestampExtractor.class);

        return props;
    }

    private void addEventCounterStream(KStream<String, GitHubEvent> source, TimeWindows timeWindows,
                                       ForeachAction<WindowedEventType, Long> writerFunction) {
        KStream<WindowedEventType, Long> countsByKey = source
                .filter(Predicates::valueNotNull)
                .map(EventsStreamer::mapEventType)
                .countByKey(timeWindows)
                .toStream()
                .map((windowedKey, value) -> {
                    WindowedEventType newKey = new WindowedEventType(windowedKey.window().start(), windowedKey.key());
                    return new KeyValue<>(newKey, value);
                });

        countsByKey.foreach(writerFunction);
    }
}
