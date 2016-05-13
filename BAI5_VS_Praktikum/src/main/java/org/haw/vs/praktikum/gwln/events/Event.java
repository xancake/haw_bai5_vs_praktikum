package org.haw.vs.praktikum.gwln.events;

import java.util.Objects;
import java.util.regex.Pattern;

public class Event {
	private String id,game,type,name,reason,resource,player,time;
	
	/**
	 * Erzeugt ein neues Event
	 * @param id Die ID des Events
	 * @param game Das Spiel zu dem das Event gehört
	 * @param type Der Typ des Events
	 * @param name Der Name des Events
	 * @param reason Der Grund für das Event
	 * @param resource Die Ressource durch die das Event ausgelöst wurde
	 * @param player Der Spieler der an dem Event beteiligt war
	 * @param time Die Zeit zu der das Event ausgelöst wurde
	 */
	public Event(String id, String game, String type, String name, String reason, String resource, String player, String time) {
		this.id = id;
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
	
	public boolean matches(Event other) {
		return (game == null     || Pattern.matches(game,     other.getGame()))
			&& (type == null     || Pattern.matches(type,     other.getType()))
			&& (name == null     || Pattern.matches(name,     other.getName()))
			&& (reason == null   || Pattern.matches(reason,   other.getReason()))
			&& (resource == null || Pattern.matches(resource, other.getResource()))
			&& (player == null   || Pattern.matches(player,   other.getPlayer()));
	}
}
