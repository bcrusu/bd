package com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.keyspaceActions;

public class KeyspaceActionFactory {
    public static KeyspaceAction create(String type, String contactPointAddress, int contactPointPort) {
        if (type == null) throw new IllegalArgumentException("type");

        switch (type.trim().toLowerCase()) {
            case "create":
                return new CreateKeyspaceAction(contactPointAddress, contactPointPort);
            case "migrate":
                return new MigrateKeyspaceAction(contactPointAddress, contactPointPort);
            case "recreate":
                return new RecreateKeyspaceAction(contactPointAddress, contactPointPort);
            case "drop":
                return new DropKeyspaceAction(contactPointAddress, contactPointPort);
            default:
                throw new IllegalArgumentException("Unrecognized action type: " + type);
        }
    }
}
