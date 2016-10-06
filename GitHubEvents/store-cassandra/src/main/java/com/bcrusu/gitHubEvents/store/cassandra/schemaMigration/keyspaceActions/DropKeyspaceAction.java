package com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.keyspaceActions;

class DropKeyspaceAction extends KeyspaceAction {
    DropKeyspaceAction(String contactPointAddress, int contactPointPort) {
        super(contactPointAddress, contactPointPort);
    }

    @Override
    public void executeInternal(String keyspace) {
        dropKeyspace(keyspace);
    }
}
