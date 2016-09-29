package com.bcrusu.gitHubEvents.indexer;


import com.bcrusu.gitHubEvents.indexer.writer.ConsoleEventWriter;
import com.bcrusu.gitHubEvents.indexer.writer.ElasticsearchEventWriter;
import com.bcrusu.gitHubEvents.indexer.writer.IEventWriter;

class EventWriterFactory {
    public final static String EVENT_WRITER_TYPE_ELASTICSEARCH = "elasticsearch";
    public final static String EVENT_WRITER_TYPE_CONSOLE = "console";

    public static IEventWriter create(CommandLineArgs args) {
        String eventWriterType = args.getEventWriterType();

        switch (eventWriterType.toLowerCase()) {
            case EVENT_WRITER_TYPE_ELASTICSEARCH:
                String address = args.getElasticsearchServerAddress();
                int port = args.getElasticsearchServerPort();
                String clusterName = args.getElasticsearchClusterName();
                String index = args.getElasticsearchIndex();
                return new ElasticsearchEventWriter(address, port, clusterName, index);
            case EVENT_WRITER_TYPE_CONSOLE:
                return new ConsoleEventWriter();
            default:
                throw new IllegalArgumentException("Unrecognized event writer type: " + eventWriterType);
        }
    }
}
