package com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.migrations;

import com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.migrations.schema.CreateTableX;
import io.smartcat.migration.MigrationResources;

public class MigrationResourcesFactory {
    public static MigrationResources createMigrationResources() {
        MigrationResources result = new MigrationResources();

        result.addMigration(new CreateTableX(1));

        return result;
    }
}
