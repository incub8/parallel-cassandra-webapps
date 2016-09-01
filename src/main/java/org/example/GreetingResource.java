package org.example;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("greeting")
@Produces(MediaType.TEXT_PLAIN)
public class GreetingResource
{
    private static final Logger log = LoggerFactory.getLogger(GreetingResource.class);

    @Inject
    private CassandraDatabase cassandraDatabase;

    @GET
    public String get()
    {
        log.info("Get called");
        String greeting = readGreetingFromCassandra();
        log.info("Returning {}", greeting);
        return greeting;
    }

    private String readGreetingFromCassandra()
    {
        return cassandraDatabase.getSession().execute(
            "select greeting from greetingkeyspace.greetings where id = 'greetingId'").one().getString("greeting");
    }
}