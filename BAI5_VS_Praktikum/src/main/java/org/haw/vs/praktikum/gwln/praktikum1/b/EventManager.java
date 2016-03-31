package org.haw.vs.praktikum.gwln.praktikum1.b;

import static spark.Spark.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import com.google.gson.Gson;


public class EventManager {
	
	private List<Event> events = new ArrayList<>();
	
	public void addEvent(Event e) {
		events.add(e);
	}
	
	public Event getEvent(String id) {
		for(Event event : events) {
			if(event.getId().equals(id)) {
				return event;
			}
		}
		return null;
	}
	
	public List<Event> getMatchingEvents(String game, String type, String name, String reason, String resource, String player) {
		return events.stream().filter((event) -> {
			return (game == null     || Pattern.matches(game,     event.getGame()))
				&& (type == null     || Pattern.matches(type,     event.getTime()))
				&& (name == null     || Pattern.matches(name,     event.getName()))
				&& (reason == null   || Pattern.matches(reason,   event.getReason()))
				&& (resource == null || Pattern.matches(resource, event.getResource()))
				&& (player == null   || Pattern.matches(player,   event.getPlayer()));
		}).collect(Collectors.toList());
	}
	
	public void deleteEvent(String game, String type, String name, String reason, String resource, String player) {
		List<Event> matchingEvents = events.stream().filter((event) -> {
			return (game == null     || Pattern.matches(game,     event.getGame()))
				&& (type == null     || Pattern.matches(type,     event.getTime()))
				&& (name == null     || Pattern.matches(name,     event.getName()))
				&& (reason == null   || Pattern.matches(reason,   event.getReason()))
				&& (resource == null || Pattern.matches(resource, event.getResource()))
				&& (player == null   || Pattern.matches(player,   event.getPlayer()));
		}).collect(Collectors.toList());
		events.removeAll(matchingEvents);
	}
	
	public static void main(String[] args) {
		EventManager manager = new EventManager();
		Gson gson = new Gson();
		
		post("/events", (request,response) -> {
			Event event = gson.fromJson(request.body(), Event.class);
			
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
