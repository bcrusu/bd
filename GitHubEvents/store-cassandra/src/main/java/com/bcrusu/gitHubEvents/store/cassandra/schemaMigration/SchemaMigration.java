package com.bcrusu.gitHubEvents.store.cassandra.schemaMigration;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import io.smartcat.migration.MigrationEngine;
import io.smartcat.migration.MigrationResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchemaMigration {
    private static final Logger _logger = LoggerFactory.getLogger(SchemaMigration.class);

    private String _contactPointAddress;
    private int _contactPointPort;
    private String _keyspace;

    public SchemaMigration(String contactPointAddress, int contactPointPort, String keyspace) {
        _contactPointAddress = contactPointAddress;
        _contactPointPort = contactPointPort;
        _keyspace = keyspace;
    }

    public boolean run() {
        _logger.info("Connecting to Cassandra...");

        try (Cluster cluster = createCluster();
             Session session = cluster.connect()) {
            // ensure keyspace exists before running the migration scripts
            ensureKeyspace(session);

            MigrationResources resources = MigrationResourcesFactory.createMigrationResources();

            _logger.info("Running migrations...");
            boolean result = MigrationEngine.withSession(session).migrate(resources);

            return result;
        }

    }

    private Cluster createCluster() {
        Cluster cluster = Cluster.builder()
                .addContactPoints(_contactPointAddress)
                .withPort(_contactPointPort)
                .build();

        return cluster;
    }

    private void ensureKeyspace(Session session) {
        String statement = String.format("CREATE KEYSPACE IF NOT EXISTS \"%s\" WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1 };", _keyspace);
        ResultSet resultSet = session.execute(statement); //TODO: check result
    }
}
