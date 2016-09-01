package org.example;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

@ApplicationScoped
public class CassandraDatabase
{
    private static final Logger log = LoggerFactory.getLogger(CassandraDatabase.class);

    private final Cluster cluster;
    private final Session session;

    protected CassandraDatabase()
    {
        log.info("Connecting to cassandra");
        cluster = Cluster.builder().addContactPoints("127.0.0.1").build().init();
        log.info("Cluster {} created", cluster);
        session = cluster.connect();
        log.info("Session {} created", session);
    }

    public Session getSession()
    {
        log.info("Returning {}", session);
        return session;
    }

    @PreDestroy
    protected void closeDatabaseConnection()
    {
        log.info("Disconnecting from cassandra");
        session.close();
        log.info("Session {} closed", session);
        cluster.close();
        log.info("Cluster {} closed", cluster);
    }
}