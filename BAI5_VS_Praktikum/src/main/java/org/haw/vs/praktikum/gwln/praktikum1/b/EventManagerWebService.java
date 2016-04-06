package org.haw.vs.praktikum.gwln.praktikum1.b;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class EventManagerWebService {
	private static final String YELLOW_PAGES = "http://172.18.0.5:4567/services/14";
	
	public static void main(String[] args) {
		try {
			JSONObject json = new JSONObject();
			json.put("name", "Event Manager Service");
			json.put("description", "Bester Event Manager 42_1337_69");
			json.put("service", "Event Manager Service");
			json.put("uri", "http://abq335_events:4567/events");
			HttpResponse<String> response = Unirest.put(YELLOW_PAGES).header("Content-Type", "application/json").body(json).asString();
			System.out.println(response.getBody());
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
