package com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.keyspaceActions;

import com.bcrusu.gitHubEvents.common.cli.CassandraProperties;

public class KeyspaceActionFactory {
    public static KeyspaceAction create(String type, CassandraProperties properties) {
        if (type == null) throw new IllegalArgumentException("type");

        String contactPoints = properties.getContactPoints();
        int port = properties.getPort();

        switch (type.trim().toLowerCase()) {
            case "create":
                return new CreateKeyspaceAction(contactPoints, port);
            case "migrate":
                return new MigrateKeyspaceAction(contactPoints, port);
            case "recreate":
                return new RecreateKeyspaceAction(contactPoints, port);
            case "drop":
                return new DropKeyspaceAction(contactPoints, port);
            default:
                throw new IllegalArgumentException("Unrecognized action type: " + type);
        }
    }
}
