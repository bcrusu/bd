package com.bcrusu.gitHubEvents.streaming.kafkaStreams;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

import java.time.Instant;

public class GitHubEventTimestampExtractor implements TimestampExtractor {
    private final static String CREATED_AT_FIELD_NAME = "created_at";

    @Override
    public long extract(ConsumerRecord<Object, Object> consumerRecord) {
        Object value = consumerRecord.value();
        if (!(value instanceof JsonNode))
            return consumerRecord.timestamp();

        JsonNode jsonNode = (JsonNode) value;
        if (!jsonNode.has(CREATED_AT_FIELD_NAME))
            return consumerRecord.timestamp();

        String createdAtText = jsonNode.get(CREATED_AT_FIELD_NAME).textValue();
        return Instant.parse(createdAtText).toEpochMilli();
    }
}
