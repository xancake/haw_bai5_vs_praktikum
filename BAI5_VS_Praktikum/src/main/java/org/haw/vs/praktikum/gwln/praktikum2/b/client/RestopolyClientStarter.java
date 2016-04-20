package org.haw.vs.praktikum.gwln.praktikum2.b.client;

import org.haw.vs.praktikum.gwln.praktikum2.b.client.ui.lobby.RestopolyLobbyController;

public class RestopolyClientStarter {
	private static final String URL = "";
	
	public static void main(String... args) throws Exception {
		RestopolyLobbyController lobby = new RestopolyLobbyController(URL);
		lobby.start();
	}
}
