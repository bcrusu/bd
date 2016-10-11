package com.bcrusu.gitHubEvents.streaming.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;

abstract class TopologyRunner {
    public static final String TOPOLOGY_NAME = "gitHubEvents";
    public final static String TOPOLOGY_RUNNER_TYPE_LOCAL = "local";
    public final static String TOPOLOGY_RUNNER_TYPE_REMOTE = "remote";

    public static void run(StormTopology topology) {
        // TODO:
        TopologyRunner runner = createTopologyRunner(null);
        Config config = new Config();

        runner.submit(topology, config);
    }

    protected abstract void submit(StormTopology topology, Config config);

    private static TopologyRunner createTopologyRunner(String type) {
        switch (type.toLowerCase()) {
            case TOPOLOGY_RUNNER_TYPE_LOCAL:
                return new LocalTopologyRunner();
            case TOPOLOGY_RUNNER_TYPE_REMOTE:
                return new RemoteTopologyRunner();
            default:
                throw new IllegalArgumentException("Unrecognized topology runner: " + type);
        }
    }

    private static class LocalTopologyRunner extends TopologyRunner {
        @Override
        protected void submit(StormTopology topology, Config config) {
            LocalCluster cluster = new LocalCluster();
            config.setDebug(true);

            cluster.submitTopology(TOPOLOGY_NAME, config, topology);
        }
    }

    private static class RemoteTopologyRunner extends TopologyRunner {
        @Override
        protected void submit(StormTopology topology, Config config) {
            try {
                StormSubmitter.submitTopology(TOPOLOGY_NAME, config, topology);
            } catch (Exception e) {
                throw new TopologyRunnerException(e);
            }
        }
    }
}
