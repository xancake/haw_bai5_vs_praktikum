package org.haw.vs.praktikum.gwln.client;

import org.haw.vs.praktikum.gwln.client.ui.lobby.RestopolyLobbyController;

public class RestopolyClientStarter {
	public static void main(String... args) throws Exception {
		RestopolyLobbyController lobby = new RestopolyLobbyController();
		lobby.start();
	}
}
