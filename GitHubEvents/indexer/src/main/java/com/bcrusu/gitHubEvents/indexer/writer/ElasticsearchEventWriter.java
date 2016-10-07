package com.bcrusu.gitHubEvents.indexer.writer;

import com.bcrusu.gitHubEvents.common.cli.ElasticsearchProperties;
import com.bcrusu.gitHubEvents.indexer.Event;
import com.bcrusu.gitHubEvents.indexer.IndexerError;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public class ElasticsearchEventWriter implements IEventWriter {
    private static final String INDEX_TYPE = "events";
    private static final Logger _logger = LoggerFactory.getLogger(ElasticsearchEventWriter.class);

    private final TransportClient _transportClient;
    private final ElasticsearchProperties _properties;

    public ElasticsearchEventWriter(ElasticsearchProperties properties) {
        if (properties == null) throw new IllegalArgumentException("properties");
        _properties = properties;
        _transportClient = createTransportClient(properties);
    }

    @Override
    public void write(Event event) {
        _transportClient.prepareIndex(_properties.getIndex(), INDEX_TYPE, event.getId())
                .setSource(event.getJson())
                .get();
    }

    @Override
    public void close() throws Exception {
        _transportClient.close();
    }

    private static TransportClient createTransportClient(ElasticsearchProperties properties) {
        Settings.Builder settingsBuilder = Settings.settingsBuilder()
                .put("client.transport.sniff", true)
                .put("cluster.name", properties.getCluster());

        Settings settings = settingsBuilder.build();
        TransportClient client;

        try {
            client = TransportClient.builder()
                    .settings(settings)
                    .build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(properties.getAddress()), properties.getPort()));
        } catch (UnknownHostException e) {
            throw new IndexerError("Failed to connect to Elasticsearch server", e);
        }

        ensureIndexExists(client, properties.getIndex());
        return client;
    }

    private static void ensureIndexExists(TransportClient client, String index) {
        try {
            boolean isExists = client.admin().indices()
                    .exists(new IndicesExistsRequest(index))
                    .get().isExists();

            if (isExists) {
                _logger.info("Elasticsearch index {} exists", index);
                return;
            }

            _logger.info("Creating index {}...", index);
            boolean isAcknowledged = client.admin().indices().create(new CreateIndexRequest(index)).get().isAcknowledged();

            if (!isAcknowledged) {
                throw new IndexerError(String.format("Failed to create Elasticsearch index '%s'", index));
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new IndexerError("Failed to create Elasticsearch index", e);
        }
    }
}
