package com.bcrusu.gitHubEvents.store.cassandra;

import com.bcrusu.gitHubEvents.common.cli.CassandraProperties;
import com.bcrusu.gitHubEvents.common.store.IEventStoreWriter;
import com.bcrusu.gitHubEvents.common.store.WindowedEventType;
import com.datastax.driver.core.Session;

public class CassandraStoreWriter implements IEventStoreWriter {
    private final CassandraProperties _properties;
    private Session _session;

    public CassandraStoreWriter(CassandraProperties properties) {
        if (properties == null) throw new IllegalArgumentException("properties");
        _properties = properties;
    }

    @Override
    public void writeEventsPerSecond(WindowedEventType windowedEventType, long count) {
        ensureSession();
        //TODO:
    }

    @Override
    public void writeEventsPerMinute(WindowedEventType windowedEventType, long count) {
        ensureSession();
        //TODO:
    }

    private void ensureSession() {
        if (_session != null)
            return;

        _session = CassandraUtils.openSession(_properties.getContactPoints(), _properties.getPort());
    }

    @Override
    public void close() throws Exception {
        if (_session == null)
            return;

        CassandraUtils.closeSession(_session);
        _session = null;
    }
}
