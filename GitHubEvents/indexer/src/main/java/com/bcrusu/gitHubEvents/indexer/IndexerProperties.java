package com.bcrusu.gitHubEvents.indexer;

import com.bcrusu.gitHubEvents.common.cli.CliProperties;
import com.bcrusu.gitHubEvents.common.cli.ElasticsearchProperties;
import com.bcrusu.gitHubEvents.common.cli.KafkaProperties;

import java.util.Properties;

public class IndexerProperties extends CliProperties {
    private final static String PROPERTY_NAME_ID = "ghe.id";
    private final static String PROPERTY_NAME_EVENT_WRITER_TYPE = "ghe.event_writer";
    private final static String PROPERTY_DEFAULT_EVENT_WRITER_TYPE = EventWriterFactory.EVENT_WRITER_TYPE_ELASTICSEARCH;

    public final KafkaProperties kafka;
    public final ElasticsearchProperties elasticsearch;

    protected IndexerProperties(Properties properties) {
        super(properties);
        kafka = new KafkaProperties(properties);
        elasticsearch = new ElasticsearchProperties(properties);
    }

    @Override
    public boolean validate() {
        return elasticsearch.validate() && kafka.validate() &&
                hasValidStringProperty(PROPERTY_NAME_ID);
    }

    public static IndexerProperties parse(String[] args) {
        return (IndexerProperties) CliProperties.parse(args, IndexerProperties.class);
    }

    public String getId() {
        return getStringProperty(PROPERTY_NAME_ID);
    }

    public String getEventWriterType() {
        return getStringProperty(PROPERTY_NAME_EVENT_WRITER_TYPE, PROPERTY_DEFAULT_EVENT_WRITER_TYPE);
    }
}
