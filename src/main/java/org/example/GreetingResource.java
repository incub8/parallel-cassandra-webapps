package org.example;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Session;

@Path("greeting")
@Produces(MediaType.TEXT_PLAIN)
public class GreetingResource
{
    private static final Logger log = LoggerFactory.getLogger(GreetingResource.class);

    @Inject
    private Session session;

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
        return session.execute("select greeting from greetingkeyspace.greetings where id = 'greetingId'")
            .one()
            .getString("greeting");
    }
}