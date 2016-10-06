package com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.keyspaceActions;

public class CreateKeyspaceAction extends KeyspaceAction {
    CreateKeyspaceAction(String contactPointAddress, int contactPointPort) {
        super(contactPointAddress, contactPointPort);
    }

    @Override
    protected void executeInternal(String keyspace) {
        createKeyspace(keyspace);
        useKeyspace(keyspace);
        runMigrations();
    }
}
