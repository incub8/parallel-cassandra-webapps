/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.example;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassandraExtension implements Extension
{
    private static final Logger log = LoggerFactory.getLogger(CassandraExtension.class);

    private boolean clusterFound = false;
    private boolean sessionFound = false;
    private Cluster cluster;
    private Session session;

    public <A> void processBean(final @Observes ProcessBean<A> processBeanEvent)
    {
        if (clusterFound && sessionFound)
        {
            return;
        }

        final Bean<A> bean = processBeanEvent.getBean();
        if (ClusterBean.class.isInstance(bean) || SessionBean.class.isInstance(bean))
        {
            return;
        }

        if (!clusterFound)
        {
            clusterFound = bean.getTypes().contains(Cluster.class);
        }
        if (!sessionFound)
        {
            sessionFound = bean.getTypes().contains(Session.class);
        }
    }

    public void addJCacheBeans(final @Observes AfterBeanDiscovery afterBeanDiscovery)
    {
        if (clusterFound && sessionFound)
        {
            return;
        }

        if (!clusterFound)
        {
            log.info("Creating new cluster instance");
            cluster = Cluster.builder().addContactPoints("127.0.0.1").build().init();
            afterBeanDiscovery.addBean(new ClusterBean(cluster));
            log.info("Cluster instance {} created", cluster);
        }
        if (!sessionFound)
        {
            log.info("Creating new session instance");
            session = cluster.connect();
            afterBeanDiscovery.addBean(new SessionBean(session));
            log.info("Session instance {} created", session);
        }
    }

    public void destroyIfCreated(final @Observes BeforeShutdown beforeShutdown)
    {
        if (session != null)
        {
            log.info("Closing session {}", session);
            session.close();
            log.info("Session {} closed", session);
        }
        if (cluster != null)
        {
            log.info("Closing cluster {}", cluster);
            cluster.close();
            log.info("Cluster {} closed", cluster);
        }
    }
}