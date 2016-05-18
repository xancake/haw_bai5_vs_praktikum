package org.haw.vs.praktikum.gwln.client.ui.game;

import java.util.Objects;
import org.haw.vs.praktikum.gwln.client.restclient.dice.DiceRestClient;
import org.haw.vs.praktikum.gwln.client.restclient.game.Game;
import org.haw.vs.praktikum.gwln.client.restclient.game.GamesRestClient;
import org.haw.vs.praktikum.gwln.client.service.ClientService;
import org.haw.vs.praktikum.gwln.client.service.ClientServiceListener_I;
import org.haw.vs.praktikum.gwln.events.Event;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

public class RestopolyGameController implements RestopolyGameListener_I, ClientServiceListener_I {
	private DiceRestClient _diceClient;
	private RestopolyGameUI _ui;
	private ClientService _clientService;
	private Game _game;
	
	public RestopolyGameController(Game game) throws UnirestException {
		_ui = new RestopolyGameUI(this);
		_game = Objects.requireNonNull(game);
		JSONObject services = GamesRestClient.getGameServices(game.getServices());
		_diceClient = new DiceRestClient(services.getString("dice"));
		_clientService = new ClientService();
	}
	
	public void start() {
		_ui.show();
		_clientService.start();
		_clientService.addListener(this);
	}
	
	@Override
	public void onWuerfeln() {
		try {
			_diceClient.rollDice();
		} catch(UnirestException e) {
			// TODO: Fehlermeldung auf GUI?
		}
	}

	@Override
	public void onTurn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEvent(Event e) {
		// TODO Auto-generated method stub
		
	}
}
