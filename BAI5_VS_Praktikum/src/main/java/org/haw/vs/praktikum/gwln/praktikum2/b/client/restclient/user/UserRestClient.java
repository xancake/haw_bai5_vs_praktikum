package org.haw.vs.praktikum.gwln.praktikum2.b.client.restclient.user;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class UserRestClient {
	private static final String ENDPOINT = "/users";
	
	private String _url;
	
	public UserRestClient(String url) {
		_url = url.endsWith(ENDPOINT) ? url : url + ENDPOINT;
	}
	
	public String registerUser(String username) throws UnirestException{
		
		try {
			JSONObject json = new JSONObject();
			json.put("id", "/users/" + username.toLowerCase());
			json.put("name", username);
			
			String url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":4567";
			
			json.put("uri", url);
			
			HttpResponse<String> response = Unirest.post(_url + "/users")
					.header("Content-Type", "application/json")
					.body(json)
					.asString();
			
			return response.getHeaders().getFirst("Location");
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}
}
