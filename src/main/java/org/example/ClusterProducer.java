package org.example;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;

class ClusterProducer
{
    private static final Logger log = LoggerFactory.getLogger(ClusterProducer.class);

    @Produces
    @Singleton
    public Cluster produce()
    {
        log.info("Producing cluster");
        Cluster cluster = Cluster.builder().addContactPoints("127.0.0.1").build().init();
        log.info("Produced {}", cluster);
        return cluster;
    }

    public void dispose(@Disposes Cluster cluster)
    {
        log.info("Disposing {}", cluster);
        cluster.close();
        log.info("Disposed {}", cluster);
    }
}