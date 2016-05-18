package org.haw.vs.praktikum.gwln.events.rest.service;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.haw.vs.praktikum.gwln.events.Event;
import org.haw.vs.praktikum.gwln.events.EventManager;
import org.haw.vs.praktikum.gwln.events.Subscription;
import org.haw.vs.praktikum.gwln.events.rest.EventJsonMarshaller;
import org.haw.vs.praktikum.gwln.events.rest.SubscriptionJsonMarshaller;
import org.haw.vs.praktikum.gwln.yellowpages.YellowPagesNotAvailableException;
import org.haw.vs.praktikum.gwln.yellowpages.YellowPagesRegistry;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import spark.Request;
import spark.Response;

public class EventManagerWebService {
	private static final String NAME = "Event Manager Service 42_1337_69";
	private static final String DESCRIPTION = "Bester Event Manager 42_1337_69";
	private static final String SERVICE = "Event Manager Service";
	
	private static String URI;
	private static final EventJsonMarshaller EVENT_MARSHALLER = new EventJsonMarshaller();
	private static final SubscriptionJsonMarshaller SUBSCRIPTION_MARSHALLER = new SubscriptionJsonMarshaller();
	
	private static final EventManager MANAGER = new EventManager();
	private static int EVENT_COUNTER = 0;
	private static int SUBSCRIPTION_COUNTER = 0;
	
	private static String postEvent(Request request, Response response) {
		Event requestEvent = EVENT_MARSHALLER.unmarshall(request.body());
		
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
		
		MANAGER.getSubscriptions().forEach(subscription -> {
			Event prototypeEvent = subscription.getPrototypeEvent();
			if(prototypeEvent.matches(event)) {
				new Thread(() -> {
					try {
						Unirest.post(subscription.getCallUri())
								.header(HttpHeader.CONTENT_TYPE.asString(), "appliction/json")
								.body(EVENT_MARSHALLER.marshall(event))
								.asString();
					} catch(UnirestException e) {
						// Subscription entfernen, wenn sie nicht mehr funktioniert (aufrufbar ist)
						MANAGER.removeSubscription(subscription.getId());
						System.out.println("[EVENTS] Tote Subscription (" + subscription.getId() + ") von '" + subscription.getCallUri() +"' entfernt.");
					}
				}).start();
			}
		});
		
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
		return EVENT_MARSHALLER.marshall(event);
	}
	
	private static String getSubscriptions(Request request, Response response) {
		try {
			List<Subscription> subscriptions = MANAGER.getSubscriptions();
			
			return SUBSCRIPTION_MARSHALLER.marshall(subscriptions);
		} catch(Exception e) {
			response.status(HttpStatus.PRECONDITION_FAILED_412);
			return e.getMessage();
		}
	}
	
	private static String postSubscription(Request request, Response response) {
		JSONObject json = new JSONObject(request.body());
		String game = json.getString("game");
		String callUri = json.getString("uri");
		Event prototypeEvent = EVENT_MARSHALLER.unmarshall(json.getJSONObject("event"));
		
		Subscription subscription = new Subscription(SUBSCRIPTION_COUNTER++, URI + "/events/subscriptions/", game, callUri, prototypeEvent);
		MANAGER.addSubscription(subscription);
		
		response.status(HttpStatus.CREATED_201);
		response.header(HttpHeader.LOCATION.asString(), subscription.getUri());
		return "Subscription eingetragen";
	}
	
	private static String deleteSubscription(Request request, Response response) {
		try {
			String subscriptionId = request.params(":subscriptionId");
			
			MANAGER.removeSubscription(Integer.parseInt(subscriptionId));
			
			response.status(HttpStatus.ACCEPTED_202);
			return "Subscription ausgetragen";
		} catch(Exception e) {
			response.status(HttpStatus.PRECONDITION_FAILED_412);
			return e.getMessage();
		}
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
		get("/events/subscriptions", EventManagerWebService::getSubscriptions);
		post("/events/subscriptions", EventManagerWebService::postSubscription);
		delete("/events/subscriptions/:subscriptionId", EventManagerWebService::deleteSubscription);
		get("/events/:eventid", EventManagerWebService::getEvent);
	}
}
