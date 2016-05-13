package org.haw.vs.praktikum.gwln.client.restclient.game;

import com.google.gson.JsonObject;

public class Game {
	private String _id;
	private String _name;
	private String _playersUri;
	private JsonObject _services;
	private JsonObject _components;
	
	public Game(String id) {
		_id = id;
	}

	public String getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}

	public String getPlayersUri() {
		return _playersUri;
	}

	public JsonObject getServices() {
		return _services;
	}

	public JsonObject getComponents() {
		return _components;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setPlayersUri(String playersUri) {
		_playersUri = playersUri;
	}

	public void setServices(JsonObject services) {
		_services = services;
	}

	public void setComponents(JsonObject components) {
		_components = components;
	}
	
}
