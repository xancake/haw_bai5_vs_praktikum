package org.haw.vs.praktikum.gwln.client.restclient.game;

public class Game {
	private String id;
	private String name;
	private String players;
	private String services;
	private String components;
	
	public Game(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPlayers() {
		return players;
	}

	public String getServices() {
		return services;
	}

	public String getComponents() {
		return components;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPlayers(String playersUri) {
		players = playersUri;
	}

	public void setServices(String services) {
		this.services = services;
	}

	public void setComponents(String components) {
		this.components = components;
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", name=" + name + ", players=" + players + ", services=" + services + ", components="
				+ components + "]";
	}
}
