package org.haw.vs.praktikum.gwln.praktikum2.b.client.restclient.game;

import java.util.ArrayList;
import java.util.List;

import org.haw.vs.praktikum.gwln.rest.client.AbstractRestClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class GamesRestClient extends AbstractRestClient {
	private Gson _gson;
	
	public GamesRestClient(String url) {
		super(url, "/games");
		_gson = new Gson();
	}
	
	public List<Game> getGames() throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.get(getUrl()).asJson();
		
		List<Game> games = new ArrayList<Game>();
		JSONArray gamesJson = response.getBody().getArray();
		for(Object gameEntryO : gamesJson) {
			JSONObject gameJson = (JSONObject)gameEntryO;
			
			Game game = _gson.fromJson(gameJson.toString(), Game.class);
			games.add(game);
		}
		
		return games;
	}
	
	public String getGameStatus(String gameId) throws UnirestException {
		return Unirest.get(getUrl() + gameId + "/status").asString().getBody();
	}
	
	public void registerPlayer(String gameId, String user, String pawn, String account, String ready) throws UnirestException {
		JSONObject json = new JSONObject();
		json.put("user", user);
		json.put("pawn", pawn);
		json.put("account", account);
		json.put("ready", ready);
		
		Unirest.post(getUrl() + gameId + "/players")
				.header("Content-Type", "application/json")
				.body(json)
				.asString();
	}
	
	public boolean isPlayersTurn(String gameId, String user) throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.get(getUrl() + gameId + "/players/turn").asJson();
		JSONObject currentPlayer = response.getBody().getObject();
		return currentPlayer.getString("user").equals(user);
	}
}
