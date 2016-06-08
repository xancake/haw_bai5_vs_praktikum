package org.haw.vs.praktikum.gwln.client.ui.lobby;

import java.net.MalformedURLException;
import java.util.List;

import javax.swing.JOptionPane;

import org.haw.vs.praktikum.gwln.client.restclient.game.Game;
import org.haw.vs.praktikum.gwln.client.restclient.game.GamesRestClient;
import org.haw.vs.praktikum.gwln.client.restclient.user.UserRestClient;
import org.haw.vs.praktikum.gwln.client.ui.game.RestopolyGameController;
import org.haw.vs.praktikum.gwln.player.Player;
import org.haw.vs.praktikum.gwln.yellowpages.Service;
import org.haw.vs.praktikum.gwln.yellowpages.YellowPagesRestClient;

import com.mashape.unirest.http.exceptions.UnirestException;

public class RestopolyLobbyController implements RestopolyLobbyListener_I {
	private YellowPagesRestClient _yellowpages;
	private GamesRestClient _gamesClient;
	private RestopolyLobbyUI _ui;
	
	public RestopolyLobbyController() throws UnirestException, MalformedURLException {
		_yellowpages = new YellowPagesRestClient(YellowPagesRestClient.HAW_YELLOW_PAGES_INTERNAL);
		_ui = new RestopolyLobbyUI(this);
		_ui.setConnectEnabled(true);
		_ui.setRefreshEnabled(false);
		_ui.setCreateEnabled(false);
		_ui.setJoinEnabled(false);
	}
	
	public void start() {
		_ui.show();
	}
	
	@Override
	public void onMitGameServiceVerbinden() {
		try {
			Service gameService = selectServiceOfType("games");
			if(gameService != null) {
				_gamesClient = new GamesRestClient(gameService.getUri());
				_ui.setGameService(gameService);
				_ui.setRefreshEnabled(true);
				_ui.setCreateEnabled(true);
				onAktualisieren();
			}
		} catch(MalformedURLException e) {
			_ui.showFehlermeldung("Der ausgewählte Service verwendet eine ungültige URL:\n" + e.getMessage());
		} catch(UnirestException e) {
			_ui.showFehlermeldung("Fehler beim Verbinden mit dem Game-Service.\n" + e.getMessage());
		}
	}
	
	@Override
	public void onSpielAnlegen() {
		String name = JOptionPane.showInputDialog("Spielname: ");
		String diceService = selectServiceOfTypeEndlessly("dice");
		String eventService = selectServiceOfTypeEndlessly("events");
		// TODO: die anderen Services suchen
		
		try {
			_gamesClient.postGame(name, diceService, eventService);
			onAktualisieren();
		} catch(UnirestException e) {
			_ui.showFehlermeldung(e.toString());
			e.printStackTrace();
		}
	}
	
	@Override
	public void onAktualisieren() {
		try {
			List<Game> games = _gamesClient.getGames();
			games.removeIf((game) -> {
				try {
					System.out.println("[!!!] " + game);
					String gameStatus = _gamesClient.getGameStatus(game.getId());
					System.out.println("[!!!] Status: " + gameStatus);
					return !"registration".equalsIgnoreCase(gameStatus);
				} catch(UnirestException e) {
					e.printStackTrace();
					return false;
				}
			});
			
			_ui.setGames(games);
		} catch(MalformedURLException e) {
			_ui.showFehlermeldung(e.getMessage());
			e.printStackTrace();
		} catch(UnirestException e) {
			_ui.showFehlermeldung(e.toString());
			e.printStackTrace();
		}
	}
	
	@Override
	public void onBeitreten(Game game) {
		try {
			String username = JOptionPane.showInputDialog("USERNAME:");
			
			String usersService = selectServiceOfTypeEndlessly("users");
			UserRestClient userClient = new UserRestClient(usersService);
			String userURI = userClient.registerUser(username);
			
			Player player = _gamesClient.registerPlayer(game.getId().substring("/games".length()), userURI, "", "", "");
			_ui.hide();
			
			RestopolyGameController gameController = new RestopolyGameController(_gamesClient, game, player);
			gameController.start();
			
		} catch(MalformedURLException e) {
			_ui.showFehlermeldung(e.getMessage());
			e.printStackTrace();
		} catch(UnirestException e) {
			_ui.showFehlermeldung(e.getMessage());
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
				e.printStackTrace();
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
