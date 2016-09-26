package com.bcrusu.gitHubEvents.watcher;

import com.bcrusu.gitHubEvents.watcher.api.GitHubEventSource;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Invalid arguments.");
            System.exit(-1);
            return;
        }

        String oauthToken = args[0];
        String url = "https://api.github.com/events";

        new GitHubEventSource(oauthToken, url)
                .getEventsSource().subscribe(x -> System.out.println(x.getId()));

        System.out.println("Press any key to exit...");
        System.in.read();
    }
}
