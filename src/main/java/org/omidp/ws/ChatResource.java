package org.omidp.ws;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.atmosphere.annotation.Broadcast;
import org.atmosphere.annotation.Suspend;
import org.atmosphere.config.service.AtmosphereService;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListenerAdapter;
import org.atmosphere.jersey.JerseyBroadcaster;


@Path("/")
@AtmosphereService (broadcaster = JerseyBroadcaster.class)
public class ChatResource {

	@Suspend(contentType = "application/json", listeners = {OnDisconnect.class})
    @GET
    public String suspend() {

        return "";
    }
	@Broadcast(writeEntity = true)
    @POST
    @Produces("application/json")
    public Customer broadcast(Message message) {
        return new Customer(message.author, message.message);
    }

    public static final class OnDisconnect extends AtmosphereResourceEventListenerAdapter {
        /**
         * {@inheritDoc}
         */
        @Override
        public void onDisconnect(AtmosphereResourceEvent event) {
            System.out.println(event);
        }
    }
	
}
