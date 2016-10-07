package com.bcrusu.gitHubEvents.common.cli;

import java.util.Properties;

public class CassandraProperties extends CliProperties {
    private final static String PROPERTY_NAME_CONTACT_POINTS = "cassandra.contactPoints";
    private final static String PROPERTY_NAME_PORT = "cassandra.port";
    private final static String PROPERTY_NAME_KEYSPACE = "cassandra.keyspace";
    private final static String PROPERTY_DEFAULT_CONTACT_POINTS = "localhost";
    private final static int PROPERTY_DEFAULT_PORT = 9042;
    private final static String PROPERTY_DEFAULT_KEYSPACE = "gitHubEvents";

    public CassandraProperties(Properties properties) {
        super(properties);
    }

    @Override
    public boolean validate() {
        return hasValidIntProperty(PROPERTY_NAME_PORT);
    }

    public String getContactPoints() {
        return getStringProperty(PROPERTY_NAME_CONTACT_POINTS, PROPERTY_DEFAULT_CONTACT_POINTS);
    }

    public int getPort() {
        return getIntProperty(PROPERTY_NAME_PORT, PROPERTY_DEFAULT_PORT);
    }

    public String getKeyspace() {
        return getStringProperty(PROPERTY_NAME_KEYSPACE, PROPERTY_DEFAULT_KEYSPACE);
    }
}
