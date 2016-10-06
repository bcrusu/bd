package com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.keyspaceActions;

public class RecreateKeyspaceAction extends KeyspaceAction {
    RecreateKeyspaceAction(String contactPointAddress, int contactPointPort) {
        super(contactPointAddress, contactPointPort);
    }

    @Override
    public String description() {
        return "RECREATE";
    }

    @Override
    protected boolean executeInternal(String keyspace) {
        dropKeyspace(keyspace);
        createKeyspace(keyspace);
        useKeyspace(keyspace);
        return runMigrations();
    }
}
