package org.haw.vs.praktikum.gwln.praktikum2.b.client;

import java.util.List;
import org.haw.vs.praktikum.gwln.praktikum2.b.client.games.DiceRestClient;
import org.haw.vs.praktikum.gwln.praktikum2.b.client.games.Game;
import org.haw.vs.praktikum.gwln.praktikum2.b.client.games.GamesRestClient;
import com.mashape.unirest.http.exceptions.UnirestException;

public class RestopolyClient {
	private static final String URL = "";
	
	public static void main(String... args) throws Exception {
		GamesRestClient client = new GamesRestClient(URL);
		
		List<Game> games = client.getGames();
		games.removeIf((game) -> {
			try {
				return !"registration".equals(client.getGameStatus(game.getId()));
			} catch(UnirestException e) {
				e.printStackTrace();
				return false;
			}
		});
		
		
		Game game = games.get(0);
		String user = "user";
		DiceRestClient dice = new DiceRestClient(game.getServices().get("dice").getAsString());
		client.registerPlayer(game.getId(), user, "pawn", "account", "false");
		
		if(client.isPlayersTurn(game.getId(),user)){
			dice.rollDice();
		}
		
	}
}
