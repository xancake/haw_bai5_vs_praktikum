package org.haw.vs.praktikum.gwln.praktikum2.b.client.ui.game;

import java.util.Objects;
import org.haw.vs.praktikum.gwln.praktikum2.b.client.games.DiceRestClient;
import org.haw.vs.praktikum.gwln.praktikum2.b.client.games.Game;
import com.mashape.unirest.http.exceptions.UnirestException;

public class RestopolyGameController implements RestopolyGameListener_I {
	private DiceRestClient _diceClient;
	private RestopolyGameUI _ui;
	private Game _game;
	
	public RestopolyGameController(Game game) {
		_ui = new RestopolyGameUI(this);
		_game = Objects.requireNonNull(game);
		_diceClient = new DiceRestClient(game.getServices().get("dice").getAsString());
	}
	
	public void start() {
		_ui.show();
	}
	
	@Override
	public void onWuerfeln() {
		try {
			_diceClient.rollDice();
		} catch(UnirestException e) {
			// TODO: Fehlermeldung auf GUI?
		}
	}
}
