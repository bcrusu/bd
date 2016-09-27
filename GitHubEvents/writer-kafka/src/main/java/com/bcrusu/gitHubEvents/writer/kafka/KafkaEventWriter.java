package com.bcrusu.gitHubEvents.writer.kafka;

import com.bcrusu.gitHubEvents.core.api.GitHubEvent;
import com.bcrusu.gitHubEvents.core.writer.IEventWriter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaEventWriter implements IEventWriter {
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
            //TODO: log
        }
    }

    @Override
    public void close() throws IOException {
        _producer.close();
    }

    private static KafkaProducer<String, String> createKafkaProducer(String bootstrapServers) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("client.id", "gitHubEvents-writer-kafka");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "1");
        //props.put("compression.type", "gzip");
        props.put("retries", "3");
        props.put("security.protocol", "PLAINTEXT");

        return new KafkaProducer<>(props);
    }
}
