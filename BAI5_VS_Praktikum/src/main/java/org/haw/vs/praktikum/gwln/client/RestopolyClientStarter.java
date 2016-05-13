package org.haw.vs.praktikum.gwln.client;

import org.haw.vs.praktikum.gwln.client.ui.lobby.RestopolyLobbyController;

public class RestopolyClientStarter {
	private static final String GAME_SERVICE_NAME = "";
	
	public static void main(String... args) throws Exception {
		RestopolyLobbyController lobby = new RestopolyLobbyController(GAME_SERVICE_NAME);
		lobby.start();
	}
}
