package org.haw.vs.praktikum.gwln.praktikum1.b.events;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.haw.vs.praktikum.gwln.yellowpages.YellowPagesNotAvailableException;
import org.haw.vs.praktikum.gwln.yellowpages.YellowPagesRegistry;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;

public class EventManagerWebService {
	private static final String NAME = "Event Manager Service 42_1337_69";
	private static final String DESCRIPTION = "Bester Event Manager 42_1337_69";
	private static final String SERVICE = "Event Manager Service";
	
	private static String URI;
	private static final EventJsonMarshaller MARSHALLER = new EventJsonMarshaller();
	
	private static final EventManager MANAGER = new EventManager();
	private static int EVENT_COUNTER = 0;
	
	private static String postEvent(Request request, Response response) {
		Event requestEvent = MARSHALLER.unmarshall(request.body());
		
		Event event = new Event(
				String.valueOf(++EVENT_COUNTER),
				requestEvent.getGame(),
				requestEvent.getType(),
				requestEvent.getName(),
				requestEvent.getReason(),
				requestEvent.getResource(),
				requestEvent.getPlayer(),
				requestEvent.getTime()
		);
		MANAGER.addEvent(event);
		
		response.header("Location", URI + "/events/" + event.getId());
		response.status(201);
		return "ok";
	}
	
	private static String getEvents(Request request, Response response) {
		String game = request.queryParams("game");
		String type = request.queryParams("type");
		String name = request.queryParams("name");
		String reason  = request.queryParams("reason");
		String resource = request.queryParams("resource");
		String player = request.queryParams("player");
		
		List<Event> filteredEvents = MANAGER.getMatchingEvents(game, type, name, reason, resource, player);
		
		return new Gson().toJson(filteredEvents);
	}
	
	private static String deleteEvents(Request request, Response response) {
		String game = request.queryParams("game");
		String type = request.queryParams("type");
		String name = request.queryParams("name");
		String reason  = request.queryParams("reason");
		String resource = request.queryParams("resource");
		String player = request.queryParams("player");
		
		MANAGER.deleteEvents(game, type, name, reason, resource, player);
		
		response.status(204);
		return "ok";
	}
	
	private static String getEvent(Request request, Response response) {
		String id = request.params(":eventid");
		
		Event event = MANAGER.getEvent(id);
		
		if(event == null) {
			response.status(404);
			return null;
		}
		return MARSHALLER.marshall(event);
	}
	
	public static void main(String[] args) throws UnknownHostException {
		try {
			URI = "http://" + InetAddress.getLocalHost().getHostAddress() + ":4567";
			YellowPagesRegistry.registerOrUpdateService(NAME, DESCRIPTION, SERVICE, URI + "/events");
		} catch(YellowPagesNotAvailableException e) {
			e.printStackTrace();
		}
		
		post("/events", EventManagerWebService::postEvent);
		get("/events", EventManagerWebService::getEvents);
		delete("/events", EventManagerWebService::deleteEvents);
		get("/events/:eventid", EventManagerWebService::getEvent);
	}
}