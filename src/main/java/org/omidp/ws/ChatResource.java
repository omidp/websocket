package org.omidp.ws;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.atmosphere.config.service.AtmosphereHandlerService;
import org.atmosphere.cpr.AtmosphereHandler;
import org.atmosphere.cpr.AtmosphereRequest;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResponse;

//@Path("/")
@AtmosphereHandlerService(path = "/chat")
public class ChatResource implements AtmosphereHandler {

	@Override
	public void onRequest(AtmosphereResource resource) throws IOException {
		AtmosphereRequest request = resource.getRequest();
		if (request.getMethod().equalsIgnoreCase("GET")) {
			resource.suspend();
		} else if (request.getMethod().equalsIgnoreCase("POST")) {
			resource.getBroadcaster().broadcast(
					request.getReader().readLine().trim());
			//resource.getBroadcaster().scheduleFixedBroadcast(request.getReader().readLine().trim(), 3, TimeUnit.SECONDS);
		}
	}

	@Override
	public void onStateChange(AtmosphereResourceEvent event) throws IOException {
		AtmosphereResource r = event.getResource();
		AtmosphereResponse res = r.getResponse();
		if (r.isSuspended()) {
			String body = event.getMessage() == null ? "{}" : event.getMessage().toString(); //must be json
			res.getWriter().write(body);
			switch (r.transport()) {
			case JSONP:
			case LONG_POLLING:
				event.getResource().resume();
				break;
			case WEBSOCKET:
			case STREAMING:
				res.getWriter().flush();
				break;
			}
		}else if (!event.isResuming()){
			event.broadcaster().broadcast("sa");
		}
	}

	@Override
	public void destroy() {

	}

}
