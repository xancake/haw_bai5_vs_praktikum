package org.haw.vs.praktikum.gwln.client.ui.lobby;

import java.util.List;

import javax.swing.JOptionPane;

import org.haw.vs.praktikum.gwln.client.restclient.game.Game;
import org.haw.vs.praktikum.gwln.client.restclient.game.GamesRestClient;
import org.haw.vs.praktikum.gwln.client.restclient.user.UserRestClient;
import org.haw.vs.praktikum.gwln.client.ui.game.RestopolyGameController;
import org.haw.vs.praktikum.gwln.yellowpages.Service;
import org.haw.vs.praktikum.gwln.yellowpages.YellowPagesRestClient;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

public class RestopolyLobbyController implements RestopolyLobbyListener_I {
	private YellowPagesRestClient _yellowpages;
	private GamesRestClient _gamesClient;
	private RestopolyLobbyUI _ui;
	
	public RestopolyLobbyController() throws UnirestException {
		_yellowpages = new YellowPagesRestClient(YellowPagesRestClient.HAW_YELLOW_PAGES_INTERNAL);
		_gamesClient = new GamesRestClient(selectServiceOfTypeEndlessly("games"));
		_ui = new RestopolyLobbyUI(this);
	}
	
	public void start() {
		onAktualisieren();
		_ui.show();
	}
	
	@Override
	public void onSpielAnlegen() {
		String name = JOptionPane.showInputDialog("Spielname: ");
		String diceService = selectServiceOfTypeEndlessly("dice");
		String eventService = selectServiceOfTypeEndlessly("events");
		
		try {
			_gamesClient.postGame(name, diceService, eventService);
			onAktualisieren();
		} catch(UnirestException e) {
			_ui.showFehlermeldung(e.toString());
		}
	}
	
	@Override
	public void onAktualisieren() {
		try {
			List<Game> games = _gamesClient.getGames();
			games.removeIf((game) -> {
				try {
					System.out.println("[!!!] " + game);
					String gameStatus = GamesRestClient.getGameStatus(game.getId());
					System.out.println("[!!!] Status: " + gameStatus);
					return !"registration".equals(gameStatus);
				} catch(UnirestException e) {
					e.printStackTrace();
					return false;
				}
			});
			
			_ui.setGames(games);
		} catch(UnirestException e) {
			_ui.showFehlermeldung(e.toString());
		}
	}
	
	@Override
	public void onBeitreten(Game game) {
		try {
			String username = JOptionPane.showInputDialog("USERNAME:");
			
			JSONObject services = GamesRestClient.getGameServices(game.getServices());
			UserRestClient userClient = new UserRestClient(services.getString("users"));
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
	
	private String selectServiceOfTypeEndlessly(String type) {
		String serviceUri = null;
		while(serviceUri == null) {
			try {
				Service service = selectServiceOfType(type);
				if(service != null) {
					serviceUri = service.getUri();
				}
			} catch(UnirestException e) {
				_ui.showFehlermeldung(e.toString());
			}
		}
		return serviceUri;
	}
	
	private Service selectServiceOfType(String type) throws UnirestException {
		List<Service> eventServices = _yellowpages.getServicesOfType(type);
		
		if(!eventServices.isEmpty()) {
			return (Service)JOptionPane.showInputDialog(
					null,
					"Wählen Sie den " + type + "-Service für das Spiel aus!",
					"Service auswählen",
					JOptionPane.QUESTION_MESSAGE,
					null,
					eventServices.toArray(),
					eventServices.get(0)
			);
		} else {
			return null;
		}
	}
}
