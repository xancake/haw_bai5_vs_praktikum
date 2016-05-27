package org.haw.vs.praktikum.gwln.events.rest.client;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpHeader;
import org.haw.vs.praktikum.gwln.events.Event;
import org.haw.vs.praktikum.gwln.events.rest.EventJsonMarshaller;
import org.haw.vs.praktikum.gwln.rest.client.AbstractRestClient;
import org.json.JSONArray;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class EventManagerRestClient extends AbstractRestClient {
	private static EventJsonMarshaller _marshaller = new EventJsonMarshaller();
	
	/**
	 * 
	 * @param url Die URL des "/events"-Services
	 * @throws MalformedURLException 
	 */
	public EventManagerRestClient(String url) throws MalformedURLException {
		super(url, "/events");
	}
	
	public String postEvent(Event event) throws UnirestException {
		HttpResponse<String> response = Unirest.post(getURL().toExternalForm())
				.header("Content-Type", "application/json")
				.body(_marshaller.marshall(event))
				.asString();
		return response.getHeaders().getFirst(HttpHeader.LOCATION.asString());
	}
	
	public Event getEventById(String eventId) throws UnirestException {
		return getEvent(getURL().toExternalForm() + "/" + eventId);
	}
	
	public static Event getEvent(String uri) throws UnirestException {
		HttpResponse<String> response = Unirest.get(uri).asString();
		return _marshaller.unmarshall(response.getBody());
	}
	
	@SuppressWarnings("unchecked")
	public List<Event> getEvents(String game, String type, String name, String reason, String resource, String player) throws UnirestException {
		String parameters = createParameterString(game, type, name, reason, resource, player);
		HttpResponse<String> response = Unirest.get(getURL().toExternalForm() + parameters).asString();
		return (List<Event>)new Gson().fromJson(response.getBody(), List.class);
	}
	
	public void deleteEvents(String game, String type, String name, String reason, String resource, String player) throws UnirestException {
		String parameters = createParameterString(game, type, name, reason, resource, player);
		Unirest.delete(getURL().toExternalForm() + parameters).asString();
	}
	
	public List<String> getSubscriptions() throws UnirestException{
		HttpResponse<String> response = Unirest.get(getURL().toExternalForm() + "/subscriptions").asString();
		JSONArray responseList = new JSONArray(response);
		List<String> subscriptions = new ArrayList<>();
		responseList.forEach( entry -> subscriptions.add((String)entry) );
		return subscriptions;
	}
	
	public String postSubscription(Event prototypeEvent) throws UnirestException{
		HttpResponse<String> response = Unirest.post(getURL().toExternalForm() + "/subscriptions")
												.body(_marshaller.marshall(prototypeEvent))
												.asString();
		return response.getHeaders().getFirst(HttpHeader.LOCATION.asString());
	}
	
	public void deleteSubscription(String id) throws UnirestException{
		Unirest.delete(getURL().toExternalForm() + "/subscriptions/" + id).asString();
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
