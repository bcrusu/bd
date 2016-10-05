package com.bcrusu.gitHubEvents.store.cassandra.schemaMigration;

import com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.migrations.schema.CreateTableX;
import io.smartcat.migration.MigrationResources;

class MigrationResourcesFactory {
    static MigrationResources createMigrationResources() {
        MigrationResources result = new MigrationResources();

        result.addMigration(new CreateTableX(1));

        return result;
    }
}
