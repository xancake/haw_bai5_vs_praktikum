package org.haw.vs.praktikum.gwln.praktikum1.b.events;

import java.util.List;

import org.haw.vs.praktikum.gwln.rest.client.AbstractRestClient;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class EventManagerRestClient extends AbstractRestClient {
	private EventJsonMarshaller _marshaller;
	
	/**
	 * 
	 * @param url Die URL des "/events"-Services
	 */
	public EventManagerRestClient(String url) {
		super(url, "/events");
		_marshaller = new EventJsonMarshaller();
	}
	
	public void postEvent(Event event) throws UnirestException {
		Unirest.post(getUrl())
				.header("Content-Type", "application/json")
				.body(_marshaller.marshall(event))
				.asString();
	}
	
	public Event getEvent(String id) throws UnirestException {
		HttpResponse<String> response = Unirest.get(getUrl() + "/" + id).asString();
		return _marshaller.unmarshall(response.getBody());
	}
	
	@SuppressWarnings("unchecked")
	public List<Event> getEvents(String game, String type, String name, String reason, String resource, String player) throws UnirestException {
		String parameters = createParameterString(game, type, name, reason, resource, player);
		HttpResponse<String> response = Unirest.get(getUrl() + parameters).asString();
		return (List<Event>)new Gson().fromJson(response.getBody(), List.class);
	}
	
	public void deleteEvents(String game, String type, String name, String reason, String resource, String player) throws UnirestException {
		String parameters = createParameterString(game, type, name, reason, resource, player);
		Unirest.delete(getUrl() + parameters).asString();
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
