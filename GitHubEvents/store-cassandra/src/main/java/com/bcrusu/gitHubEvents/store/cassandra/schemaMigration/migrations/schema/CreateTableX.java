package com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.migrations.schema;

import com.datastax.driver.core.SimpleStatement;

import io.smartcat.migration.exceptions.MigrationException;
import io.smartcat.migration.SchemaMigration;

public class CreateTableX extends SchemaMigration {

    public CreateTableX(final int version) {
        super(version);
    }

    @Override
    public String getDescription() {
        return "TODO";
    }

    @Override
    public void execute() throws MigrationException {
        try {
            final String alterBooksAddGenreCQL = "CREATE  TABLE ...;";

            executeWithSchemaAgreement(new SimpleStatement(alterBooksAddGenreCQL));

        } catch (final Exception e) {
            throw new MigrationException("Failed to execute CreateTableX migration", e);
        }
    }

}