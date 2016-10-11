package com.bcrusu.gitHubEvents.streaming.storm;

import com.bcrusu.gitHubEvents.common.cli.CassandraProperties;
import com.bcrusu.gitHubEvents.common.cli.CliProperties;
import com.bcrusu.gitHubEvents.common.cli.KafkaProperties;

import java.util.Properties;

public class AppProperties extends CliProperties {
    private final static String PROPERTY_NAME_TOPOLOGY_RUNNER_TYPE = "ghe.topologyRunnerType";
    private final static String PROPERTY_DEFAULT_RUNNER_TYPE = TopologyRunner.TOPOLOGY_RUNNER_TYPE_LOCAL;

    public final KafkaProperties kafka;
    public final CassandraProperties cassandra;

    protected AppProperties(Properties properties) {
        super(properties);
        kafka = new KafkaProperties(properties);
        cassandra = new CassandraProperties(properties);
    }

    @Override
    public boolean validate() {
        return cassandra.validate() && kafka.validate();
    }

    public static AppProperties parse(String[] args) {
        return (AppProperties) CliProperties.parse(args, AppProperties.class);
    }

    public String getTopologyRunnerType() {
        return getStringProperty(PROPERTY_NAME_TOPOLOGY_RUNNER_TYPE, PROPERTY_DEFAULT_RUNNER_TYPE);
    }
}
