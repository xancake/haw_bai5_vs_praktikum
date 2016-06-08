package org.haw.vs.praktikum.gwln.events.rest;

import java.util.Arrays;
import java.util.List;
import org.haw.vs.praktikum.gwln.events.Event;
import org.json.JSONArray;
import org.json.JSONObject;

public class EventJsonMarshaller {
	
	public String marshall(Event... events) {
		return marshall(Arrays.asList(events));
	}
	
	public String marshall(List<Event> events) {
		return toJSONArray(events).toString();
	}
	
	public String marshall(Event event) {
		return toJSONObject(event).toString();
	}
	
	public JSONArray toJSONArray(List<Event> events) {
		JSONArray array = new JSONArray();
		for(Event event : events) {
			array.put(toJSONObject(event));
		}
		return array;
	}

	public JSONObject toJSONObject(Event event){
		JSONObject json = new JSONObject();
		json.put("id", event.getId());
		json.put("game", event.getGame());
		json.put("type", event.getType());
		json.put("name", event.getName());
		json.put("reason", event.getReason());
		json.put("resource", event.getResource());
		json.put("player", event.getPlayer());
		json.put("time", event.getTime());
		return json;
	}
	
	public Event unmarshall(String source) {
		return unmarshall(new JSONObject(source));
	}
	
	public Event unmarshall(JSONObject json) {
		String id = json.optString("id");
		String game = json.getString("game");
		String type = json.getString("type");
		String name = json.getString("name");
		String reason  = json.getString("reason");
		String resource = json.optString("resource");
		String player = json.optString("player");
		String time = json.optString("time");
		return new Event(id, game, type, name, reason, resource, player, time);
	}
}
