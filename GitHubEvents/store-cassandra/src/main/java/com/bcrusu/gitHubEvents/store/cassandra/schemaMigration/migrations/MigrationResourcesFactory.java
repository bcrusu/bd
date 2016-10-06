package com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.migrations;

import com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.migrations.schema.*;
import io.smartcat.migration.MigrationResources;

public class MigrationResourcesFactory {
    public static MigrationResources createMigrationResources() {
        MigrationResources result = new MigrationResources();

        result.addMigration(new CreateTableEventTypePerSecond(1000));
        result.addMigration(new CreateTableEventTypePerMinute(1010));

        return result;
    }
}
