package com.bcrusu.gitHubEvents.store.cassandra.schemaMigration;

import org.apache.commons.cli.*;

class CommandLineArgs {
    private final static String DEFAULT_CASSANDRA_SERVER_ADDRESS = "localhost";
    private final static int DEFAULT_CASSANDRA_SERVER_PORT = 9042;
    private final static String DEFAULT_CASSANDRA_KEYSPACE = "gitHubEvents";
    private final static String DEFAULT_ACTION_TYPE = "migrate";

    private static Options _options;

    static {
        _options = buildOptions();
    }

    private final CommandLine _commandLine;

    private CommandLineArgs(CommandLine commandLine) {
        _commandLine = commandLine;
    }

    static CommandLineArgs parse(String[] args) {
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(_options, args);
            return new CommandLineArgs(cmd);
        } catch (ParseException e) {
            return null;
        }
    }

    public String getActionType() {
        String result = DEFAULT_ACTION_TYPE;
        if (_commandLine.hasOption("action"))
            result = _commandLine.getOptionValue("action");

        return result;
    }

    public String getServerAddress() {
        String result = DEFAULT_CASSANDRA_SERVER_ADDRESS;
        if (_commandLine.hasOption("address"))
            result = _commandLine.getOptionValue("address");

        return result;
    }

    public int getServerPort() {
        int result = DEFAULT_CASSANDRA_SERVER_PORT;
        if (_commandLine.hasOption("port"))
            result = Integer.parseInt(_commandLine.getOptionValue("port"));

        return result;
    }

    public String getKeyspace() {
        String result = DEFAULT_CASSANDRA_KEYSPACE;
        if (_commandLine.hasOption("keyspace"))
            result = _commandLine.getOptionValue("keyspace");

        return result;
    }

    static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("indexer", _options);
    }

    private static Options buildOptions() {
        Options result = new Options();

        result.addOption(Option.builder("action")
                .hasArg()
                .desc("Keyspace action: create|migrate|recreate|drop")
                .build());

        addCassandraOptions(result);
        return result;
    }

    private static void addCassandraOptions(Options options) {
        options.addOption(Option.builder("address")
                .longOpt("server_address")
                .hasArg()
                .desc("Cassandra server address")
                .build());

        options.addOption(Option.builder("port")
                .longOpt("server_port")
                .hasArg()
                .desc("Cassandra server port")
                .build());

        options.addOption(Option.builder("keyspace")
                .hasArg()
                .desc("Cassandra keyspace")
                .build());
    }
}
