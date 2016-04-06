package org.haw.vs.praktikum.gwln;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class YellowPagesRegistry {
	private static final String YELLOW_PAGES = "http://172.18.0.5:4567/services";
	
	public static String registerOrUpdateService(String name, String description, String service, String uri) {
		if(isServiceRegistered(name, description, service, uri)) {
			String id = getServiceId(name, description, service, uri);
			updateServiceRegistry(id, name, description, service, uri);
			return id;
		} else {
			registerService(name, description, service, uri);
			return getServiceId(name, description, service, uri);
		}
	}
	
	public static boolean isServiceRegistered(String name, String description, String service, String uri) {
		try {
			HttpResponse<JsonNode> jsonResponse = Unirest.get(YELLOW_PAGES + "/of/name/" + name).asJson();
			if(jsonResponse.getStatus() == 200) {
				JSONArray services = jsonResponse.getBody().getArray();
				if(services.length() > 0) {
					// TODO: auf einem toten Service registrieren (put)
					// Wenn es keinen gibt, müssen wir evtl. trotzdem putten
				}
				return false;
			}
			throw new IllegalArgumentException("Yellow-Pages lieferte Statuscode '" + jsonResponse.getStatus() + "'");
		} catch(UnirestException e) {
			// TODO: besseren Exceptiontypen, idealerweise eine checked-Exception
			throw new RuntimeException("Yellow-Pages Registry nicht erreichbar", e);
		}
	}
	
	public static String getServiceId(String name, String description, String service, String uri) {
		
		
		
		return null;
	}
	
	public static void registerService(String name, String description, String service, String uri) {
		try {
			HttpResponse<String> response = Unirest.post(YELLOW_PAGES)
					.header("Content-Type", "application/json")
					.body(createYellowPagesJSON(name, description, service, uri))
					.asString();
		} catch (UnirestException e) {
			// TODO: besseren Exceptiontypen, idealerweise eine checked-Exception
			throw new RuntimeException("Yellow-Pages Registry nicht erreichbar", e);
		}
	}
	
	public static void updateServiceRegistry(String id, String name, String description, String service, String uri) {
		try {
			HttpResponse<String> response = Unirest.put(YELLOW_PAGES + "/" + id)
					.header("Content-Type", "application/json")
					.body(createYellowPagesJSON(name, description, service, uri))
					.asString();
		} catch (UnirestException e) {
			// TODO: besseren Exceptiontypen, idealerweise eine checked-Exception
			throw new RuntimeException("Yellow-Pages Registry nicht erreichbar", e);
		}
	}
	
	private static JSONObject createYellowPagesJSON(String name, String description, String service, String uri) {
		JSONObject json = new JSONObject();
		json.put("name", name);
		json.put("description", description);
		json.put("service", service);
		json.put("uri", uri);
		return json;
	}
}
