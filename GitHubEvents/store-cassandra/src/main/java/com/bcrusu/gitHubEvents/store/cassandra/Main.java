package com.bcrusu.gitHubEvents.store.cassandra;

import com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.SchemaMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger _logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        CommandLineArgs commandLineArgs = CommandLineArgs.parse(args);
        if (commandLineArgs == null) {
            System.err.println("Invalid command line arguments.");
            CommandLineArgs.printHelp();
            System.exit(-1);
            return;
        }

        boolean result = false;
        try {
            _logger.info("Running schema migration...");
            SchemaMigration schemaMigrator = createSchemaMigrator(commandLineArgs);
            result = schemaMigrator.run();
        } catch (Exception e) {
            _logger.error("Unexpected error", e);
            System.exit(-2);
        }

        if (!result) {
            _logger.error("Schema migration failed.");
            System.exit(-3);
        }
    }

    private static SchemaMigration createSchemaMigrator(CommandLineArgs args) {
        String address = args.getServerAddress();
        int port = args.getServerPort();
        String keyspace = args.getKeyspace();
        return new SchemaMigration(address, port, keyspace);
    }
}
