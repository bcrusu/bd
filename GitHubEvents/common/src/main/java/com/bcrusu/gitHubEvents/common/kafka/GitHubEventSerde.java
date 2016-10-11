package com.bcrusu.gitHubEvents.common.kafka;

import com.bcrusu.gitHubEvents.common.gitHub.GitHubEvent;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class GitHubEventSerde implements Serde<GitHubEvent> {
    private final Serializer<GitHubEvent> serializer;
    private final Deserializer<GitHubEvent> deserializer;

    public GitHubEventSerde() {
        this.serializer = new GitHubEventSerializer();
        this.deserializer = new GitHubEventDeserializer();
    }

    public void configure(Map<String, ?> configs, boolean isKey) {
        this.serializer.configure(configs, isKey);
        this.deserializer.configure(configs, isKey);
    }

    public void close() {
        this.serializer.close();
        this.deserializer.close();
    }

    public Serializer<GitHubEvent> serializer() {
        return this.serializer;
    }

    public Deserializer<GitHubEvent> deserializer() {
        return this.deserializer;
    }
}