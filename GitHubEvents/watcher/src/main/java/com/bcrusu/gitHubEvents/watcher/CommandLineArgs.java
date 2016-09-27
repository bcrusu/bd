package com.bcrusu.gitHubEvents.watcher;

import org.apache.commons.cli.*;

class CommandLineArgs {
    private final static String DEFAULT_GITHUB_EVENTS_URL = "https://api.github.com/events";
    private final static String DEFAULT_KAFKA_SERVER = "localhost:9092";

    private static Options _options;

    static {
        _options = buildOptions();
    }

    private final CommandLine _commandLine;

    private CommandLineArgs(CommandLine commandLine) {
        _commandLine = commandLine;
    }

    public String getUrl() {
        String result = DEFAULT_GITHUB_EVENTS_URL;
        if (_commandLine.hasOption("u"))
            result = _commandLine.getOptionValue("u");

        return result;
    }

    public String getOauth2Token() {
        return _commandLine.getOptionValue("t");
    }

    public String getEventWriterType() {
        String result = EventWriterFactory.EVENT_WRITER_TYPE_KAFKA;
        if (_commandLine.hasOption("ew"))
            result = _commandLine.getOptionValue("ew");

        return result;
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
        formatter.printHelp("watcher", _options);
    }

    private static Options buildOptions() {
        Options result = new Options();

        result.addOption(Option.builder("t")
                .required()
                .longOpt("oauth2_token")
                .hasArg()
                .desc("GitHub OAuth2 Token")
                .build());

        result.addOption(Option.builder("u")
                .longOpt("url")
                .hasArg()
                .desc("GitHub Events URL")
                .build());

        result.addOption(Option.builder("ew")
                .required()
                .longOpt("event_writer")
                .hasArg()
                .desc("Event Writer Type")
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
    }
}
