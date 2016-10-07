package com.bcrusu.gitHubEvents.loader;

import com.bcrusu.gitHubEvents.common.cli.*;

import java.util.Properties;

public class LoaderProperties extends CliProperties {
    private final static String PROPERTY_NAME_EVENT_WRITER_TYPE = "ghe.event_writer";
    private final static String PROPERTY_DEFAULT_EVENT_WRITER_TYPE = EventWriterFactory.EVENT_WRITER_TYPE_KAFKA;

    public final KafkaProperties kafka;
    public final GitHubApiProperties gitHub;

    protected LoaderProperties(Properties properties) {
        super(properties);
        kafka = new KafkaProperties(properties);
        gitHub = new GitHubApiProperties(properties);
    }

    @Override
    public boolean validate() {
        return kafka.validate() && gitHub.validate();
    }

    public static LoaderProperties parse(String[] args) {
        return (LoaderProperties) CliProperties.parse(args, LoaderProperties.class);
    }

    public String getEventWriterType() {
        return getStringProperty(PROPERTY_NAME_EVENT_WRITER_TYPE, PROPERTY_DEFAULT_EVENT_WRITER_TYPE);
    }
}
