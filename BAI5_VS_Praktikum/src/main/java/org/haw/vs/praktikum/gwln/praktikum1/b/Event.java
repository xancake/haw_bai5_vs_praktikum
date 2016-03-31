package org.haw.vs.praktikum.gwln.praktikum1.b;

import java.util.Objects;

public class Event {
	private String id,game,type,name,reason,resource,player,time;
	
	public Event(String id, String game, String type, String name, String reason, String resource, String player,
			String time) {
		this.id = Objects.requireNonNull(id);
		this.game = Objects.requireNonNull(game);
		this.type = Objects.requireNonNull(type);
		this.name = Objects.requireNonNull(name);
		this.reason = Objects.requireNonNull(reason);
		this.resource = resource;
		this.player = player;
		this.time = time;
	}

	public String getId() {
		return id;
	}

	public String getGame() {
		return game;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getReason() {
		return reason;
	}

	public String getResource() {
		return resource;
	}

	public String getPlayer() {
		return player;
	}

	public String getTime() {
		return time;
	}

	
}
