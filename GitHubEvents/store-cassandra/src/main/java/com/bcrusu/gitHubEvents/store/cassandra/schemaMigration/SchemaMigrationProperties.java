package com.bcrusu.gitHubEvents.store.cassandra.schemaMigration;

import com.bcrusu.gitHubEvents.common.cli.CassandraProperties;
import com.bcrusu.gitHubEvents.common.cli.CliProperties;

import java.util.Properties;

public class SchemaMigrationProperties extends CliProperties {
    private final static String PROPERTY_NAME_ACTION = "ghe.action";
    private final static String PROPERTY_DEFAULT_ACTION = "migrate";

    public final CassandraProperties cassandra;

    protected SchemaMigrationProperties(Properties properties) {
        super(properties);
        cassandra = new CassandraProperties(properties);
    }

    @Override
    public boolean validate() {
        return cassandra.validate();
    }

    public static SchemaMigrationProperties parse(String[] args) {
        return (SchemaMigrationProperties) CliProperties.parse(args, SchemaMigrationProperties.class);
    }

    public String getAction() {
        return getStringProperty(PROPERTY_NAME_ACTION, PROPERTY_DEFAULT_ACTION);
    }
}
