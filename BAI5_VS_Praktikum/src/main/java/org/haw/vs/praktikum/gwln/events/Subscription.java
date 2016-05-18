package org.haw.vs.praktikum.gwln.events;

import java.util.Objects;

public class Subscription {
	private int _id;
	private String _baseUri;
	private String _game;
	private String _callUri;
	private Event _prototypeEvent;
	
	public Subscription(int id, String baseUri, String game, String callUri, Event prototypeEvent) {
		_id = id;
		_baseUri = Objects.requireNonNull(baseUri);
		_game = Objects.requireNonNull(game);
		_callUri = Objects.requireNonNull(callUri);
		_prototypeEvent = Objects.requireNonNull(prototypeEvent);
	}
	
	public int getId() {
		return _id;
	}
	
	public String getBaseUri() {
		return _baseUri;
	}
	
	public String getUri() {
		return (_baseUri.endsWith("/") ? _baseUri : _baseUri + "/") + _id;
	}
	
	public String getGame() {
		return _game;
	}
	
	public String getCallUri() {
		return _callUri;
	}
	
	public Event getPrototypeEvent() {
		return _prototypeEvent;
	}
}
