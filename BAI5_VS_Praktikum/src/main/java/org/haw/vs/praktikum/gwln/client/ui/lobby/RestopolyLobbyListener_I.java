package org.haw.vs.praktikum.gwln.client.ui.lobby;

import org.haw.vs.praktikum.gwln.client.restclient.game.Game;

public interface RestopolyLobbyListener_I {
	
	void onSpielAnlegen();
	
	
	void onAktualisieren();
	
	
	void onBeitreten(Game game);
}
