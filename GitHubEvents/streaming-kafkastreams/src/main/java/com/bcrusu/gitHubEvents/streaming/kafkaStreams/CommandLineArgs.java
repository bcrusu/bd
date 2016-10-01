package com.bcrusu.gitHubEvents.streaming.kafkaStreams;

import org.apache.commons.cli.*;

class CommandLineArgs {
    private final static String DEFAULT_KAFKA_SERVER = "localhost:9092";
    private final static String DEFAULT_KAFKA_STREAMS_STATE_DIR = "/tmp/kafka-streams";

    private static Options _options;

    static {
        _options = buildOptions();
    }

    private final CommandLine _commandLine;

    private CommandLineArgs(CommandLine commandLine) {
        _commandLine = commandLine;
    }

    public String getId() {
        return _commandLine.getOptionValue("id");
    }

    public String getKafkaServer() {
        String result = DEFAULT_KAFKA_SERVER;
        if (_commandLine.hasOption("ks"))
            result = _commandLine.getOptionValue("ks");

        return result;
    }

    public String getKafkaTopic() {
        return _commandLine.getOptionValue("kt");
    }

    public String getKafkaStreamsStateDir(){
        String result = DEFAULT_KAFKA_STREAMS_STATE_DIR;
        if (_commandLine.hasOption("sd"))
            result = _commandLine.getOptionValue("sd");

        return result;
    }

    public static CommandLineArgs parse(String[] args) {
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(_options, args);
            return new CommandLineArgs(cmd);
        } catch (ParseException e) {
            return null;
        }
    }

    public static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("indexer", _options);
    }

    private static Options buildOptions() {
        Options result = new Options();

        result.addOption(Option.builder("id")
                .required()
                .hasArg()
                .desc("Indexer ID")
                .build());

        addKafkaOptions(result);

        return result;
    }

    private static void addKafkaOptions(Options options) {
        options.addOption(Option.builder("kt")
                .longOpt("kafka_topic")
                .hasArg()
                .desc("Kafka topic")
                .build());

        options.addOption(Option.builder("ks")
                .longOpt("kafka_servers")
                .hasArg()
                .desc("Kafka config property: bootstrap.servers")
                .build());

        options.addOption(Option.builder("sd")
                .longOpt("state_dir")
                .hasArg()
                .desc("Kafka config property: state.dir")
                .build());
    }
}
