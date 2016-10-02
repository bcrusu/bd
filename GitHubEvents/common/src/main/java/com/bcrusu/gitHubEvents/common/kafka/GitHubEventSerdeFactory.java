package com.bcrusu.gitHubEvents.common.kafka;

import com.bcrusu.gitHubEvents.common.gitHub.GitHubEvent;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class GitHubEventSerdeFactory {
    public static Serde<GitHubEvent> getJsonSerde() {
        GitHubEventSerializer jsonSerializer = new GitHubEventSerializer();
        GitHubEventDeserializer jsonDeserializer = new GitHubEventDeserializer();
        return Serdes.serdeFrom(jsonSerializer, jsonDeserializer);
    }
}
