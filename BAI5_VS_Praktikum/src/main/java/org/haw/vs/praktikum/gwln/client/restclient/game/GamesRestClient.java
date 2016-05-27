package org.haw.vs.praktikum.gwln.client.restclient.game;

import java.net.MalformedURLException;
import java.net.URL;
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
	
	public List<Game> getGames() throws UnirestException, MalformedURLException {
		System.out.println(Unirest.get(getURL().toExternalForm()).asString().getBody());
		HttpResponse<JsonNode> response = Unirest.get(getURL().toExternalForm()).asJson();
		
		List<Game> games = new ArrayList<Game>();
		JSONArray gamesJson = response.getBody().getArray();
		for(Object gameEntryO : gamesJson) {
			JSONObject json = new JSONObject(gameEntryO.toString());
			String gameURL = json.getString("id");
			if(!gameURL.startsWith("http://")) {
				gameURL = getURL().getProtocol() + "://" + getURL().getAuthority() + gameURL;
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
		
		HttpResponse<String> response = Unirest.post(getURL().getProtocol() + "://" + getURL().getAuthority() + "/games")
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
		
		Unirest.post(getURL().getProtocol() + "://" + getURL().getAuthority() + "/games/" + gameId + "/players")
				.header("Content-Type", "application/json")
				.body(json)
				.asString();
	}
	
	public Game getGame(String uri) throws UnirestException, MalformedURLException {
		return getGame(new URL(getURL(), uri));
	}
	
	public String getGameStatus(String uri) throws UnirestException {
		return Unirest.get(getURL().getProtocol() + "://" + getURL().getAuthority() + uri + "/status").asString().getBody();
	}
	
	public JSONObject getGameServices(String uri) throws UnirestException{
		HttpResponse<String> response = Unirest.get(getURL().getProtocol() + "://" + getURL().getAuthority() + uri).asString();
		return new JSONObject(response.getBody());
	}
	
	public boolean isPlayersTurn(String gameId, String user) throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.get(getURL().getProtocol() + "://" + getURL().getAuthority() + "/games/" + gameId + "/players/turn").asJson();
		JSONObject currentPlayer = response.getBody().getObject();
		return currentPlayer.getString("user").equals(user);
	}
	
	public static Game getGame(URL url) throws UnirestException {
		HttpResponse<String> response = Unirest.get(url.toExternalForm()).asString();
		return _gson.fromJson(response.getBody(), Game.class);
	}
}
