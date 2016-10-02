package com.bcrusu.gitHubEvents.common.kafka;

import com.bcrusu.gitHubEvents.common.gitHub.GitHubEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class GitHubEventSerializer implements Serializer<GitHubEvent> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GitHubEventSerializer() {
    }

    public void configure(Map<String, ?> config, boolean isKey) {
    }

    public byte[] serialize(String topic, GitHubEvent data) {
        if (data == null) {
            return null;
        }

        try {
            return this.objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }

    public void close() {
    }
}