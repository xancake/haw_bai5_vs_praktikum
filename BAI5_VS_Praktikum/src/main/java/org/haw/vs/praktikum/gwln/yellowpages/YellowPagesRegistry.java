package org.haw.vs.praktikum.gwln.yellowpages;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class YellowPagesRegistry {
	private static final String YELLOW_PAGES = "http://172.18.0.5:4567";
	
	public static String registerOrUpdateService(String name, String description, String service, String uri) throws YellowPagesNotAvailableException {
		String id = getServiceId(name, description, service, uri);
		if(id != null) {
			updateServiceRegistry(id, name, description, service, uri);
			return id;
		} else {
			registerService(name, description, service, uri);
			return getServiceId(name, description, service, uri);
		}
	}
	
	public static String getServiceId(String name, String description, String service, String uri) throws YellowPagesNotAvailableException {
		try {
			HttpResponse<JsonNode> jsonResponse = Unirest.get(YELLOW_PAGES + "/services/of/name/" + name + "?expanded").asJson();
			if(jsonResponse.getStatus() == 200) {
				JSONObject services = jsonResponse.getBody().getObject();
				JSONArray servicesArray = services.getJSONArray("services");
				
				for(Object object : servicesArray) {
					JSONObject serviceObject = (JSONObject)object;
					String id     = serviceObject.getString("_uri");
					String status = serviceObject.getString("status");
					if("dead".equals(status)) {
						return id;
					}
				}
				return null;
			}
			throw new IllegalArgumentException("Yellow-Pages lieferte Statuscode '" + jsonResponse.getStatus() + "'");
		} catch(UnirestException e) {
			throw new YellowPagesNotAvailableException("Yellow-Pages Registry nicht erreichbar", e);
		}
	}
	
	public static void registerService(String name, String description, String service, String uri) throws YellowPagesNotAvailableException {
		try {
			Unirest.post(YELLOW_PAGES + "/services")
					.header("Content-Type", "application/json")
					.body(createYellowPagesJSON(name, description, service, uri))
					.asString();
		} catch (UnirestException e) {
			throw new YellowPagesNotAvailableException("Yellow-Pages Registry nicht erreichbar", e);
		}
	}
	
	public static void updateServiceRegistry(String id, String name, String description, String service, String uri) throws YellowPagesNotAvailableException {
		try {
			Unirest.put(YELLOW_PAGES + id)
					.header("Content-Type", "application/json")
					.body(createYellowPagesJSON(name, description, service, uri))
					.asString();
		} catch (UnirestException e) {
			throw new YellowPagesNotAvailableException("Yellow-Pages Registry nicht erreichbar", e);
		}
	}
	
	public static void unregisterService(String id) throws YellowPagesNotAvailableException {
		try {
			Unirest.delete(YELLOW_PAGES + id).asString();
		} catch (UnirestException e) {
			throw new YellowPagesNotAvailableException("Yellow-Pages Registry nicht erreichbar", e);
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
