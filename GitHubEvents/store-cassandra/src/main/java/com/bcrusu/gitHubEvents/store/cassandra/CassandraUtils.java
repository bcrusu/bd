package com.bcrusu.gitHubEvents.store.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class CassandraUtils {
    public static Session openSession(String contactPointAddress, int contactPointPort) {
        Cluster cluster = null;

        try {
            cluster = Cluster.builder()
                    .addContactPoints(contactPointAddress)
                    .withPort(contactPointPort)
                    .build();
            return cluster.connect();
        } catch (Exception e) {
            if (cluster != null)
                cluster.close();

            throw e;
        }
    }

    public static void closeSession(Session session) {
        Cluster cluster = session.getCluster();
        session.close();
        cluster.close();
    }

    public static void useKeyspace(Session session, String keyspace) {
        String statement = String.format("USE %s", keyspace);
        session.execute(statement);
    }
}
