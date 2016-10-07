package com.bcrusu.gitHubEvents.common.cli;

import java.util.Properties;

public class ElasticsearchProperties extends CliProperties {
    private final static String PROPERTY_NAME_ADDRESS = "elasticsearch.address";
    private final static String PROPERTY_NAME_PORT = "elasticsearch.port";
    private final static String PROPERTY_NAME_CLUSTER = "elasticsearch.cluster";
    private final static String PROPERTY_NAME_INDEX = "elasticsearch.index";
    private final static String PROPERTY_DEFAULT_ADDRESS = "localhost";
    private final static int PROPERTY_DEFAULT_PORT = 9300;
    private final static String PROPERTY_DEFAULT_CLUSTER = "elasticsearch";
    private final static String PROPERTY_DEFAULT_INDEX = "gitHubEvents";

    public ElasticsearchProperties(Properties properties) {
        super(properties);
    }

    @Override
    public boolean validate() {
        return hasValidIntProperty(PROPERTY_NAME_PORT);
    }

    public String getAddress() {
        return getStringProperty(PROPERTY_NAME_ADDRESS, PROPERTY_DEFAULT_ADDRESS);
    }

    public int getPort() {
        return getIntProperty(PROPERTY_NAME_PORT, PROPERTY_DEFAULT_PORT);
    }

    public String getCluster() {
        return getStringProperty(PROPERTY_NAME_CLUSTER, PROPERTY_DEFAULT_CLUSTER);
    }

    public String getIndex() {
        return getStringProperty(PROPERTY_NAME_INDEX, PROPERTY_DEFAULT_INDEX);
    }
}
