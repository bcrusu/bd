package com.bcrusu.gitHubEvents.store.cassandra;

import com.bcrusu.gitHubEvents.common.TimeUtils;
import com.bcrusu.gitHubEvents.common.cli.CassandraProperties;
import com.bcrusu.gitHubEvents.common.store.IEventStoreWriter;
import com.bcrusu.gitHubEvents.common.store.WindowedEventType;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.LocalDate;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

import java.util.Date;

public class CassandraStoreWriter implements IEventStoreWriter {
    private final CassandraProperties _cassandraProperties;
    private Session _session;

    private PreparedStatement _writeEventsPerSecond;
    private PreparedStatement _writeEventsPerMinute;

    public CassandraStoreWriter(CassandraProperties cassandraProperties) {
        if (cassandraProperties == null) throw new IllegalArgumentException("cassandraProperties");
        _cassandraProperties = cassandraProperties;
    }

    @Override
    public void writeEventsPerSecond(WindowedEventType windowedEventType, long count) {
        ensureSession();

        Long day = TimeUtils.truncatedToDays(windowedEventType.windowStart);
        Long second = TimeUtils.truncatedToSeconds(windowedEventType.windowStart);

        if (_writeEventsPerSecond == null)
            _writeEventsPerSecond = _session.prepare("INSERT INTO EventTypePerSecond (day, second, eventtype, count) VALUES (?, ?, ?, ?);");

        BoundStatement bound = _writeEventsPerSecond.bind(LocalDate.fromMillisSinceEpoch(day), new Date(second), windowedEventType.eventType, count);
        _session.execute(bound);
    }

    @Override
    public void writeEventsPerMinute(WindowedEventType windowedEventType, long count) {
        ensureSession();

        Long day = TimeUtils.truncatedToDays(windowedEventType.windowStart);
        Long minute = TimeUtils.truncatedToMinutes(windowedEventType.windowStart);

        if (_writeEventsPerMinute == null)
            _writeEventsPerMinute = _session.prepare("INSERT INTO EventTypePerMinute (day, minute, eventtype, count) VALUES (?, ?, ?, ?);");

        BoundStatement bound = _writeEventsPerMinute.bind(LocalDate.fromMillisSinceEpoch(day), new Date(minute), windowedEventType.eventType, count);
        _session.execute(bound);
    }

    private void ensureSession() {
        if (_session != null)
            return;

        Session session = CassandraUtils.openSession(_cassandraProperties.getContactPoints(), _cassandraProperties.getPort());
        CassandraUtils.useKeyspace(session, _cassandraProperties.getKeyspace());

        _session = session;
    }

    @Override
    public void close() throws Exception {
        if (_session == null)
            return;

        CassandraUtils.closeSession(_session);
        _session = null;
    }
}
