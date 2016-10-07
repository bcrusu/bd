package com.bcrusu.gitHubEvents.store.cassandra.schemaMigration;

import com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.keyspaceActions.KeyspaceAction;
import com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.keyspaceActions.KeyspaceActionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger _logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        SchemaMigrationProperties properties = SchemaMigrationProperties.parse(args);
        if (!properties.validate()) {
            System.err.println("Invalid command line arguments.");
            System.exit(-1);
            return;
        }

        boolean success = false;
        try {
            KeyspaceAction action = KeyspaceActionFactory.create(properties.getAction(), properties.cassandra);

            _logger.info("Running keyspace action '{}'...", action.description());

            String keyspace = properties.cassandra.getKeyspace();
            success = action.execute(keyspace);
        } catch (Exception e) {
            _logger.error("Unexpected error", e);
            System.exit(-2);
        }

        if (!success) {
            _logger.error("Migration not successful");
            System.exit(-3);
        }
    }
}
