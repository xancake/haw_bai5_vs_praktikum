package org.haw.vs.praktikum.gwln.praktikum2.b.client.restclient.user;

import org.json.JSONObject;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class UserRestClient {
	
	private String _url;
	
	public UserRestClient(String url) {
		_url = url;
	}
	
	public void registerUser(String username) throws UnirestException{
		JSONObject json = new JSONObject();
		json.put("id", "/users/" + username.toLowerCase());
		json.put("name", username);
		//TODO set uri
		json.put("uri", "");
		
		Unirest.post(_url + "/users")
				.header("Content-Type", "application/json")
				.body(json)
				.asString();
	}
}
