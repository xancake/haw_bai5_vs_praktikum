package org.haw.vs.praktikum.gwln.client.restclient.game;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpHeader;
import org.haw.vs.praktikum.gwln.rest.client.AbstractRestClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class GamesRestClient extends AbstractRestClient {
	private static final Gson _gson = new Gson();
	
	public GamesRestClient(String url) throws MalformedURLException {
		super(url, "/games");
	}
	
	public List<Game> getGames() throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.get(getURL().toExternalForm()).asJson();
		
		List<Game> games = new ArrayList<Game>();
		JSONArray gamesJson = response.getBody().getArray();
		for(Object gameEntryO : gamesJson) {
			JSONObject json = new JSONObject(gameEntryO.toString());
			String gameURL = json.getString("id");
			if(!gameURL.startsWith("http://")) {
				gameURL = getURL().getHost() + gameURL;
			}
			Game game = getGame(gameURL);
			games.add(game);
		}
		
		return games;
	}
	
	public String postGame(String name, String diceService, String eventService) throws UnirestException {
		JSONObject services = new JSONObject();
		services.put("dice", diceService);
		services.put("events", eventService);
		
		JSONObject json = new JSONObject();
		json.put("name", name);
		json.put("services", services);
		
		HttpResponse<String> response = Unirest.post(getURL().toExternalForm())
				.header(HttpHeader.CONTENT_TYPE.asString(), "application/json")
				.body(json)
				.asString();
		
		return response.getHeaders().getFirst(HttpHeader.LOCATION.asString());
	}
	
	public void registerPlayer(String gameId, String user, String pawn, String account, String ready) throws UnirestException {
		JSONObject json = new JSONObject();
		json.put("user", user);
		json.put("pawn", pawn);
		json.put("account", account);
		json.put("ready", ready);
		
		Unirest.post(getURL().toExternalForm() + gameId + "/players")
				.header("Content-Type", "application/json")
				.body(json)
				.asString();
	}
	
	public static Game getGame(String uri) throws UnirestException{
		HttpResponse<String> response = Unirest.get(uri).asString();
		return _gson.fromJson(response.getBody(), Game.class);
	}
	
	public static String getGameStatus(String url) throws UnirestException {
		return Unirest.get(url + "/status").asString().getBody();
	}
	
	public static JSONObject getGameServices(String uri) throws UnirestException{
		HttpResponse<String> response = Unirest.get(uri).asString();
		return new JSONObject(response.getBody());
	}
	
	public boolean isPlayersTurn(String gameId, String user) throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.get(getURL().toExternalForm() + gameId + "/players/turn").asJson();
		JSONObject currentPlayer = response.getBody().getObject();
		return currentPlayer.getString("user").equals(user);
	}
}
