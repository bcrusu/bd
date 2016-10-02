package com.bcrusu.gitHubEvents.common.kafka;

import com.bcrusu.gitHubEvents.common.gitHub.GitHubEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class GitHubEventDeserializer implements Deserializer<GitHubEvent> {
    private ObjectMapper objectMapper = new ObjectMapper();

    public GitHubEventDeserializer() {
    }

    public void configure(Map<String, ?> props, boolean isKey) {
    }

    public GitHubEvent deserialize(String topic, byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        try {
            return this.objectMapper.readValue(bytes, GitHubEvent.class);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }

    public void close() {
    }
}
