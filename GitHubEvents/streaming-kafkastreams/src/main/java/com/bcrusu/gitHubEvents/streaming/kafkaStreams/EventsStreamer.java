package com.bcrusu.gitHubEvents.streaming.kafkaStreams;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

class EventsStreamer implements AutoCloseable{
    private static final Logger _logger = LoggerFactory.getLogger(EventsStreamer.class);

    private final String _clientId;
    private final String _bootstrapServers;
    private final String _topic;
    private final boolean _seekToBeginning;
    private final String _stateDir;

    private KafkaStreams _streams;

    public EventsStreamer(String stateDir, String clientId, String bootstrapServers, String topic, boolean seekToBeginning) {
        if (stateDir == null) throw new IllegalArgumentException("stateDir");
        if (clientId == null) throw new IllegalArgumentException("clientId");
        if (bootstrapServers == null) throw new IllegalArgumentException("bootstrapServers");
        if (topic == null) throw new IllegalArgumentException("topic");

        _stateDir = stateDir;
        _clientId = clientId;
        _bootstrapServers = bootstrapServers;
        _topic = topic;
        _seekToBeginning = seekToBeginning;
    }

    public void start() {
        if (_streams != null)
            throw new IllegalStateException("Start already called.");

        KStreamBuilder builder = createStreamBuilder();
        Properties props = createStreamsProperties();

        _streams = new KafkaStreams(builder, props);
        _streams.cleanUp();
    }

    @Override
    public void close() throws Exception {
        if (_streams == null)
            return;

        _streams.close();
        _streams.cleanUp();
    }

    private KStreamBuilder createStreamBuilder() {
        KStreamBuilder builder = new KStreamBuilder();
        KStream<String, JsonNode> source = builder.stream(Serdes.String(), getJsonSerde(), _topic);

        //TODO: actual event counters (grouped by user/repo/org and windowed)
        KTable<String, Long> countsByKey = source.filter(Predicates::valueNotNull)
                .map(EventsStreamer::mapEventType)
                .countByKey("count");

        countsByKey.to(Serdes.String(), Serdes.Long(), "events-by-key");

        return builder;
    }

    private static Serde<JsonNode> getJsonSerde() {
        Serializer<JsonNode> jsonSerializer = new JsonSerializer();
        Deserializer<JsonNode> jsonDeserializer = new JsonDeserializer();
        return Serdes.serdeFrom(jsonSerializer, jsonDeserializer);
    }

    private static KeyValue<String, Long> mapEventType(String eventId, JsonNode node) {
        String eventType = node.get("type").textValue();
        return new KeyValue<>(eventType, 1L);
    }

    private Properties createStreamsProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "gitHubEvents-streaming-kafkastreams");
        props.put(StreamsConfig.CLIENT_ID_CONFIG, "gitHubEvents-streaming-kafkastreams-" + _clientId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, _bootstrapServers);
        //TODO: props.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, "localhost:2181");
        props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.STATE_DIR_CONFIG, _stateDir);

        if (_seekToBeginning)
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return props;
    }
}
