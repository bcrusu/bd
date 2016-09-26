package com.bcrusu.gitHubEvents.watcher;

import com.bcrusu.gitHubEvents.watcher.api.GitHubEventSource;

public class Main {
    public static void main(String[] args) throws Exception {
        CommandLineArgs commandLineArgs = CommandLineArgs.parse(args);
        if (commandLineArgs == null) {
            System.out.println("Invalid command line arguments.");
            CommandLineArgs.printHelp();
            System.exit(-1);
            return;
        }

        String oauthToken = commandLineArgs.getOauth2Token();
        String url = commandLineArgs.getUrl();

        new GitHubEventSource(oauthToken, url)
                .getEventsSource().subscribe(x -> System.out.println(x.getId()));

        System.out.println("Press any key to exit...");
        System.in.read();
    }
}
