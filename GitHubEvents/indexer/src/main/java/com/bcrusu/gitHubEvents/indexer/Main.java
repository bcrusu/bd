package com.bcrusu.gitHubEvents.indexer;

public class Main {
    public static void main(String[] args) throws Exception {
        CommandLineArgs commandLineArgs = CommandLineArgs.parse(args);
        if (commandLineArgs == null) {
            System.err.println("Invalid command line arguments.");
            CommandLineArgs.printHelp();
            System.exit(-1);
            return;
        }

        System.out.println("Indexing events. Press any key to exit...");

        try {

            //TODO

        } catch (Exception e) {
            System.err.println(e);
            System.exit(-2);
        }
    }
}
