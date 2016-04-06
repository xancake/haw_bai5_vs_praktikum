package org.haw.vs.praktikum.gwln.praktikum1.b;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class EventManagerWebService {
	private static final String YELLOW_PAGES = "http://172.18.0.5:4567/services";
	
	private static final String NAME = "Event Manager Service 42_1337_69";
	private static final String DESCRIPTION = "Bester Event Manager 42_1337_69";
	private static final String SERVICE = "Event Manager Service";
	private static final String URI = "http://abq335_events:4567/events";
	
	public static void main(String[] args) {
		try {
			HttpResponse<JsonNode> jsonResponse = Unirest.get(YELLOW_PAGES+"/of/name/"+NAME).asJson();
			JSONObject json = new JSONObject();
			json.put("name", NAME);
			json.put("description", DESCRIPTION);
			json.put("service", SERVICE);
			json.put("uri", URI);
			if(jsonResponse.getStatus() == 200){
				JSONArray services = jsonResponse.getBody().getArray();
				if(services.length()>0) {
					// TODO: auf einem toten Service registrieren (put)
					// Wenn es keinen gibt, müssen wir evtl. trotzdem putten
				} else {
					HttpResponse<String> response = Unirest.post(YELLOW_PAGES)
							.header("Content-Type", "application/json")
							.body(json)
							.asString();
				}
			}
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		
		EventManager manager = new EventManager();
		Gson gson = new Gson();
		
		post("/events", (request,response) -> {
			String game = request.queryParams("game");
			String type = request.queryParams("type");
			String name = request.queryParams("name");
			String reason  = request.queryParams("request");
			String resource = request.queryParams("resource");
			String player = request.queryParams("player");
			String time = request.queryParams("time");
			
			Event event = new Event(String.valueOf(manager.getSize()+1), game, type, name, reason, resource, player, time);
			
			manager.addEvent(event);
			
			return "ok";
		});
		get("/events", (request, response) -> {
			String game = request.queryParams("game");
			String type = request.queryParams("type");
			String name = request.queryParams("name");
			String reason  = request.queryParams("request");
			String resource = request.queryParams("resource");
			String player = request.queryParams("player");
			
			List<Event> filteredEvents = manager.getMatchingEvents(game, type, name, reason, resource, player);
			
			response.status(200);
			return gson.toJson(filteredEvents);
		});
		delete("/events", (request, response) -> {
			String game = request.queryParams("game");
			String type = request.queryParams("type");
			String name = request.queryParams("name");
			String reason  = request.queryParams("request");
			String resource = request.queryParams("resource");
			String player = request.queryParams("player");
			
			manager.deleteEvent(game, type, name, reason, resource, player);
			
			return "ok";
		});
		get("/events/:eventid", (request, response) -> {
			String id = request.params(":eventid");
			
			Event event = manager.getEvent(id);
			
			return gson.toJson(event);
		});
	}
}
