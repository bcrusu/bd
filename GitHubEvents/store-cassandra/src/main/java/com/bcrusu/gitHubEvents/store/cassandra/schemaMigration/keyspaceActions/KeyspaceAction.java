package com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.keyspaceActions;

import com.bcrusu.gitHubEvents.store.cassandra.CassandraUtils;
import com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.migrations.MigrationResourcesFactory;
import com.datastax.driver.core.Session;
import io.smartcat.migration.MigrationEngine;
import io.smartcat.migration.MigrationResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class KeyspaceAction {
    private final String _contactPointAddress;
    private final int _contactPointPort;
    protected final Logger logger;

    private Session _session;

    KeyspaceAction(String contactPointAddress, int contactPointPort) {
        _contactPointAddress = contactPointAddress;
        _contactPointPort = contactPointPort;
        logger = LoggerFactory.getLogger(this.getClass());
    }

    public final void execute(String keyspace) {
        if (_session != null)
            throw new IllegalStateException("Execution is in progress.");

        Session session = null;
        try {
            session = CassandraUtils.openSession(_contactPointAddress, _contactPointPort);
            _session = session;

            executeInternal(keyspace);
        } finally {
            _session = null;
            if (session != null)
                CassandraUtils.closeSession(session);
        }
    }

    protected abstract void executeInternal(String keyspace);

    protected Session getSession() {
        return _session;
    }

    protected void useKeyspace(String keyspace) {
        String statement = String.format("USE \"%s\"", keyspace);
        _session.execute(statement);
    }

    protected void createKeyspace(String keyspace) {
        String statement = String.format("CREATE KEYSPACE \"%s\" WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1 };", keyspace);

        logger.info("Creating keyspace '{}'", keyspace);
        _session.execute(statement);
    }

    protected boolean runMigrations() {
        MigrationResources resources = MigrationResourcesFactory.createMigrationResources();

        logger.info("Running migrations");
        return MigrationEngine.withSession(_session).migrate(resources);
    }

    protected void dropKeyspace(String keyspace) {
        String statement = String.format("DROP KEYSPACE IF EXISTS \"%s\";", keyspace);

        logger.info("Dropping keyspace '{}'", keyspace);
        _session.execute(statement);
    }
}
