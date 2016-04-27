package org.haw.vs.praktikum.gwln.praktikum1.b;

import java.util.List;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class EventManagerRestClient {
	private static final String ENDPOINT = "/events";
	
	private String _url; // Die Adresse muss "/events" beinhalten
	
	/**
	 * 
	 * @param url Die URL des "/events"-Services
	 */
	public EventManagerRestClient(String url) {
		_url = url.endsWith(ENDPOINT) ? url : url + ENDPOINT;
	}
	
	public void postEvent(Event event) throws UnirestException {
		Unirest.post(_url)
				.header("Content-Type", "application/json")
				.body(EventJsonMarshaller.marshall(event))
				.asString();
	}
	
	public Event getEvent(String id) throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.get(_url + "/" + id).asJson();
		return EventJsonMarshaller.unmarshall(response.getBody().getObject());
	}
	
	@SuppressWarnings("unchecked")
	public List<Event> getEvents(String game, String type, String name, String reason, String resource, String player) throws UnirestException {
		String parameters = createParameterString(game, type, name, reason, resource, player);
		HttpResponse<String> response = Unirest.get(_url + parameters).asString();
		return (List<Event>)new Gson().fromJson(response.getBody(), List.class);
	}
	
	public void deleteEvents(String game, String type, String name, String reason, String resource, String player) throws UnirestException {
		String parameters = createParameterString(game, type, name, reason, resource, player);
		Unirest.delete(_url + parameters).asString();
	}
	
	private String createParameterString(String game, String type, String name, String reason, String resource, String player) {
		String params = "";
		if(game != null && !game.isEmpty()) params += "game=" + game;
		if(type != null && !type.isEmpty()) params += "type=" + type;
		if(name != null && !name.isEmpty()) params += "name=" + name;
		if(reason != null && !reason.isEmpty()) params += "reason=" + reason;
		if(resource != null && !resource.isEmpty()) params += "resource=" + resource;
		if(player != null && !player.isEmpty()) params += "player=" + player;
		return params.isEmpty() ? "" : "?" + params;
	}
}
