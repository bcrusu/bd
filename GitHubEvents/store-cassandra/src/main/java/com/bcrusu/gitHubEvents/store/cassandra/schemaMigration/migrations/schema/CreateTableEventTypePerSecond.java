package com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.migrations.schema;

import com.datastax.driver.core.SimpleStatement;

import io.smartcat.migration.exceptions.MigrationException;
import io.smartcat.migration.SchemaMigration;

public class CreateTableEventTypePerSecond extends SchemaMigration {

    public CreateTableEventTypePerSecond(final int version) {
        super(version);
    }

    @Override
    public String getDescription() {
        return "CreateTableEventTypePerSecond";
    }

    @Override
    public void execute() throws MigrationException {
        try {
            final String statement = "CREATE TABLE EventTypePerSecond\n" +
                    "(day date, second timestamp, eventType text, count bigint,\n" +
                    "PRIMARY KEY ((day), second, eventType));";

            executeWithSchemaAgreement(new SimpleStatement(statement));
        } catch (final Exception e) {
            throw new MigrationException("Failed to execute CreateTableEventTypePerSecond migration", e);
        }
    }
}