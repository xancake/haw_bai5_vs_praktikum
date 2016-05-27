package org.haw.vs.praktikum.gwln.client.restclient.dice;

import java.net.MalformedURLException;

import org.haw.vs.praktikum.gwln.rest.client.AbstractRestClient;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class DiceRestClient extends AbstractRestClient {
	public DiceRestClient(String url) throws MalformedURLException {
		super(url, "/dice");
	}
	
	public String rollDice() throws UnirestException {
		return Unirest.get(getURL().toExternalForm()).asString().getBody();
	}
}
