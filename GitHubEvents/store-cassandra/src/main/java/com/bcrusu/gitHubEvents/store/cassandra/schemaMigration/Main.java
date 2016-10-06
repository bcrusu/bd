package com.bcrusu.gitHubEvents.store.cassandra.schemaMigration;

import com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.keyspaceActions.KeyspaceAction;
import com.bcrusu.gitHubEvents.store.cassandra.schemaMigration.keyspaceActions.KeyspaceActionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger _logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        CommandLineArgs commandLineArgs = CommandLineArgs.parse(args);
        if (commandLineArgs == null) {
            System.err.println("Invalid command line arguments.");
            CommandLineArgs.printHelp();
            System.exit(-1);
            return;
        }

        try {
            _logger.info("Running keyspace action ...");
            KeyspaceAction action = createKeyspaceAction(commandLineArgs);
            String keyspace = commandLineArgs.getKeyspace();
            action.execute(keyspace);
        } catch (Exception e) {
            _logger.error("Unexpected error", e);
            System.exit(-2);
        }
    }

    private static KeyspaceAction createKeyspaceAction(CommandLineArgs args) {
        String address = args.getServerAddress();
        int port = args.getServerPort();
        String actionType = args.getActionType();
        return KeyspaceActionFactory.create(actionType, address, port);
    }
}
