package org.haw.vs.praktikum.gwln.praktikum1.b;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


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
				&& (type == null     || Pattern.matches(type,     event.getType()))
				&& (name == null     || Pattern.matches(name,     event.getName()))
				&& (reason == null   || Pattern.matches(reason,   event.getReason()))
				&& (resource == null || Pattern.matches(resource, event.getResource()))
				&& (player == null   || Pattern.matches(player,   event.getPlayer()));
		}).collect(Collectors.toList());
	}
	
	public void deleteEvent(String game, String type, String name, String reason, String resource, String player) {
		List<Event> matchingEvents = events.stream().filter((event) -> {
			return (game == null     || Pattern.matches(game,     event.getGame()))
				&& (type == null     || Pattern.matches(type,     event.getType()))
				&& (name == null     || Pattern.matches(name,     event.getName()))
				&& (reason == null   || Pattern.matches(reason,   event.getReason()))
				&& (resource == null || Pattern.matches(resource, event.getResource()))
				&& (player == null   || Pattern.matches(player,   event.getPlayer()));
		}).collect(Collectors.toList());
		events.removeAll(matchingEvents);
	}
	
	public int getSize() {
		return events.size();
	}
}
