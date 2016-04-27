package org.haw.vs.praktikum.gwln.praktikum2.b.client.restclient.dice;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class DiceRestClient {
	private static final String ENDPOINT = "/dice";
	
	private String _url;
	
	public DiceRestClient(String url) {
		_url = url.endsWith(ENDPOINT) ? url : url + ENDPOINT;
	}
	
	public String rollDice() throws UnirestException {
		return Unirest.get(_url).asString().getBody();
	}
}
