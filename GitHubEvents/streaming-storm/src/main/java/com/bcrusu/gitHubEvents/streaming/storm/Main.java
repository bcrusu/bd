package com.bcrusu.gitHubEvents.streaming.storm;

import org.apache.storm.generated.StormTopology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger _logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        AppProperties properties = AppProperties.parse(args);
        if (!properties.validate()) {
            System.err.println("Invalid command line arguments.");
            System.exit(-1);
            return;
        }

        System.out.println("Press any key to exit...");

        try {
            StormTopology topology = TopologyBuilder.build();
            TopologyRunner.run(topology);

            _logger.info("running...");
            System.in.read();
        } catch (Exception e) {
            _logger.error("Unexpected error", e);
            System.exit(-2);
        }
    }
}
