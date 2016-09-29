package com.bcrusu.gitHubEvents.loader.writer;

import com.bcrusu.gitHubEvents.loader.api.GitHubEvent;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaEventWriter implements IEventWriter {
    private static final Logger _logger = LoggerFactory.getLogger(KafkaEventWriter.class);

    private final KafkaProducer<String, String> _producer;
    private final String _topic;

    public KafkaEventWriter(String bootstrapServers, String topic) {
        if (bootstrapServers == null) throw new IllegalArgumentException("bootstrapServers");
        if (topic == null) throw new IllegalArgumentException("topic");

        _producer = createKafkaProducer(bootstrapServers);
        _topic = topic;
    }

    @Override
    public void write(GitHubEvent event) {
        String eventId = event.getId();
        String eventBody = event.getJson();

        ProducerRecord<String, String> record = new ProducerRecord<>(_topic, eventId, eventBody);

        try {
            _producer.send(record).get();
        } catch (InterruptedException | ExecutionException e) {
            _logger.error("Error sending message", e);
        }
    }

    @Override
    public void close() throws Exception {
        _producer.close();
    }

    private static KafkaProducer<String, String> createKafkaProducer(String bootstrapServers) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "gitHubEvents-loader");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        //props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        props.put(ProducerConfig.RETRIES_CONFIG, "3");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "PLAINTEXT");

        return new KafkaProducer<>(props);
    }
}
