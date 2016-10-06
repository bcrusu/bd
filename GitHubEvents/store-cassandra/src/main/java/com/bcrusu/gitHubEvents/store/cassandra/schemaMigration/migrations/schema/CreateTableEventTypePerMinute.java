package com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.migrations.schema;

import com.datastax.driver.core.SimpleStatement;
import io.smartcat.migration.SchemaMigration;
import io.smartcat.migration.exceptions.MigrationException;

public class CreateTableEventTypePerMinute extends SchemaMigration {

    public CreateTableEventTypePerMinute(final int version) {
        super(version);
    }

    @Override
    public String getDescription() {
        return "CreateTableEventTypePerMinute";
    }

    @Override
    public void execute() throws MigrationException {
        try {
            final String statement = "CREATE TABLE EventTypePerMinute\n" +
                    "(day date, minute timestamp, eventType text, count bigint,\n" +
                    "PRIMARY KEY (day, minute));";

            executeWithSchemaAgreement(new SimpleStatement(statement));
        } catch (final Exception e) {
            throw new MigrationException("Failed to execute CreateTableEventTypePerMinute migration", e);
        }
    }
}