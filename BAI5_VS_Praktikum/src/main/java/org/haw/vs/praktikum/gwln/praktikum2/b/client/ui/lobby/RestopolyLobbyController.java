package org.haw.vs.praktikum.gwln.praktikum2.b.client.ui.lobby;

import java.util.List;
import org.haw.vs.praktikum.gwln.praktikum2.b.client.games.Game;
import org.haw.vs.praktikum.gwln.praktikum2.b.client.games.GamesRestClient;
import org.haw.vs.praktikum.gwln.praktikum2.b.client.ui.game.RestopolyGameController;
import com.mashape.unirest.http.exceptions.UnirestException;

public class RestopolyLobbyController implements RestopolyLobbyListener_I {
	private GamesRestClient _gamesClient;
	private RestopolyLobbyUI _ui;
	
	
	public RestopolyLobbyController(String gamesURL) {
		_gamesClient = new GamesRestClient(gamesURL);
		_ui = new RestopolyLobbyUI(this);
	}
	
	public void start() {
		try {
			List<Game>games = _gamesClient.getGames();
			games.removeIf((game) -> {
				try {
					return !"registration".equals(_gamesClient.getGameStatus(game.getId()));
				} catch(UnirestException e) {
					e.printStackTrace();
					return false;
				}
			});
			
			_ui.setGames(games);
			_ui.show();
		} catch(UnirestException e) {
			// TODO: Fehlermeldung auf GUI?
			e.printStackTrace();
		}
	}
	
	@Override
	public void onBeitreten(Game game) {
		String user = "user";
		try {
			_gamesClient.registerPlayer(game.getId(), user, "pawn", "account", "false");
			_ui.hide();
			
			RestopolyGameController gameController = new RestopolyGameController(game);
			gameController.start();
			
		} catch(UnirestException e) {
			// TODO: Fehlermeldung auf GUI?
			e.printStackTrace();
		}
	}
}
