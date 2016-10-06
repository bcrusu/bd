package com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.keyspaceActions;

class MigrateKeyspaceAction extends KeyspaceAction {
    MigrateKeyspaceAction(String contactPointAddress, int contactPointPort) {
        super(contactPointAddress, contactPointPort);
    }

    @Override
    public String description() {
        return "MIGRATE";
    }

    public boolean executeInternal(String keyspace) {
        useKeyspace(keyspace);
        return runMigrations();
    }
}
