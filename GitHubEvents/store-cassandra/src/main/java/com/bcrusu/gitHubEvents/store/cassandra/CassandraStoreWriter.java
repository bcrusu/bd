package com.bcrusu.gitHubEvents.store.cassandra;

import com.bcrusu.gitHubEvents.common.store.IEventStoreWriter;
import com.bcrusu.gitHubEvents.common.store.WindowedEventType;
import com.datastax.driver.core.Session;

public class CassandraStoreWriter implements IEventStoreWriter {
    private final String _contactPointAddress;
    private final int _contactPointPort;
    private final String _keyspace;
    private Session _session;

    public CassandraStoreWriter(String contactPointAddress, int contactPointPort, String keyspace) {
        if (contactPointAddress == null) throw new IllegalArgumentException("contactPointAddress");
        if (keyspace == null) throw new IllegalArgumentException("keyspace");

        _contactPointAddress = contactPointAddress;
        _contactPointPort = contactPointPort;
        _keyspace = keyspace;
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

        _session = CassandraUtils.openSession(_contactPointAddress, _contactPointPort);
    }

    @Override
    public void close() throws Exception {
        if (_session == null)
            return;

        CassandraUtils.closeSession(_session);
        _session = null;
    }
}
