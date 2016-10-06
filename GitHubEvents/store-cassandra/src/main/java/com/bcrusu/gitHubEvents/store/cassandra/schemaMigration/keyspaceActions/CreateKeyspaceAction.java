package com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.keyspaceActions;

public class CreateKeyspaceAction extends KeyspaceAction {
    CreateKeyspaceAction(String contactPointAddress, int contactPointPort) {
        super(contactPointAddress, contactPointPort);
    }

    @Override
    public String description() {
        return "CREATE";
    }

    @Override
    protected boolean executeInternal(String keyspace) {
        createKeyspace(keyspace);
        useKeyspace(keyspace);
        return runMigrations();
    }
}
