package org.example;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

class SessionProducer
{
    private static final Logger log = LoggerFactory.getLogger(SessionProducer.class);

    @Produces
    @Singleton
    public Session produce(Cluster cluster)
    {
        log.info("Producing session");
        Session session = cluster.connect();
        log.info("Produced {}", session);
        return session;
    }

    public void dispose(@Disposes Session session)
    {
        log.info("Disposing {}", session);
        session.close();
        log.info("Disposed {}", session);
    }
}