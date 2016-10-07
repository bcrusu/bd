package com.bcrusu.gitHubEvents.common.cli;

import java.util.Properties;

public class KafkaProperties extends CliProperties {
    private final static String PROPERTY_NAME_BOOTSTRAP_SERVERS = "kafka.bootstrapServers";
    private final static String PROPERTY_NAME_TOPIC = "kafka.topic";
    private final static String PROPERTY_DEFAULT_BOOTSTRAP_SERVERS = "localhost:9092";
    private final static String PROPERTY_DEFAULT_TOPIC = "gitHubEvents";

    public KafkaProperties(Properties properties) {
        super(properties);
    }

    @Override
    public boolean validate() {
        return true;
    }

    public String getBootstrapServers() {
        return getStringProperty(PROPERTY_NAME_BOOTSTRAP_SERVERS, PROPERTY_DEFAULT_BOOTSTRAP_SERVERS);
    }

    public String getTopic() {
        return getStringProperty(PROPERTY_NAME_TOPIC, PROPERTY_DEFAULT_TOPIC);
    }
}
