package org.haw.vs.praktikum.gwln.client.ui.game;

import java.net.MalformedURLException;
import java.util.Objects;

import org.haw.vs.praktikum.gwln.client.restclient.dice.DiceRestClient;
import org.haw.vs.praktikum.gwln.client.restclient.game.Game;
import org.haw.vs.praktikum.gwln.client.restclient.game.GamesRestClient;
import org.haw.vs.praktikum.gwln.client.service.ClientService;
import org.haw.vs.praktikum.gwln.client.service.ClientServiceListener_I;
import org.haw.vs.praktikum.gwln.events.Event;
import org.haw.vs.praktikum.gwln.events.rest.client.EventManagerRestClient;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

public class RestopolyGameController implements RestopolyGameListener_I, ClientServiceListener_I {
	private RestopolyGameUI _ui;
	private GamesRestClient _gamesClient;
	private DiceRestClient _diceClient;
	private ClientService _clientService;
	private Game _game;
	private String _userURI;
	
	public RestopolyGameController(GamesRestClient gamesClient, Game game, String userURI) throws UnirestException, MalformedURLException {
		_ui = new RestopolyGameUI(this);
		_gamesClient = Objects.requireNonNull(gamesClient);
		_game = Objects.requireNonNull(game);
		JSONObject services = _gamesClient.getGameServices(game.getServices());
		_diceClient = new DiceRestClient(services.getString("dice"));
		_clientService = new ClientService();
		_userURI = userURI;
	}
	
	public void start() {
		_ui.show();
		_clientService.start();
		_clientService.addListener(this);
		try {
			EventManagerRestClient eventManagerRestClient = new EventManagerRestClient(_gamesClient.getGameServices(_game.getServices()).getString("events"));
			Event prototype = new Event("",_game.getId(),"","","","","","");
			eventManagerRestClient.postSubscription(prototype,_game.getId(),_clientService.getUri());
		} catch (UnirestException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onWuerfeln() {
		try {
			_ui.setDiceResult(_diceClient.rollDice());
		} catch(UnirestException e) {
			// TODO: Fehlermeldung auf GUI?
		}
	}

	public void onReady() {
		try {
			_gamesClient.putPlayerReady(_userURI);
		} catch (UnirestException e){
			// TODO: Fehlermeldung auf GUI
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
