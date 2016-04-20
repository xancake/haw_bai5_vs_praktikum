package org.haw.vs.praktikum.gwln.praktikum1.b;

import org.json.JSONObject;

public class JsonEventMarshaller {
	public static JSONObject marshall(Event event) {
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
	
	public static Event unmarshall(JSONObject json) {
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
