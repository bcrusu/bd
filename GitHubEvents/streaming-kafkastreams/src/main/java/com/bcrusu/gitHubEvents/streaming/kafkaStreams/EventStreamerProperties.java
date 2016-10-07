package com.bcrusu.gitHubEvents.streaming.kafkaStreams;

import com.bcrusu.gitHubEvents.common.cli.CassandraProperties;
import com.bcrusu.gitHubEvents.common.cli.CliProperties;
import com.bcrusu.gitHubEvents.common.cli.KafkaProperties;

import java.util.Properties;

public class EventStreamerProperties extends CliProperties {
    private final static String PROPERTY_NAME_ID = "ghe.id";
    private final static String PROPERTY_NAME_STATE_DIR = "kafkaStreams.stateDir";
    private final static String PROPERTY_DEFAULT_STATE_DIR = "/tmp/kafka-streams";

    public final KafkaProperties kafka;
    public final CassandraProperties cassandra;

    protected EventStreamerProperties(Properties properties) {
        super(properties);
        kafka = new KafkaProperties(properties);
        cassandra = new CassandraProperties(properties);
    }

    @Override
    public boolean validate() {
        return cassandra.validate() && kafka.validate() &&
                hasValidStringProperty(PROPERTY_NAME_ID);
    }

    public static EventStreamerProperties parse(String[] args) {
        return (EventStreamerProperties) CliProperties.parse(args, EventStreamerProperties.class);
    }

    public String getId() {
        return getStringProperty(PROPERTY_NAME_ID);
    }

    public String getStateDir() {
        return getStringProperty(PROPERTY_NAME_STATE_DIR, PROPERTY_DEFAULT_STATE_DIR);
    }
}
