package com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.keyspaceActions;

class MigrateKeyspaceAction extends KeyspaceAction {
    MigrateKeyspaceAction(String contactPointAddress, int contactPointPort) {
        super(contactPointAddress, contactPointPort);
    }

    public void executeInternal(String keyspace) {
        useKeyspace(keyspace);
        runMigrations();
    }
}
