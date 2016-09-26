package com.bcrusu.gitHubEvents.watcher;

import org.apache.commons.cli.*;

class CommandLineArgs {
    private final static String DEFAULT_EVENTS_URL = "https://api.github.com/events";
    private static Options _options;

    static {
        _options = buildOptions();
    }

    private final String _url;
    private final String _oauth2Token;

    private CommandLineArgs(String url, String oauth2Token) {
        _url = url;
        _oauth2Token = oauth2Token;
    }

    public String getUrl() {
        return _url;
    }

    public String getOauth2Token() {
        return _oauth2Token;
    }

    public static CommandLineArgs parse(String[] args) {
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(_options, args);

            String oauth2Token = cmd.getOptionValue("t");
            String url = DEFAULT_EVENTS_URL;
            if (cmd.hasOption("u"))
                url = cmd.getOptionValue("u");

            return new CommandLineArgs(url, oauth2Token);
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
                .desc("OAuth2 Token")
                .build());

        result.addOption(Option.builder("u")
                .longOpt("url")
                .hasArg()
                .desc("Events URL")
                .build());

        return result;
    }
}
