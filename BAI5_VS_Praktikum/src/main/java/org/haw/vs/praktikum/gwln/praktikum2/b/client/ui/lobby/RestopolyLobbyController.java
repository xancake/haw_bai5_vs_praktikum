package org.haw.vs.praktikum.gwln.praktikum2.b.client.ui.lobby;

import java.util.List;
import org.haw.vs.praktikum.gwln.praktikum2.b.client.restclient.game.Game;
import org.haw.vs.praktikum.gwln.praktikum2.b.client.restclient.game.GamesRestClient;
import org.haw.vs.praktikum.gwln.praktikum2.b.client.restclient.user.UserRestClient;
import org.haw.vs.praktikum.gwln.praktikum2.b.client.ui.game.RestopolyGameController;
import org.haw.vs.praktikum.gwln.yellowpages.Service;
import org.haw.vs.praktikum.gwln.yellowpages.YellowPagesRestClient;
import com.mashape.unirest.http.exceptions.UnirestException;

public class RestopolyLobbyController implements RestopolyLobbyListener_I {
	private GamesRestClient _gamesClient;
	private RestopolyLobbyUI _ui;
	
	public RestopolyLobbyController(String gameServiceName) throws UnirestException {
		YellowPagesRestClient yellowpages = new YellowPagesRestClient(YellowPagesRestClient.HAW_YELLOW_PAGES_EXTERNAL);
		List<Service> services = yellowpages.getServicesOfName(gameServiceName);
		Service gameService = null;
		for(Service s : services) {
			if("running".equals(s.getStatus())) {
				gameService = s;
				break;
			}
		}
		if(gameService == null) {
			throw new IllegalStateException("Es gibt keinen Service, der auf den Namen '" + gameServiceName + "' registriert wurde!");
		}
		
		_gamesClient = new GamesRestClient(gameService.getUri());
		_ui = new RestopolyLobbyUI(this);
	}
	
	public void start() {
		try {
			List<Game> games = _gamesClient.getGames();
			games.removeIf((game) -> {
				try {
					String gameStatus = _gamesClient.getGameStatus(game.getId().substring("/games".length()));
					return !"registration".equals(gameStatus);
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
		try {
			String username = "";
			
			UserRestClient userClient = new UserRestClient(game.getServices().get("users").getAsString());
			String user = userClient.registerUser(username);
			
			_gamesClient.registerPlayer(game.getId().substring("/games".length()), user, "pawn", "account", "false");
			_ui.hide();
			
			RestopolyGameController gameController = new RestopolyGameController(game);
			gameController.start();
			
		} catch(UnirestException e) {
			// TODO: Fehlermeldung auf GUI?
			e.printStackTrace();
		}
	}
}
