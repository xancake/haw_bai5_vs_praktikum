package org.haw.vs.praktikum.gwln.praktikum2.b.client.restclient.dice;

import org.haw.vs.praktikum.gwln.rest.client.AbstractRestClient;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class DiceRestClient extends AbstractRestClient {
	public DiceRestClient(String url) {
		super(url, "/dice");
	}
	
	public String rollDice() throws UnirestException {
		return Unirest.get(getUrl()).asString().getBody();
	}
}
